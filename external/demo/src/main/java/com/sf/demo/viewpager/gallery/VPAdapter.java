package com.sf.demo.viewpager.gallery;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by sufan on 17/6/24.
 */

public abstract class VPAdapter<T> extends PagerAdapter {

    protected Context mContext;
    private List<View> mViews;


    public VPAdapter(Context context, List<T> datas) {
        mContext = context;
        mViews = new ArrayList<>();

        for (T data : datas) {
            mViews.add(getItemView(data));
        }
    }


    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    public abstract View getItemView(T data);


}
