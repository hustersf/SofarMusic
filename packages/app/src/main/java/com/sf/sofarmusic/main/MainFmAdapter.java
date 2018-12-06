package com.sf.sofarmusic.main;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sufan on 16/11/8.
 */

public class MainFmAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFmList;

    public MainFmAdapter(FragmentManager fm, List<Fragment> fmList) {
        super(fm);
        mFmList=fmList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFmList.get(position);
    }

    @Override
    public int getCount() {
        return mFmList.size();
    }
}
