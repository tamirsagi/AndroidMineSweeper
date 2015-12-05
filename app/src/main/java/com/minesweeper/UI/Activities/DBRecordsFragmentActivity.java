package com.minesweeper.UI.Activities;

import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.minesweeper.UI.Fragments.RecordsFragmentPagerAdapter;
import com.minesweeper.UI.Fragments.RecordsTable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class DBRecordsFragmentActivity extends AppCompatActivity{

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
