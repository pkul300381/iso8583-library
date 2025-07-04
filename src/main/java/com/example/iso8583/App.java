package com.example.iso8583;

/**
 * Simple application example for parsing a fixed ISO8583 message and printing
 * the JSON representation.
 */
public class App {
    public static void main(String[] args) {
        // Example ISO8583 message string demonstrating both primary and
        // secondary bitmaps. Format:
        // MTI + primary bitmap [+ secondary bitmap] + data elements
        String message = "0200" +
                "F238000000000000" + // Primary bitmap: fields 2,3,4,7,11,12,13 and secondary indicator
                "0400000000000000" + // Secondary bitmap: field 70
                "1234567890123456" + // Field 2
                "000000" +             // Field 3
                "000000010000" +       // Field 4
                "0707221800" +         // Field 7
                "123456" +             // Field 11
                "221800" +             // Field 12
                "0707" +               // Field 13
                "001";                // Field 70

        Iso8583Parser parser = new Iso8583Parser();
        Iso8583Message isoMsg = parser.parse(message);

        System.out.println(isoMsg.toJson());
    }
}
