package com.minesweeper.UI.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import com.minesweeper.BL.DB.DbManager;
import com.minesweeper.UI.Fragments.RecordsFragmentPagerAdapter;


/**
 * @author Tamir Sagi
 *         This Class manages the Tabs and pager(which contains 2 fragments, Records and the map)
 */
public class DBRecordsFragmentActivity extends FragmentActivity {

    public static String mDefaultTable = DbManager.Tables.PLAYERS_RECORDS_INTERMEDIATE.toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbrecords_fragments);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pagersViewer);
        final FragmentStatePagerAdapter adapter = new RecordsFragmentPagerAdapter(getSupportFragmentManager()
                , DBRecordsFragmentActivity.this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                if(RecordsFragmentPagerAdapter.PAGES.get(position).equals(RecordsFragmentPagerAdapter.PAGES.MAP))
//                    viewPager.getChildAt(position)
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);



    }


}
