package com.minesweeper.BL;

import com.minesweeper.app.R;


/**
 * Created by Administrator on 11/13/2015.
 */
public class BombCell extends Cell{

    public BombCell(int row, int column){
        super(CellType.BOMB,row,column);
    }

    @Override
    public int getCellImage(){
        if(isClicked())
            return R.drawable.disabled_clicked_bomb_cell;
        else if(isRevealed())
            return R.drawable.bomb_cell;
        else
            return R.drawable.empty_cell;
    }


}
