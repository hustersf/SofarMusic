package com.sf.widget.swipe;

import android.view.MotionEvent;

/**
 * 事件拦截接口
 */
public interface OnInterceptSwipedListener {
  /**
   * 是否拦截滑动事件
   */
  boolean onInterceptSliding(boolean fromEdge, SwipeType type, MotionEvent event);

  /**
   * 是否拦截右滑进入下一页
   */
  boolean onInterceptRightSwiped();

  /**
   * 是否拦截左滑返回
   */
  boolean onInterceptLeftSwiped();
}
