package com.sf.sofarmusic.online;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sf.base.BaseFragment;
import com.sf.sofarmusic.R;

/**
 * Created by sufan on 16/11/9.
 * 视频，待开发
 */

public class VideoFragment extends BaseFragment {

  private RecyclerView video_rv;

  private TextView tv_error;

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

    tv_error = (TextView) view.findViewById(R.id.tv_error);
    dynamicAddView(tv_error, "textColor", R.color.main_text_color);
    initEvent();

  }

  protected void initEvent() {
    tv_error.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        tv_error.setVisibility(View.GONE);
      }
    });
  }
}
