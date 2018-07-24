package com.sf.demo.view.highlight.pager;

import android.app.Activity;
import android.view.View;

import com.sf.demo.R;

/**
 * Created by sufan on 17/7/28.
 */

public class Pager4 extends BasePager {


    public Pager4(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_tab4, null);
        return view;
    }

    public void initData(){

    }
}
