package com.sf.sofarmusic.online;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.base.BaseFragment;
import com.sf.sofarmusic.view.KoinAvatarView;
import com.sf.utility.DensityUtil;
import com.sf.utility.LanguageUtil;
import com.sf.widget.dialog.SofarDialog;
import com.sf.widget.dialog.SofarDialogController;
import com.sf.widget.dialog.SofarDialogFragment;

/**
 * Created by sufan on 16/11/9.
 * 推荐，待开发
 */

public class RecommendFragment extends BaseFragment {

  private View view;
  private TextView hint_tv;

  private KoinAvatarView mKoinView;

  private SofarDialogFragment mDialog;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_recommend, container, false);
    initView();
    initData();
    return view;
  }


  private void initView() {
    // activity.show();
    hint_tv = (TextView) view.findViewById(R.id.hint_tv);
    mKoinView = view.findViewById(R.id.view_koin);
  }

  private void initData() {
    mKoinView.startAnimation();

    mDialog = new SofarDialogFragment();
    // SofarDialog dialog=new SofarDialog(getActivity());
    SofarDialogController.show((AppCompatActivity) getActivity(), mDialog);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mDialog != null) {
      mDialog.dismiss();
    }
  }
}
