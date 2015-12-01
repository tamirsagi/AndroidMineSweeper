/**
 * This Application was created as part of academic course
 * Tamir Sagi
 */

package com.minesweeper.BL.GameLogic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.minesweeper.UI.Activities.R;

import java.util.ArrayList;

public class ButtonAdapter extends ArrayAdapter<Cell> {

    private Context context;
    int layoutResourceId;
    private Cell[][] gameBoard;
    private int boardSize;
    private int row, columns;
    private AbsListView.LayoutParams layoutParams;
    private int cellSizeInDP;

    public ButtonAdapter(Context context, int layoutResourceId, Cell[][] gameBoard, int cellSizeInDP) {
        super(context, layoutResourceId, new ArrayList());
        this.gameBoard = gameBoard;
        row = gameBoard.length;
        columns = gameBoard[0].length;
        boardSize = row * columns;
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        //layoutParams = new AbsListView.LayoutParams(cellSizeInDP,cellSizeInDP);
        this.cellSizeInDP = cellSizeInDP;
    }

    @Override
    public int getCount() {
        return boardSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder = null;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.tv_cell = (TextView) convertView.findViewById(R.id.buttonInGrid);
            convertView.setTag(holder);
        } else {
            holder = (RecordHolder) convertView.getTag();
        }
        int clickedRow = position % row;
        int clickedColumn = position / row;
        Cell cell = gameBoard[clickedRow][clickedColumn];
        holder.tv_cell.setBackgroundResource(cell.getCellImage());
        if (cell.isCellMarked() && cell.isRevealed())
            holder.tv_cell.setText("" + cell.getNumberOfAdjacentMines());
        else
            holder.tv_cell.setText("");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(2 * cellSizeInDP, 2 * cellSizeInDP);
        convertView.setLayoutParams(new GridView.LayoutParams(params));

        return convertView;
    }

    public void setGameBoard(Cell[][] gameBoard) {
        this.gameBoard = gameBoard;
        notifyDataSetInvalidated();
    }

    static class RecordHolder {
        TextView tv_cell;
    }


}