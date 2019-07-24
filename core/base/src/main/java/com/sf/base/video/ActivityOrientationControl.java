package com.sf.base.video;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.OrientationEventListener;
import com.sf.utility.LogUtil;

/**
 * 解决在手动点击了横竖屏后(自由转屏失效)，恢复自由转屏
 */
public class ActivityOrientationControl {
  private static final String TAG = "ActivityOrientationControl";
  private OrientationEventListener mOrientationEventListener;
  private int mLastOrientation = -1;

  public ActivityOrientationControl(Activity activity) {
    mOrientationEventListener = new OrientationEventListener(activity) {
      @Override
      public void onOrientationChanged(int orientation) {
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
          // 手机平放时，检测不到有效的角度
          return;
        }
        // 只检测是否有四个角度的改变
        if (orientation > 350 || orientation < 10) {
          orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        } else if (orientation > 80 && orientation < 100) {
          orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        } else if (orientation > 170 && orientation < 190) {
          orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
        } else if (orientation > 260 && orientation < 280) {
          orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
          return;
        }

        if (mLastOrientation == orientation) {
          return;
        }

        mLastOrientation = orientation;

        LogUtil.d(TAG, "orientation:" + orientation);
        switch (orientation) {
          case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            break;
          case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            break;
          default:
            break;
        }
      }
    };
  }

  /**
   * 开启监听
   */
  public void enable() {
    if (mOrientationEventListener.canDetectOrientation()) {
      mOrientationEventListener.enable();
    }
  }

  /**
   * 关闭监听
   */
  public void disable() {
    if (mOrientationEventListener.canDetectOrientation()) {
      mOrientationEventListener.disable();
    }
  }

}
