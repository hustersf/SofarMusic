package com.sf.sofarmusic.local;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by sufan on 16/11/9.
 */
public class LocalFmAdapter extends FragmentPagerAdapter {

  private List<Fragment> mFmList;
  private List<String> mTitleList;

  public LocalFmAdapter(FragmentManager fm, List<Fragment> fmList, List<String> titleList) {
    super(fm);
    mFmList = fmList;
    mTitleList = titleList;
  }

  @Override
  public Fragment getItem(int position) {
    return mFmList.get(position);
  }

  @Override
  public int getCount() {
    return mFmList.size();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return mTitleList.get(position);
  }
}
