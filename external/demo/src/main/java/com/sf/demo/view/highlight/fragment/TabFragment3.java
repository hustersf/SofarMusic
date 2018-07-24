package com.sf.demo.view.highlight.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.base.BaseFragment;
import com.sf.demo.R;

/**
 * Created by sufan on 17/7/27.
 */

public class TabFragment3 extends BaseFragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab3, container, false);
        initView();
        return view;
    }

    public void initData(){

    }

    private void initView(){

    }

    private void initEvent(){

    }
}
