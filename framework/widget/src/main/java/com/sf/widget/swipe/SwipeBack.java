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

      mSwipeLayout.setEdgeShadow(activity.getResources().getDrawable(R.drawable.image_nav_shadow));
      mSwipeLayout.setOnSwipedListener(new SwipeLayout.OnSwipedListener() {
        @Override
        public void onSwiped() {
          activity.finish();
          if (func != null) {
            func.onSwiped();
          }
        }
      });
    }

  }

}
