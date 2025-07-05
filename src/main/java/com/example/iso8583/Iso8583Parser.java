package com.example.iso8583;

import java.util.BitSet;
import java.util.Map;
import java.util.HashMap;

/**
 * A very small ISO8583 parser example. This is NOT production ready and only
 * demonstrates parsing of a simple bitmap message with fixed-length fields.
 */
public class Iso8583Parser {

    // Basic length for a primary or secondary bitmap (64 bits => 16 hex chars)
    private static final int BITMAP_LENGTH = 16;

    // Example field lengths for a few data elements. Fields not listed will be
    // treated as simple LLVAR (length encoded with two digits).
    private static final Map<Integer, Integer> FIELD_LENGTHS = new HashMap<>();
    static {
        // Field number -> length in characters (for demonstration)
        FIELD_LENGTHS.put(2, 16);  // Primary account number (PAN)
        FIELD_LENGTHS.put(3, 6);   // Processing code
        FIELD_LENGTHS.put(4, 12);  // Amount, transaction
        FIELD_LENGTHS.put(7, 10);  // Transmission date & time
        FIELD_LENGTHS.put(11, 6);  // Systems trace audit number
        FIELD_LENGTHS.put(12, 6);  // Time, local transaction
        FIELD_LENGTHS.put(13, 4);  // Date, local transaction
        FIELD_LENGTHS.put(70, 3);  // Network management information code
    }

    /**
     * Parses a raw ISO8583 message string assuming ASCII encoding. This method
     * supports both primary and secondary bitmaps and falls back to LLVAR for
     * fields without configured fixed lengths.
     */
    public Iso8583Message parse(String message) {
        Iso8583Message isoMsg = new Iso8583Message();
        int index = 0;

        // Parse MTI (4 chars)
        String mti = message.substring(index, index + 4);
        isoMsg.setMti(mti);
        index += 4;

        // Parse primary bitmap (16 hex characters => 64 bits)
        String bitmapHex = message.substring(index, index + BITMAP_LENGTH);
        index += BITMAP_LENGTH;

        BitSet bitmap = parseBitmap(bitmapHex);

        // If the first bit is set a secondary bitmap follows
        if (bitmap.get(0)) {
            String secondaryHex = message.substring(index, index + BITMAP_LENGTH);
            index += BITMAP_LENGTH;
            BitSet secondary = parseBitmap(secondaryHex);
            for (int i = 0; i < 64; i++) {
                bitmap.set(64 + i, secondary.get(i));
            }
        }

        // Parse fields indicated in bitmap
        for (int field = 2; field <= 128; field++) {
            if (bitmap.get(field - 1)) { // bitmap bit positions start at 0
                Integer length = FIELD_LENGTHS.get(field);
                String value;
                if (length != null && length > 0) {
                    value = message.substring(index, index + length);
                    index += length;
                } else {
                    // Default to LLVAR (2 digit length prefix)
                    int len = Integer.parseInt(message.substring(index, index + 2));
                    index += 2;
                    value = message.substring(index, index + len);
                    index += len;
                }
                isoMsg.setDataElement(field, value);
            }
        }

        return isoMsg;
    }

    private BitSet parseBitmap(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int pos = i * 2;
            bytes[i] = (byte) Integer.parseInt(hex.substring(pos, pos + 2), 16);
        }
        return BitSet.valueOf(bytes);
    }
}
