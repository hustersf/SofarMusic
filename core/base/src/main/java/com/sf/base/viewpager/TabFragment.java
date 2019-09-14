package com.sf.base.viewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sf.base.BaseFragment;
import com.sf.base.R;
import com.sf.utility.CollectionUtil;
import com.sf.widget.viewpager.FragmentAdapter;
import com.sf.widget.viewpager.FragmentStateAdapter;
import java.util.List;

public abstract class TabFragment extends BaseFragment {

  private static final int FRAGMENT_THRESHOLD_COUNT = 4;

  protected TabLayout tabLayout;
  protected ViewPager viewPager;
  protected PagerAdapter adapter;
  protected int currentFragmentIndex;

  private List<Fragment> fragments;

  /**
   * 参照 R.layout.base_recycler_fragment
   */
  protected abstract int getLayoutResId();

  protected PagerAdapter onCreateFragmentAdapter() {
    if (fragments != null && fragments.size() <= FRAGMENT_THRESHOLD_COUNT) {
      return new FragmentAdapter(this.getChildFragmentManager());
    } else {
      return new FragmentStateAdapter(this.getChildFragmentManager());
    }
  }

  protected abstract List<TabLayout.Tab> getTabs();

  protected abstract List<Fragment> getTabFragments();

  protected int getCurrentFragmentIndex() {
    return 0;
  }

  protected int getOffscreenPageLimit() {
    return 1;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(getLayoutResId(), container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    tabLayout = view.findViewById(R.id.tabs);
    viewPager = view.findViewById(R.id.view_pager);

    fragments = getTabFragments();
    adapter = onCreateFragmentAdapter();
    viewPager.setAdapter(adapter);
    viewPager.setOffscreenPageLimit(getOffscreenPageLimit());

    if (!CollectionUtil.isEmpty(fragments)) {
      if (adapter instanceof FragmentAdapter) {
        ((FragmentAdapter) adapter).setFragments(fragments);
      } else if (adapter instanceof FragmentStateAdapter) {
        ((FragmentStateAdapter) adapter).setFragments(fragments);
      } else {
        return;
      }
      adapter.notifyDataSetChanged();
      currentFragmentIndex = getCurrentFragmentIndex();
      viewPager.setCurrentItem(currentFragmentIndex, false);
    }

    if (tabLayout != null) {
      tabLayout.setupWithViewPager(viewPager);
      // addTab的操作放在setupWithViewPager之后，因为setupWithViewPager会执行removeAllTabs操作
      tabLayout.removeAllTabs();
      List<TabLayout.Tab> tabs = getTabs();
      if (!CollectionUtil.isEmpty(tabs)) {
        for (int i = 0; i < tabs.size(); i++) {
          tabLayout.addTab(tabs.get(i));
        }
      }
    }

  }

  public void updateTabs() {
    fragments = getTabFragments();
    adapter = onCreateFragmentAdapter();
    viewPager.setAdapter(adapter);
    if (!CollectionUtil.isEmpty(fragments)) {
      if (adapter instanceof FragmentAdapter) {
        ((FragmentAdapter) adapter).setFragments(fragments);
      } else if (adapter instanceof FragmentStateAdapter) {
        ((FragmentStateAdapter) adapter).setFragments(fragments);
      } else {
        return;
      }
      adapter.notifyDataSetChanged();
    }

    if (tabLayout != null) {
      tabLayout.removeAllTabs();
      List<TabLayout.Tab> tabs = getTabs();
      if (!CollectionUtil.isEmpty(tabs)) {
        for (int i = 0; i < tabs.size(); i++) {
          tabLayout.addTab(tabs.get(i));
        }
      }
      tabLayout.getTabAt(viewPager.getCurrentItem()).select();
    }
  }

  public TabLayout getTabLayout() {
    return tabLayout;
  }

  public ViewPager getViewPager() {
    return viewPager;
  }

  public View getContentView() {
    return getView();
  }

}
