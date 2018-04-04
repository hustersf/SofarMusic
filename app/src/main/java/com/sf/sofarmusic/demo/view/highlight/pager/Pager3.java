package com.sf.sofarmusic.demo.view.highlight.pager;

import android.app.Activity;
import android.view.View;

import com.sf.sofarmusic.R;

/**
 * Created by sufan on 17/7/28.
 */

public class Pager3 extends BasePager {


    public Pager3(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_tab3, null);
        return view;
    }

    public void initData(){

    }
}
