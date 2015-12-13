package com.minesweeper.UI.Animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import com.minesweeper.UI.Activities.R;

import java.util.Random;

/**
 * Created by Administrator on 12/12/2015.
 */
public class TileAnimation extends View {

    private static Random rand = new Random();
    private static final int NUMBER_OF_DIFFERENT_CELL_COLORS = 4;

    private Bitmap mTile;
    private float changingY;
    private float initialX;


    public TileAnimation(Context context) {

        super(context);
        mTile = BitmapFactory.decodeResource(getResources(), getDrawableCell());
        changingY = 0;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initialX = rand.nextInt(canvas.getWidth());
       // resizeTile();
        canvas.drawBitmap(mTile, initialX, changingY, null);
        if (changingY < canvas.getHeight()) {
            changingY += rand.nextInt(10) + 5; //speed between 5 - 10
            invalidate();
        }
    }

    private int getDrawableCell() {
        int num = rand.nextInt() % NUMBER_OF_DIFFERENT_CELL_COLORS;
        switch(num){
            case 0:
                return R.drawable.yellocell;
            case 1:
                return R.drawable.redcell;
            case 2:
                return R.drawable.bluecell;
            case 3:
                return R.drawable.greencell;
            default:
                return R.drawable.greencell;
        }
    }

    private void resizeTile() {
        int height = rand.nextInt((int) (0.8 * mTile.getHeight())) + 1;
        int width = rand.nextInt((int) (0.8 * mTile.getWidth())) + 1;
        mTile = Bitmap.createScaledBitmap(mTile, width, height, false);
    }

    public void play() {
        invalidate();
    }
}
