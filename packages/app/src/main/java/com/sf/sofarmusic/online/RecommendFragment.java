package com.sf.sofarmusic.online;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.base.BaseFragment;
import com.sf.sofarmusic.job.JobManager;
import com.sf.sofarmusic.view.KoinAvatarView;


/**
 * Created by sufan on 16/11/9.
 * 推荐，待开发
 */

public class RecommendFragment extends BaseFragment {

  private View view;
  private TextView hint_tv;

  private KoinAvatarView mKoinView;


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
    hint_tv = (TextView) view.findViewById(R.id.hint_tv);
    mKoinView = view.findViewById(R.id.view_koin);
  }

  private void initData() {
    mKoinView.startAnimation();

    JobManager.getInstance(getActivity()).startJob(JobManager.JobType.JOB_DISPATCHER);
  }

}
