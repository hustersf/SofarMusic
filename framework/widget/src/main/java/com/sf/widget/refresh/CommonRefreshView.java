package com.sf.widget.refresh;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.utility.ViewUtil;
import com.sf.widget.R;

/**
 * 通用的RefreshView
 */
public class CommonRefreshView extends LinearLayout implements RefreshStatus {

  private TextView refreshTv;

  public CommonRefreshView(Context context) {
    this(context, null);
  }

  public CommonRefreshView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CommonRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    ViewUtil.inflate(this, R.layout.layout_common_refreshview, true);
    RefreshLayout.LayoutParams lp = new RefreshLayout.LayoutParams(
        RefreshLayout.LayoutParams.MATCH_PARENT, RefreshLayout.LayoutParams.WRAP_CONTENT);
    setLayoutParams(lp);
    setGravity(Gravity.CENTER);
    setBackgroundColor(Color.parseColor("#999999"));

    refreshTv = findViewById(R.id.tv_refresh);
  }

  @Override
  public void reset() {

  }

  @Override
  public void pull() {
    refreshTv.setText("下拉刷新");
  }

  @Override
  public void pullProgress(float pullDistance, float pullProgress) {

  }

  @Override
  public void release() {
    refreshTv.setText("松开刷新");
  }

  @Override
  public void refreshing() {
    refreshTv.setText("正在刷新...");
  }

  @Override
  public void complete() {
    refreshTv.setText("刷新完成");
  }
}
