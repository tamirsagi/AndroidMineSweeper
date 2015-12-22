package com.minesweeper.BL.GameLogic;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.*;
import com.minesweeper.UI.Activities.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tamir Sagi
 *         This Application was created as part of academic course
 */
public class ButtonAdapter extends ArrayAdapter<Cell> {

    private Context mContext;
    private int mLayoutResourceId;
    private Cell[][] mGameBoard;
    private int mBoardSize;
    private int mRow, mColumns;
    private int mCellSizeInDP;
    private List<View> views;
    private int delay;

    public ButtonAdapter(Context context, int layoutResourceId, Cell[][] gameBoard, int cellSizeInDP) {
        super(context, layoutResourceId, new ArrayList());
        this.mGameBoard = gameBoard;
        mRow = gameBoard.length;
        mColumns = gameBoard[0].length;
        mBoardSize = mRow * mColumns;
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        //layoutParams = new AbsListView.LayoutParams(mCellSizeInDP,mCellSizeInDP);
        this.mCellSizeInDP = cellSizeInDP;
        views = new ArrayList<View>();

    }

    @Override
    public int getCount() {
        return mBoardSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder = null;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.tv_cell = (TextView) convertView.findViewById(R.id.buttonInGrid);
            convertView.setTag(holder);
            views.add(convertView);
        } else
            holder = (RecordHolder) convertView.getTag();
        int clickedRow = position % mRow;
        int clickedColumn = position / mRow;
        Cell cell = mGameBoard[clickedRow][clickedColumn];
        holder.tv_cell.setBackgroundResource(cell.getCellImage());
        if (cell.isCellMarked() && cell.isRevealed())
            holder.tv_cell.setText("" + cell.getNumberOfAdjacentMines());
        else
            holder.tv_cell.setText("");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(2 * mCellSizeInDP, 2 * mCellSizeInDP);
        convertView.setLayoutParams(new GridView.LayoutParams(params));



        return convertView;
    }

    public void setGameBoard(Cell[][] gameBoard) {
        this.mGameBoard = gameBoard;
        notifyDataSetInvalidated();

    }

    public int getmCellSizeInDP(){
        return mCellSizeInDP;
    }

    static class RecordHolder {
        TextView tv_cell;
    }

    public void playAnimation() {
        for(View v : views) {
            ObjectAnimator animY = ObjectAnimator.ofFloat(v, "y", 1000);
            ObjectAnimator rotateView = ObjectAnimator.ofFloat(v, "rotation", 0,360);
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(animY,rotateView);
            animSetXY.setStartDelay(delay);
            delay += 100;
            animSetXY.start();
            v.invalidate();
        }
        delay = 0;
        views.clear();
    }
}