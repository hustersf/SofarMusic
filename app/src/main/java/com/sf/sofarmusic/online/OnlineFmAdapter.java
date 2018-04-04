package com.sf.sofarmusic.online;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by sufan on 16/11/9.
 */

public class OnlineFmAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFmList;
    private List<String> mTitleList;

    public OnlineFmAdapter(FragmentManager fm, List<Fragment> fmList, List<String> titleList) {
        super(fm);
        mFmList=fmList;
        mTitleList=titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFmList.get(position);
    }

    @Override
    public int getCount() {
        return mFmList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
