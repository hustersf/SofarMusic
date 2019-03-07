package com.sf.sofarmusic.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.base.BaseFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.OnlineFmAdapter;
import com.sf.sofarmusic.online.RadioFragment;
import com.sf.sofarmusic.online.RecommendFragment;
import com.sf.sofarmusic.online.VideoFragment;
import com.sf.sofarmusic.online.rank.RankFragment;

/**
 * Created by sufan on 16/11/8.
 */

public class OnlineFragment extends BaseFragment {
  private View view;

  private TabLayout online_tl;
  private ViewPager online_vp;
  private RecommendFragment recommendFragment;
  private RankFragment rankFragment;
  private VideoFragment videoFragment;
  private RadioFragment radioFragment;
  private OnlineFmAdapter onlineFmAdapter;
  private List<Fragment> mFmList;
  private List<String> mTitleList;
  private String[] titles = {"推荐", "榜单", "视频", "电台"};

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_online, container, false);
    initView();
    initData();
    initEvent();
    return view;
  }

  private void initData() {

    // tablayout+viewpager
    recommendFragment = new RecommendFragment();
    rankFragment = new RankFragment();
    videoFragment = new VideoFragment();
    radioFragment = new RadioFragment();
    mFmList = new ArrayList<>();
    mFmList.add(recommendFragment);
    mFmList.add(rankFragment);
    mFmList.add(videoFragment);
    mFmList.add(radioFragment);
    mTitleList = Arrays.asList(titles);
    FragmentManager fm = activity.getSupportFragmentManager();
    onlineFmAdapter = new OnlineFmAdapter(fm, mFmList, mTitleList);
    online_vp.setAdapter(onlineFmAdapter);
    online_vp.setOffscreenPageLimit(4);

    online_tl.addTab(online_tl.newTab().setText(mTitleList.get(0)));
    online_tl.addTab(online_tl.newTab().setText(mTitleList.get(1)));
    online_tl.addTab(online_tl.newTab().setText(mTitleList.get(2)));
    online_tl.addTab(online_tl.newTab().setText(mTitleList.get(3)));
    online_tl.setupWithViewPager(online_vp); // 是的tab随着viewpager动

  }

  private void initView() {
    online_vp = (ViewPager) view.findViewById(R.id.online_vp);
    online_tl = (TabLayout) view.findViewById(R.id.online_tl);

    dynamicAddView(online_tl, "tabLayoutIndicator", R.color.themeColor);
    dynamicAddView(online_tl, "tabLayoutTextColor", R.color.main_text_color);
  }

  private void initEvent() {}
}
