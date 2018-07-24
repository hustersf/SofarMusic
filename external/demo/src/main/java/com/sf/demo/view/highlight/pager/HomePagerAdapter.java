package com.sf.demo.view.highlight.pager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by sufan on 17/7/28.
 */

public class HomePagerAdapter extends PagerAdapter {

    private List<BasePager> mPagerList;

    public HomePagerAdapter(List<BasePager> pagerList){
        mPagerList=pagerList;
    }


    @Override
    public int getCount() {
        return mPagerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mPagerList.get(position).mRootView);
        return mPagerList.get(position).mRootView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mPagerList.get(position).mRootView);
    }
}
