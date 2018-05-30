package com.example.rahul.placessearch;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Details_Tab extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "INFO", "PHOTOS", "MAP","REVIEWS" };
    private Context context;

    public Details_Tab(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                return Info.newInstance(position + 1);
            case 1:
                return Photos.newInstance(position + 1);
            case 2:
                return Maps.newInstance(position + 1);
            case 3:
                return Reviews.newInstance(position + 1);
            default:
                return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}