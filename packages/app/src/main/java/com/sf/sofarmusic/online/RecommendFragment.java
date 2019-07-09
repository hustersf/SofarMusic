package com.sf.sofarmusic.online;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.base.BaseFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.view.KoinAvatarView;
import com.sf.widget.tip.TipType;
import com.sf.widget.tip.TipUtil;


/**
 * Created by sufan on 16/11/9.
 * 推荐，待开发
 */

public class RecommendFragment extends BaseFragment {

  private View view;
  private RelativeLayout mRootLayout;


  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_recommend, container, false);
    initView();
    initData();
    return view;
  }


  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void initView() {
    // activity.show();
    mRootLayout = view.findViewById(R.id.rl_root);
  }

  private void initData() {

  }

}
