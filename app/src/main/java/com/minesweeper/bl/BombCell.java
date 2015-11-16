/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */



package com.minesweeper.BL;
import com.minesweeper.app.R;

public class BombCell extends Cell{

    public BombCell(int row, int column){
        super(CellType.BOMB,row,column);
    }

    @Override
    public int getCellImage(){
        if(isClicked())
            return R.drawable.clicked_bomb_cell;
       else if(isRevealed()) {
            if (isFlagged())
                return R.drawable.disabled_clicked_bomb_cell;
            return R.drawable.bomb_cell;
        }
       else
            return super.getCellImage();
    }


}
