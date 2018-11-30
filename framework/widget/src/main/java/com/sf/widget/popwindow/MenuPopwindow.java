package com.sf.widget.popwindow;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.utility.ViewUtil;
import com.sf.widget.R;

/**
 * 常用的popupwindow模板
 */
public class MenuPopwindow extends PopupWindow {

  private Activity mActivity;
  private View mView;

  public MenuPopwindow(Activity activity) {
    mActivity = activity;
    initView();
    initPopwinodw();

  }

  private void initView() {
    FrameLayout contentView = new FrameLayout(mActivity);
    mView = ViewUtil.inflate(mActivity, R.layout.popwindow_menu);
    int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    mView.measure(measureSpec, measureSpec);
    contentView.addView(mView, mView.getMeasuredWidth(), mView.getMeasuredHeight());
    setContentView(contentView);

    contentView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });
  }

  private void initPopwinodw() {
    setWidth(WindowManager.LayoutParams.MATCH_PARENT);
    setHeight(WindowManager.LayoutParams.MATCH_PARENT);

    setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7f000000")));

    setTouchable(true);
    setOutsideTouchable(true);
    setFocusable(true);
  }


  /**
   * 在anchor下方展示
   */
  public void showAsCoverAnchorDropDown(View anchor) {
    final int[] screenLocation = new int[2];
    anchor.getLocationOnScreen(screenLocation);

    int leftMargin = Math.min(screenLocation[0],
            DeviceUtil.getMetricsWidth(mActivity) - mView.getMeasuredWidth() - DensityUtil.dp2px(mActivity, 12));
    ViewGroup.MarginLayoutParams layoutParams =
        (ViewGroup.MarginLayoutParams) mView.getLayoutParams();
    layoutParams.leftMargin = leftMargin;
    layoutParams.topMargin = screenLocation[1]+DeviceUtil.getStatusBarHeight(mActivity);

    showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
  }

}
