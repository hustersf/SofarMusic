package com.sf.widget.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter{

  private List<Fragment> fragments;

  public FragmentAdapter(FragmentManager fm) {
    this(fm, new ArrayList<>());
  }

  public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
    super(fm);
    this.fragments = fragments;
  }

  public void setFragments(List<Fragment> list) {
    if (fragments == null) {
      fragments = new ArrayList<>();
    }
    fragments.clear();
    fragments.addAll(list);
  }

  @Override
  public Fragment getItem(int position) {
    return fragments.get(position);
  }

  @Override
  public int getCount() {
    return fragments.size();
  }
}
