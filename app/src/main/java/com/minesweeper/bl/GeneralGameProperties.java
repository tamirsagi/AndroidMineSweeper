/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */


package com.minesweeper.bl;


public class GeneralGameProperties {


    public static final String Level = "Level";
    public static final String RowsInBoard = "Rows";
    public static final String ColumnsInBoard = "Columns";
    public static final String MinesOnBoard = "Mines";


    public final static String EeasySettings =
            "{"

                    + "      \\\"Level\\\" : \\\"Beginner\\\",\"\n" +
                    "                    + \"      \\\"Mines\\\": \\\"10\\\",\"\n" +
                    "                    + \"      \\\"Rows\\\": \\\"9\\\",\"\n" +
                    "                    + \"      \\\"Columns\\\" : \\\"9\","
                    + "    }";

    public final static String IntermediateSettings =
            "{"

                    + "      \"Level\" : \"Intermediate\","
                    + "      \"Mines\": \"40\","
                    + "      \"Rows\": \"16\","
                    + "      \"Columns\" : \"16\","
                    + "    }";

    public final static String ExpertSettings =
            "{"

                    + "      \"Level\" : \"Expert\","
                    + "      \"Mines\": \"99\","
                    + "      \"Rows\": \"16\","
                    + "      \"Columns\" : \"31\","
                    + "    }";






}
