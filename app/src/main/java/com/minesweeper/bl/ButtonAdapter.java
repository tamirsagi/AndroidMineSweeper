package com.minesweeper.BL;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.minesweeper.app.R;

import java.util.ArrayList;

public class ButtonAdapter extends ArrayAdapter<Cell> {

    private Context context;
    int layoutResourceId;
    private Cell[][] gameBoard;
    private int boardsize;
    private int row, columns;

    public ButtonAdapter(Context context, int layoutResourceId, Cell[][] gameBoard) {
        super(context, layoutResourceId, new ArrayList());
        this.gameBoard = gameBoard;
        row = gameBoard.length;
        columns = gameBoard[0].length;
        boardsize = row * columns;
        this.context = context;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        return boardsize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder = null;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.button = (TextView) convertView.findViewById(R.id.BTH_Cell);
            convertView.setTag(holder);
        } else {
            holder = (RecordHolder) convertView.getTag();
        }

        int clickedRow = position % row;
        int clickedColumn = position / row;
        Cell cell = gameBoard[clickedRow][clickedColumn];
        int adjecentMine = cell.getNumberOfAdjacentMines();
        holder.button.setBackgroundResource(cell.getCellImage());
        if (adjecentMine > 0)
            holder.button.setText("" + adjecentMine);
        else
            holder.button.setText("");
        return convertView;
    }

    public void setGameBoard(Cell[][] gameBoard) {
        this.gameBoard = gameBoard;
        notifyDataSetInvalidated();
    }

    static class RecordHolder {
        TextView button;
    }

}