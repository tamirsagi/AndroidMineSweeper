


package com.minesweeper.BL.GameLogic;

/**
 * @author Tamir Sagi
 * This Application was created as part of academic course
 */
public class GeneralGameProperties {

    public static final String KEY_PLAYER_FULL_NAME = "playerFullName";
    public static final String KEY_GAME_LEVEL = "gameLevel";
    public static final String KEY_GAME_BOARD_ROWS = "gameBoardRows";
    public static final String KEY_GAME_BOARD_COLUMNS = "gameBoardColumns";
    public static final String KEY_GAME_BOARD_MINES = "gameBoarMines";
    public static final String KEY_Play_Sound = "soundSettings";
    public static final String KEY_Play_Animation = "animationSettings";

    public static final String Level = "Level";
    public static final String RowsInBoard = "Rows";
    public static final String ColumnsInBoard = "Columns";
    public static final String MinesOnBoard = "Mines";

    public static final String BEGINNER = "Beginner";
    public static final String INTERMEDIATE = "Intermediate";
    public static final String EXPERT = "Expert";
    public static final String CUSTOM = "Custom";


    public static final String DANCING_BANANA_ANIMATION_1_URL = "https://s-media-cache-ak0.pinimg.com/originals/78/15/ce/7815cee28667b7e6d7479dba530a0219.jpg";
    public static final String EXPLOSION_1_URL = "http://i.imgur.com/O0UGS6y.gif";

    private final static String BeginnerSettings =
            "{"

                    + "      \"Level\" : \"Beginner\","
                    + "      \"Mines\": \"5\","
                    + "      \"Rows\": \"5\","
                    + "      \"Columns\" : \"5\""
                    + "    }";

    private final static String IntermediateSettings =
            "{"

                    + "      \"Level\" : \"Intermediate\","
                    + "      \"Mines\": \"15\","
                    + "      \"Rows\": \"9\","
                    + "      \"Columns\" : \"9\""
                    + "    }";

    private final static String ExpertSettings =
            "{"

                    + "      \"Level\" : \"Expert\","
                    + "      \"Mines\": \"25\","
                    + "      \"Rows\": \"16\","
                    + "      \"Columns\" : \"16\""
                    + "    }";




    public static String getGameSettings(String level){
        if(level.equals(BEGINNER))
                return BeginnerSettings;
        else if(level.equals(INTERMEDIATE))
                return IntermediateSettings;
        else if (level.equals(EXPERT))
                return ExpertSettings;
        return CUSTOM;

    }

}
