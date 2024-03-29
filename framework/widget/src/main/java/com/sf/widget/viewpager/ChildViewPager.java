package com.sf.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * 禁止父View拦截子控件的滑动事件
 */
public class ChildViewPager extends ViewPager {

  private int mDownX;
  private int mDownY;

  private int orientation = OrientationHelper.HORIZONTAL;

  /**
   * 拦截滚动的方向
   */
  public void setOrientation(@RecyclerView.Orientation int orientation) {
    this.orientation = orientation;
  }


  public ChildViewPager(@NonNull Context context) {
    super(context);
  }

  public ChildViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        int dx = Math.abs((int) (ev.getX() - mDownX));
        int dy = Math.abs((int) (ev.getY() - mDownY));
        if (orientation == OrientationHelper.HORIZONTAL) {
          getParent().requestDisallowInterceptTouchEvent(dx > dy);
        } else {
          getParent().requestDisallowInterceptTouchEvent(dy > dx);
        }
        break;
    }
    return super.onInterceptTouchEvent(ev);
  }

}
