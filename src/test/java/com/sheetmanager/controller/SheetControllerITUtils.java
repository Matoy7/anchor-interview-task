package com.sheetmanager.controller;

public class SheetControllerITUtils {
    public static String getSheetBody() {

        return "{\n" +
                "    \"columns\": [\n" +
                "        {\n" +
                "            \"name\": \"A\",\n" +
                "            \"type\": \"boolean\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"B\",\n" +
                "            \"type\": \"int\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"C\",\n" +
                "            \"type\": \"double\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"D\",\n" +
                "            \"type\": \"string\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    public static String getCellRequestBodyWithLookup(Object content) {
        return "{\n" +
                "    \"content\": " + content + "\n" +
                "}";
    }

    public static String getCellRequestBodyWithLookup(String colName, int rowNumber) {
        return "{\n" +
                "    \"content\": \"lookup(" + colName + "," + rowNumber + ")\"\n" +
                "}";
    }
}
