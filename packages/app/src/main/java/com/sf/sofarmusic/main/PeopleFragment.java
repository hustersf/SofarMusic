package com.sf.sofarmusic.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.adapter.DemoListAdapter;
import com.sf.base.LazyLoadBaseFragment;
import com.sf.sofarmusic.demo.list.ListIndexActivity;
import com.sf.sofarmusic.demo.md.MDShowActivity;
import com.sf.sofarmusic.demo.media.MediaShowActivity;
import com.sf.sofarmusic.demo.picker.PickerShowActivity;
import com.sf.sofarmusic.demo.system.SystemShowActivity;
import com.sf.sofarmusic.demo.view.show.ViewShowActivity;
import com.sf.sofarmusic.demo.viewpager.VPShowActivity;
import com.sf.sofarmusic.demo.window.WindowShowActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sufan on 17/9/29.
 */

public class PeopleFragment extends LazyLoadBaseFragment implements DemoListAdapter.OnItemClickListener{

    private View view;

    private RecyclerView demo_rv;
    private DemoListAdapter mAdapter;
    private List<String> mList;
    private String[] datas={"音视频学习","picker选择器","自定义View集合","列表样式集合","弹窗效果集合",
            "ViewPager效果集合","MD风格页面效果","系统相关"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_people, container, false);
        return view;
    }


    @Override
    protected void initData() {
        mList= Arrays.asList(datas);
        mAdapter=new DemoListAdapter(activity,mList);
        demo_rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initView() {
        demo_rv=(RecyclerView)view.findViewById(R.id.demo_rv);
        demo_rv.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onItemClick(int position, String name) {
        if ("picker选择器".equals(name)) {
            Intent picker = new Intent(activity, PickerShowActivity.class);
            startActivity(picker);
        } else if ("自定义View集合".equals(name)) {
            Intent view = new Intent(activity, ViewShowActivity.class);
            startActivity(view);
        } else if ("列表样式集合".equals(name)) {
            Intent listIndex = new Intent(activity, ListIndexActivity.class);
            startActivity(listIndex);
        } else if ("弹窗效果集合".equals(name)) {
            Intent window = new Intent(activity, WindowShowActivity.class);
            startActivity(window);
        } else if ("ViewPager效果集合".equals(name)) {
            Intent vp = new Intent(activity, VPShowActivity.class);
            startActivity(vp);
        } else if ("MD风格页面效果".equals(name)) {
            Intent md = new Intent(activity, MDShowActivity.class);
            startActivity(md);
        }else if ("系统相关".equals(name)) {
            Intent system = new Intent(activity, SystemShowActivity.class);
            startActivity(system);
        }else if ("音视频学习".equals(name)) {
            Intent media = new Intent(activity, MediaShowActivity.class);
            startActivity(media);
        }
    }
}
