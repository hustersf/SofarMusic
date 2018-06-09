package com.sf.sofarmusic.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.util.ViewUtil;



/**
 *  利用SwipeLayout，在收到滑动回调，退出Activity，即左/右滑退出
*/
public class SwipeBack {

  public static SwipeLayout attach(Activity activity, SwipebackFunction func) {
    SwipeLayout layout = ViewUtil.inflate(activity, R.layout.layout_swip);
    new Helper(layout).attachSwipeBack(activity, func);
    return layout;
  }

  public static SwipeLayout attach(Activity activity) {
    SwipeLayout layout = ViewUtil.inflate(activity, R.layout.layout_swip);
    new Helper(layout).attachSwipeBack(activity, null);
    return layout;
  }

  static class Helper {

    final SwipeLayout mSwipeLayout;

    Helper(SwipeLayout swipeLayout) {
      mSwipeLayout = swipeLayout;
    }

    void attachSwipeBack(final Activity activity, final SwipebackFunction func) {
      mSwipeLayout.setOnSwipedListener(new SwipeLayout.OnSwipedListener() {
        @Override
        public void onSwiped() {
          if (func != null) {
            func.finish();
          } else {
            activity.finish();
          }
        }
      });
      if (activity == null || activity.getWindow() == null
          || !(activity.getWindow().getDecorView() instanceof ViewGroup)) {
        return;
      }

      ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
      if (decor != null) {
        if (decor.getChildCount() > 0) {
          View decorChild = decor.getChildAt(0);
          decor.removeView(decorChild);
          mSwipeLayout.addView(decorChild);
        }
        decor.addView(mSwipeLayout);
      }
    }

  }

  public interface SwipebackFunction {
    void finish();
  }

}
