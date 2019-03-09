package com.sf.widget.swipe;

import android.view.MotionEvent;

public interface BehaviorTouchListener {
  boolean onInterceptTouchEvent(MotionEvent ev);

  boolean onTouchEvent(MotionEvent ev);
}
