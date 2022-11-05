package com.sf.sofarmusic.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.sf.base.BaseFragment;
import com.sf.demo.DemoListFragment;
import com.sf.sofarmusic.R;

/**
 * Created by sufan on 17/9/29.
 */

public class PeopleFragment extends BaseFragment {

  private FrameLayout fragment_container;
  private DemoListFragment mFragment;


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_people, container, false);

  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    fragment_container = view.findViewById(R.id.fragment_container);
  }


  @Override
  protected void onFirstVisible() {
    super.onFirstVisible();
    if (mFragment == null) {
      mFragment = DemoListFragment.newInstance();
      FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.fragment_container, mFragment);
      ft.commit();
    }
  }
}
