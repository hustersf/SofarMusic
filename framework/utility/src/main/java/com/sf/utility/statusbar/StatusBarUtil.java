package com.sf.utility.statusbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 设置状态栏的颜色，依赖了SystemBarTintManager类
 */
public class StatusBarUtil {

  private Activity activity;


  public StatusBarUtil(Activity activity) {
    this.activity = activity;
  }

  /**
   * 设置状态栏透明
   */
  public void setStatusBarTransparent() {
    setStatusBarColor(Color.TRANSPARENT);
  }

  /**
   * 设置状态栏的颜色
   * 
   * @param color
   */
  public void setStatusBarColor(int color) {
    // 4.4-5.0
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      setTranslucentStatus(true);
      SystemBarTintManager tintManager = new SystemBarTintManager(activity);
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setStatusBarTintResource(color);
      tintManager.setStatusBarTintColor(color);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0 全透明实现
      Window window = activity.getWindow();
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.getDecorView().setSystemUiVisibility(
          View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.setStatusBarColor(color);
    }
  }


  @TargetApi(19)
  private void setTranslucentStatus(boolean on) {

    WindowManager.LayoutParams winParams = activity.getWindow().getAttributes();
    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    if (on) {
      winParams.flags |= bits;
    } else {
      winParams.flags &= ~bits;
    }
    activity.getWindow().setAttributes(winParams);
  }


  public static int getStatusBarHeight(Context context) {
    // 获得状态栏高度
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    return context.getResources().getDimensionPixelSize(resourceId);
  }

  /**
   * 状态栏和底部导航栏透明
   */
  public static void setNavigationBarStatusBarTranslucent(Activity activity) {
    if (Build.VERSION.SDK_INT >= 21) {
      View decorView = activity.getWindow().getDecorView();
      int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
      decorView.setSystemUiVisibility(option);
      activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
      activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
  }
}
