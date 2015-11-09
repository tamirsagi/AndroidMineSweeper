/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */
package com.minesweeper.Events;


import com.minesweeper.BL.Cell;
import com.minesweeper.BL.MineSweeperLogicManager;

import java.util.List;

/**
 * This Interface set the events which will be launched from BL
 */
public interface BlEvents {

    /**
     * public fire an event from BL to  Game Activity
     ** @param cellsToReveal : List of cells to reveal
     */
    void applyMove(List<Cell> cellsToReveal);

    void endGame(List<Cell> mines,MineSweeperLogicManager.Result endGameResult);







}
