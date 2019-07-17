package com.sf.sofarmusic.main;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import com.sf.base.viewpager.TabFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.artist.ArtistGroupFragment;
import com.sf.sofarmusic.online.recommend.RecommendFragment;
import com.sf.sofarmusic.online.VideoFragment;
import com.sf.sofarmusic.online.rank.RankFragment;

/**
 * Created by sufan on 16/11/8.
 */
public class OnlineFragment extends TabFragment {

  private RecommendFragment recommendFragment;
  private RankFragment rankFragment;
  private VideoFragment videoFragment;
  private ArtistGroupFragment artistGroupFragment;
  private String[] titles = {"推荐", "榜单", "歌手", "视频"};

  @Override
  protected int getLayoutResId() {
    return R.layout.fragment_online;
  }

  @Override
  protected List<TabLayout.Tab> getTabs() {
    List<TabLayout.Tab> tabs = new ArrayList<>();
    for (int i = 0; i < titles.length; i++) {
      TabLayout.Tab tab = tabLayout.newTab().setText(titles[i]);
      tabs.add(tab);
    }
    return tabs;
  }

  @Override
  protected List<Fragment> getTabFragments() {
    List<Fragment> fragments = new ArrayList<>();
    recommendFragment = new RecommendFragment();
    rankFragment = new RankFragment();
    artistGroupFragment = new ArtistGroupFragment();
    videoFragment = new VideoFragment();

    fragments.add(recommendFragment);
    fragments.add(rankFragment);
    fragments.add(artistGroupFragment);
    fragments.add(videoFragment);
    return fragments;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    dynamicAddView(tabLayout, "tabLayoutIndicator", R.color.themeColor);
    dynamicAddView(tabLayout, "tabLayoutTextColor", R.color.main_text_color);
  }

  @Override
  protected int getOffscreenPageLimit() {
    return 4;
  }
}
