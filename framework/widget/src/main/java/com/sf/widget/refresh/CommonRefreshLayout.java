package com.sf.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.sf.utility.DeviceUtil;

/**
 * 通用的下拉刷新控件
 */
public class CommonRefreshLayout extends RefreshLayout {

  public CommonRefreshLayout(Context context) {
    super(context);
  }

  public CommonRefreshLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected View onCreateRefreshView() {
    View refreshView = new CommonRefreshView(getContext());
    int padding = DeviceUtil.getStatusBarHeight(getContext());
    refreshView.setPadding(0, 2 * padding, 0, padding);
    return refreshView;
  }
}
