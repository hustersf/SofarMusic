package com.sf.widget.swipe;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * swipe责任链
 */
public abstract class SwipeHandler implements BehaviorTouchListener {
  SwipeHandler mSuccessor;
  boolean mHandlerTouchEnable = true; // 是否有事件处理权
  Class<?> mAttachedViewClz = SwipeLayout.class; // 把touch事件绑定在哪个view上
  private OnInterceptSwipedListener mInterceptSwipedListener;

  public void setSuccessor(SwipeHandler successor) {
    mSuccessor = successor;
  }

  public SwipeHandler getSuccessor() {
    return mSuccessor;
  }

  /**
   * Add a swipeHandler to the last of link.
   */
  public void addSuccessor(SwipeHandler handler) {
    SwipeHandler currentHandler = this;
    while (currentHandler.mSuccessor != null) {
      currentHandler = currentHandler.mSuccessor;
    }
    currentHandler.setSuccessor(handler);
  }

  public void setAttachedViewClz(Class<?> attachedViewClz) {
    mAttachedViewClz = attachedViewClz;
    if (mSuccessor != null) {
      mSuccessor.setAttachedViewClz(attachedViewClz);
    }
  }

  public void setOnInterceptSwipedListener(OnInterceptSwipedListener listener) {
    mInterceptSwipedListener = listener;
  }

  public OnInterceptSwipedListener getOnInterceptSwipedListener() {
    return mInterceptSwipedListener;
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (!mHandlerTouchEnable) {
      // 其某个Successor单独拥有了事件处理权
      if (mSuccessor != null) {
        return mSuccessor.onInterceptTouchEvent(ev);
      }
      return false;
    }

    boolean handler = handlerInterceptTouchEvent(ev);
    if (!handler) {
      if (mSuccessor != null) {
        handler = mSuccessor.onInterceptTouchEvent(ev);
        if (handler) {
          mHandlerTouchEnable = false;
        }
      }
      return false;
    }

    return true;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if (!mHandlerTouchEnable) {
      // 其某个Successor单独拥有了事件处理权
      if (mSuccessor != null) {
        return mSuccessor.onTouchEvent(ev);
      }
      return false;
    }

    boolean handler = handlerTouchEvent(ev);
    if (!handler) {
      if (mSuccessor != null) {
        handler = mSuccessor.onTouchEvent(ev);
        if (handler) {
          mHandlerTouchEnable = false;
        }
      }
      return false;
    }

    return true;
  }

  public void handlerComputeScroll() {
    if (mSuccessor != null) {
      mSuccessor.handlerComputeScroll();
    }
  }

  public void handlerDrawShadow(Canvas canvas) {
    if (mSuccessor != null) {
      mSuccessor.handlerDrawShadow(canvas);
    }
  }

  public void handlerReset() {
    mHandlerTouchEnable = true;
    if (mSuccessor != null) {
      mSuccessor.handlerReset();
    }
  }

  public boolean isTouchEnabled(Class<?> attachedViewcLZ) {
    return mAttachedViewClz == attachedViewcLZ;
  }

  protected abstract boolean handlerInterceptTouchEvent(MotionEvent ev);

  protected abstract boolean handlerTouchEvent(MotionEvent ev);

}
