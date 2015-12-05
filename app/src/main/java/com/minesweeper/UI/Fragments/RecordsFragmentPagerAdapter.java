package com.minesweeper.UI.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class RecordsFragmentPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"Table", "Map"};
    private Context context;

    public enum PAGES {
        RECORDS(0), MAP(1);

        private static final Map<Integer, PAGES> lookup
                = new HashMap<Integer, PAGES>();

        static {
            for (PAGES w : EnumSet.allOf(PAGES.class))
                lookup.put(w.getNumVal(), w);
        }

        private int code;

        PAGES(int code) {
            this.code = code;
        }

        public int getNumVal() {
            return code;
        }

        public static PAGES get(int code) {
            return lookup.get(code);
        }
    }


    public RecordsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (PAGES.get(position)) {
            case RECORDS:
                return RecordsTable.newInstance(position + 1);
        }

        return new Fragment();

    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];


    }
}
