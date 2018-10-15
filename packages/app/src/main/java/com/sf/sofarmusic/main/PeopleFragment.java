package com.sf.sofarmusic.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sf.base.BaseActivity;
import com.sf.demo.DemoListFragment;
import com.sf.demo.md.TypeFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.adapter.DemoListAdapter;
import com.sf.base.LazyLoadBaseFragment;
import com.sf.demo.list.ListIndexActivity;
import com.sf.demo.md.MDShowActivity;
import com.sf.demo.media.MediaShowActivity;
import com.sf.demo.picker.PickerShowActivity;
import com.sf.demo.system.SystemShowActivity;
import com.sf.demo.view.show.ViewShowActivity;
import com.sf.demo.viewpager.VPShowActivity;
import com.sf.demo.window.WindowShowActivity;

import java.util.Arrays;
import java.util.List;

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
