package com.sf.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by sufan on 16/11/17.
 * 懒加载+数据缓存
 * 当fragement对用户可见时，才加载数据，且只加载一次
 */

public abstract class LazyLoadBaseFragment extends BaseFragment {

    protected boolean isInit; //是否初始化过view
    protected View mView;


    /**
     * 调用多次，tab切换一次调用一次
     * 当不可见时，isInit置为false，当再次可见时也不调用initData了
     * 在Fragment任何生命周期之前回调
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isInit) {
            initData();
        } else {
            isInit = false;
        }
    }


    /**
     * 在onCreatedView之后调用，将initView放在这，是确保将布局加载完成之后才进行初始化
     * 只调用一次，且在一进入viewpager，就已经执行了，因此在真正切换tab时，该方法不会在调用了
     * setOffscreenPageLimit(num)，即viewpager中的fragment是否预加载，由num决定
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView=view;
        initView();
        initEvent();
        isInit=true;
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initEvent();

}
