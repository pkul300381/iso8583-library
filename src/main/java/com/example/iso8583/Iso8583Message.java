package com.example.iso8583;

import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Represents a parsed ISO8583 message with data elements stored in a map.
 */
public class Iso8583Message {
    private String mti;
    private Map<Integer, String> dataElements = new LinkedHashMap<>();

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public void setDataElement(int field, String value) {
        dataElements.put(field, value);
    }

    public String getDataElement(int field) {
        return dataElements.get(field);
    }

    public Map<Integer, String> getAllDataElements() {
        return dataElements;
    }

    /**
     * Returns a simple JSON representation of this message.
     */
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"mti\": \"").append(mti).append("\",");
        sb.append("\n  \"dataElements\": {");
        boolean first = true;
        for (Map.Entry<Integer, String> entry : dataElements.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            sb.append("\n    \"").append(entry.getKey()).append("\": \"")
              .append(entry.getValue()).append("\"");
            first = false;
        }
        sb.append("\n  }\n}");
        return sb.toString();
    }
}
