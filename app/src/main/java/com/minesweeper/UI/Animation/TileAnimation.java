package com.minesweeper.UI.Animation;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.minesweeper.UI.Activities.R;

import java.util.Random;

/**
 * @author  Tamir Sagi
 * This class manges the falling tiles animation using translate
 */
public class TileAnimation {

    private static final int NUMBER_OF_IMAGES = 3;
    private static final int MIN_MILI_SECONDS = 1000;
    private static final int MAX_MILI_SECONDS = 5000;
    private static final int REPEATING = 1;

    private Animation mAnimation;
    private ImageView image;
    private Random random;


    public TileAnimation(Activity act) {
        random = new Random();

        //get the view
        View view = act.getWindow().getDecorView().getRootView();
       final RelativeLayout root = (RelativeLayout) view.findViewById(R.id.layout_GameActivity);

        //start the tile somewhere along the screen width
        int xSet = random.nextInt(view.getWidth());

        //create image view
        image = new ImageView(view.getContext());
        //get the drawable object
        Drawable dImage = ContextCompat.getDrawable(view.getContext(), getImage());
        image.setBackground(dImage);
        root.addView(image);

        //create animation Transition
        mAnimation = new TranslateAnimation(xSet,xSet,0,view.getHeight());//AnimationUtils.loadAnimation(context, getTileAnimation());
        mAnimation.setDuration(random.nextInt(MAX_MILI_SECONDS) + MIN_MILI_SECONDS);
        mAnimation.setRepeatCount(REPEATING);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                root.removeView(image);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    /**
     * random drawable cell
     * @return
     */
    private int getImage(){

        int choice = random.nextInt() % NUMBER_OF_IMAGES;
        switch (choice){
            case 0:
                return R.drawable.redcell;
            case 1:
                return R.drawable.bluecell;
            default:
                return R.drawable.greencell;
        }
    }

    public void playAnimation(){
        image.startAnimation(mAnimation);
    }

}
