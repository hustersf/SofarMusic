package com.sf.sofarmusic.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sf.base.LazyLoadBaseFragment;
import com.sf.demo.DemoListFragment;
import com.sf.sofarmusic.R;

/**
 * Created by sufan on 17/9/29.
 */

public class PeopleFragment extends LazyLoadBaseFragment {

    private FrameLayout fragment_container;
    private DemoListFragment mFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        return view;
    }


    @Override
    protected void initData() {
        if (mFragment == null) {
            mFragment =  DemoListFragment.newInstance();
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, mFragment);
            ft.commit();
        }
    }

    @Override
    protected void initView() {
        fragment_container = mView.findViewById(R.id.fragment_container);
    }

    @Override
    protected void initEvent() {

    }

}
