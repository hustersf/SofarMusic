package com.sf.sofarmusic.demo.view.highlight.pager;

import android.app.Activity;
import android.view.View;

/**
 * Created by sufan on 17/7/28.
 */

public abstract class BasePager {

    public Activity mActivity;
    public View mRootView;

    //页面是否加载过数据,非刷新
    private boolean isPagerDataLoaded=false;


    public BasePager(Activity activity){
        mActivity=activity;
        mRootView=initView();
    }

    //初始化布局
    public abstract View initView();

    public boolean isPagerDataLoaded() {
        return isPagerDataLoaded;
    }

    public void setPagerDataLoaded(boolean pagerDataLoaded) {
        isPagerDataLoaded = pagerDataLoaded;
    }

}
