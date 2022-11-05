package com.sf.sofarmusic.online;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.sf.base.BaseFragment;
import com.sf.sofarmusic.R;

/**
 * Created by sufan on 16/11/9.
 * 视频，待开发
 */

public class VideoFragment extends BaseFragment {

  private RecyclerView video_rv;
  private ProgressBar progressBar;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_video, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    video_rv = (RecyclerView) view.findViewById(R.id.video_rv);
    video_rv.setLayoutManager(new LinearLayoutManager(activity));
    progressBar = view.findViewById(R.id.ad_pb);

    initEvent();

  }

  protected void initEvent() {
    new Handler().postDelayed(()->{
      progressBar.setActivated(true);
    },2000);

    new Handler().postDelayed(()->{
      progressBar.setSelected(true);
    },4000);
  }
}
