package com.sf.demo.view.highlight.fragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by sufan on 17/7/27.
 */

public class HomeFmAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFmList;

    public HomeFmAdapter(FragmentManager fm, List<Fragment> fmList) {
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
