package com.sf.sofarmusic.search;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import com.sf.base.viewpager.TabFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.rank.RankFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * 点击搜索键之后的分类结果
 */
public class SearchCategoryTabFragment extends TabFragment {

  private String[] titles = {"热门单曲", "全部专辑", "MV"};

  private RankFragment rankFragment;

  @Override
  protected int getLayoutResId() {
    return R.layout.artist_person_fragment;
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
    rankFragment = new RankFragment();

    List<Fragment> fragments = new ArrayList<>();
    fragments.add(rankFragment);
    return fragments;
  }
}
