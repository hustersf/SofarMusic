package com.sf.widget.viewpager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentStateAdapter extends FragmentStatePagerAdapter {

  private List<Fragment> fragments;

  public FragmentStateAdapter(FragmentManager fm) {
    this(fm, new ArrayList<>());
  }

  public FragmentStateAdapter(FragmentManager fm, List<Fragment> fragments) {
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
