package com.sf.demo;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.demo.adapter.DemoListAdapter;
import com.sf.demo.widget.WidgetActivity;
import com.sf.demo.list.ListIndexActivity;
import com.sf.demo.md.MDShowActivity;
import com.sf.demo.media.MediaShowActivity;
import com.sf.demo.performance.PerformanceActivity;
import com.sf.demo.picker.PickerShowActivity;
import com.sf.demo.system.SystemShowActivity;
import com.sf.demo.view.show.ViewShowActivity;
import com.sf.demo.viewpager.VPShowActivity;
import com.sf.demo.window.WindowShowActivity;

/**
 * Demo入口
 */
public class DemoListFragment extends Fragment implements DemoListAdapter.OnItemClickListener {

  private View view;

  private RecyclerView demo_rv;
  private DemoListAdapter mAdapter;
  private List<String> mList;
  private String[] datas = {"音视频学习", "picker选择器", "自定义View集合", "列表样式集合", "弹窗效果集合",
      "ViewPager效果集合", "MD风格页面效果", "系统相关", "性能测试", "特殊控件"};

  public static DemoListFragment newInstance() {
    DemoListFragment fragment = new DemoListFragment();
    return fragment;
  }


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_demo_list, container, false);
    initView();
    initData();
    initEvent();
    return view;
  }


  protected void initData() {
    mList = Arrays.asList(datas);
    mAdapter = new DemoListAdapter(getActivity(), mList);
    demo_rv.setAdapter(mAdapter);
    mAdapter.setOnItemClickListener(this);
  }


  protected void initView() {
    demo_rv = (RecyclerView) view.findViewById(R.id.demo_rv);
    demo_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
  }


  protected void initEvent() {

  }

  @Override
  public void onItemClick(int position, String name) {
    if ("picker选择器".equals(name)) {
      Intent picker = new Intent(getActivity(), PickerShowActivity.class);
      startActivity(picker);
    } else if ("自定义View集合".equals(name)) {
      Intent view = new Intent(getActivity(), ViewShowActivity.class);
      startActivity(view);
    } else if ("列表样式集合".equals(name)) {
      Intent listIndex = new Intent(getActivity(), ListIndexActivity.class);
      startActivity(listIndex);
    } else if ("弹窗效果集合".equals(name)) {
      Intent window = new Intent(getActivity(), WindowShowActivity.class);
      startActivity(window);
    } else if ("ViewPager效果集合".equals(name)) {
      Intent vp = new Intent(getActivity(), VPShowActivity.class);
      startActivity(vp);
    } else if ("MD风格页面效果".equals(name)) {
      Intent md = new Intent(getActivity(), MDShowActivity.class);
      startActivity(md);
    } else if ("系统相关".equals(name)) {
      Intent system = new Intent(getActivity(), SystemShowActivity.class);
      startActivity(system);
    } else if ("音视频学习".equals(name)) {
      Intent media = new Intent(getActivity(), MediaShowActivity.class);
      startActivity(media);
    } else if ("性能测试".equals(name)) {
      Intent performance = new Intent(getActivity(), PerformanceActivity.class);
      startActivity(performance);
    } else if ("特殊控件".equals(name)) {
      Intent widget = new Intent(getActivity(), WidgetActivity.class);
      startActivity(widget);
    }
  }
}
