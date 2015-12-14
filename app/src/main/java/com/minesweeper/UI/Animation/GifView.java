package com.minesweeper.UI.Animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;
import com.minesweeper.UI.Activities.R;

import java.io.InputStream;
import java.util.Random;

/**
 * Represent gif animation when game is over (win or lose)
 */
public class GifView extends View {

    private InputStream gifInputStream;
    private Movie gifMovie;
    private int movieWidth;
    private int movieHeight;
    private long movieDuration;
    private long movieStart;

    boolean won;


    public GifView(Context context) {
        super(context,null);
        init(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
}

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setEndGameState(boolean won){
        this.won = won;

    }

    private void init(Context context) {
        setFocusable(true);
        setVisibility(View.VISIBLE);
        gifInputStream = context.getResources().openRawResource(getRaw());
        gifMovie = Movie.decodeStream(gifInputStream);
        movieWidth = gifMovie.width();
        movieHeight = gifMovie.height();
        movieDuration = gifMovie.duration();


    }

    private int getRaw() {
        if (won)
            return R.drawable.dancing_banana;
        else {
            if ((new Random()).nextInt() % 2 == 0)
                return R.drawable.explosion1;
            else
                return R.drawable.explosion2;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(movieWidth, movieHeight);
    }


    public int getMovieHeight() {
        return movieHeight;
    }

    public int getMovieWidth() {
        return movieWidth;
    }

    public long getMovieDuration() {
        return movieDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = System.currentTimeMillis();

        if (movieStart == 0)
            movieStart = now;
        if (gifMovie != null) {
            int dur = gifMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }
            int realTime = (int) ((now - movieStart) % dur);
            gifMovie.setTime(realTime);
            gifMovie.draw(canvas, canvas.getWidth() / 2, canvas.getHeight() / 2);
            invalidate();
        }
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        stopGif();
    }

    public void stopGif(){
        gifMovie = null;
        setVisibility(View.GONE);
    }

    public void play(){
        invalidate();
    }
}
