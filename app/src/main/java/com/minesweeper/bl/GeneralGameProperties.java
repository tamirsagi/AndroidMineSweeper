package com.minesweeper.bl;

/**
 * Created by Administrator on 11/3/2015.
 */
public class GeneralGameProperties {


    public static final String Level = "Level";
    public static final String NumberOfBombs = "NumberOfBombs";
    public static final String RowsInBoard = "Rows";
    public static final String ColumnsInBoard = "Columns";
    public static final String MineOnBoard = "Mine";


    public final static String EeasySettings =
            "{"

                    + "      \\\"Level\\\" : \\\"Beginner\\\",\"\n" +
                    "                    + \"      \\\"Mine\\\": \\\"10\\\",\"\n" +
                    "                    + \"      \\\"Rows\\\": \\\"9\\\",\"\n" +
                    "                    + \"      \\\"Columns\\\" : \\\"9\","
                    + "    }";

    public final static String IntermediateSettings =
            "{"

                    + "      \"Level\" : \"Intermediate\","
                    + "      \"Mine\": \"40\","
                    + "      \"Rows\": \"16\","
                    + "      \"Columns\" : \"16\","
                    + "    }";

    public final static String ExpertSettings =
            "{"

                    + "      \"Level\" : \"Expert\","
                    + "      \"Mine\": \"99\","
                    + "      \"Rows\": \"16\","
                    + "      \"Columns\" : \"31\","
                    + "    }";






}
