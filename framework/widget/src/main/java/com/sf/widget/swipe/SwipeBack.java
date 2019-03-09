package com.sf.widget.swipe;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.sf.utility.ViewUtil;
import com.sf.widget.R;

public class SwipeBack {

  public static SwipeLayout attach(Activity activity, SwipeLayout.OnSwipedListener func) {
    SwipeLayout layout = ViewUtil.inflate(activity, R.layout.widget_swipe_layout);
    new Helper(layout).attachSwipeBack(activity, func);
    return layout;
  }

  public static SwipeLayout attach(Activity activity) {
    SwipeLayout layout = ViewUtil.inflate(activity, R.layout.widget_swipe_layout);
    new Helper(layout).attachSwipeBack(activity, null);
    return layout;
  }

  static class Helper {

    final SwipeLayout mSwipeLayout;

    Helper(SwipeLayout swipeLayout) {
      mSwipeLayout = swipeLayout;
    }

    void attachSwipeBack(final Activity activity, final SwipeLayout.OnSwipedListener func) {
      mSwipeLayout.setOnSwipedListener(new SwipeLayout.OnSwipedListenerAdapter() {
        @Override
        public void onRightSwiped() {
          if (func != null) {
            func.onRightSwiped();
          } else {
            activity.finish();
          }
        }

        @Override
        public void onLeftSwiped() {
          if (func != null) {
            func.onLeftSwiped();
          }
        }

        @Override
        public void onRightSwipedFromEdge() {
          if (func != null) {
            func.onRightSwipedFromEdge();
          }
        }

        @Override
        public void onLeftSwipedFromEdge() {
          if (func != null) {
            func.onLeftSwipedFromEdge();
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

}
