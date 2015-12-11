package com.minesweeper.UI.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import com.minesweeper.BL.DB.DbManager;
import com.minesweeper.UI.Fragments.RecordsFragmentPagerAdapter;

public class DBRecordsFragmentActivity extends FragmentActivity{

    public static String mDefaultTable = DbManager.Tables.PLAYERS_RECORDS_INTERMEDIATE.toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbrecords_fragments);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pagersViewer);
        viewPager.setAdapter(new RecordsFragmentPagerAdapter(getSupportFragmentManager()
                ,DBRecordsFragmentActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


    }





}
