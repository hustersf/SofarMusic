package com.sf.sofarmusic.menu.profile;

import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

public class PullToZoomCoordinatorLayout extends CoordinatorLayout {
  private static final float FRICTION = 3.0f;

  private int mTouchSlop;
  private boolean mIsBeingDragged = false;
  private float mLastMotionY;
  private float mLastMotionX;
  private float mInitialMotionY;
  private boolean isZooming = false;

  private View mZoomView;
  private int mZoomViewHeight;
  private int mZoomViewMaxHeight;
  private ScalingRunnable mScalingRunnable;

  private IPullZoom mPullZoom;

  private OnPullZoomListener onPullZoomListener;

  // private static final Interpolator sInterpolator = paramAnonymousFloat -> {
  // float f = paramAnonymousFloat - 1.0F;
  // return 1.0F + f * (f * (f * (f * f)));
  // };

  // private static final Interpolator sInterpolator=new SpringScaleInterpolator(0.6f);
  private static final Interpolator sInterpolator = new Interpolator() {
    @Override
    public float getInterpolation(float input) {
      float f = input - 1.0F;
      return 1.0F + f * (f * (f * (f * f)));
    }
  };

  public PullToZoomCoordinatorLayout(Context context) {
    super(context);
    init(context);
  }

  public PullToZoomCoordinatorLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public PullToZoomCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    ViewConfiguration config = ViewConfiguration.get(context);
    mTouchSlop = config.getScaledTouchSlop();
    mScalingRunnable = new ScalingRunnable();
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    if (!isPullToZoomEnabled()) {
      return super.onInterceptTouchEvent(event);
    }

    final int action = event.getAction();

    if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
      mIsBeingDragged = false;
      return super.onInterceptTouchEvent(event);
    }

    if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
      return true;
    }
    switch (action) {
      case MotionEvent.ACTION_MOVE: {
        if (isReadyForPullStart()) {
          final float y = event.getY(), x = event.getX();
          final float diff, oppositeDiff, absDiff;

          // We need to use the correct values, based on scroll
          // direction
          diff = y - mLastMotionY;
          oppositeDiff = x - mLastMotionX;
          absDiff = Math.abs(diff);

          if (absDiff > mTouchSlop && absDiff > Math.abs(oppositeDiff)) {
            if (diff >= 1f && isReadyForPullStart()) {
              mLastMotionY = y;
              mLastMotionX = x;
              mIsBeingDragged = true;
            }
          } else {
            mIsBeingDragged = false;
          }
        }
        break;
      }
      case MotionEvent.ACTION_DOWN: {
        if (isReadyForPullStart()) {
          mLastMotionY = mInitialMotionY = event.getY();
          mLastMotionX = event.getX();
        }
        mIsBeingDragged = false;
        break;
      }
    }

    return mIsBeingDragged || super.onInterceptTouchEvent(event);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (!isPullToZoomEnabled()) {
      return super.onTouchEvent(event);
    }

    if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
      return super.onTouchEvent(event);
    }

    switch (event.getAction()) {
      case MotionEvent.ACTION_MOVE: {
        if (mIsBeingDragged) {
          mLastMotionY = event.getY();
          mLastMotionX = event.getX();
          pullEvent();
          isZooming = true;
          return true;
        }
        break;
      }

      case MotionEvent.ACTION_DOWN: {
        if (isReadyForPullStart()) {
          mLastMotionY = mInitialMotionY = event.getY();
          mLastMotionX = event.getX();
          return true;
        }
        break;
      }

      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP: {
        if (mIsBeingDragged) {
          mIsBeingDragged = false;
          // If we're already refreshing, just scroll back to the top
          if (isZooming()) {
            smoothScrollToTop();
            if (onPullZoomListener != null) {
              onPullZoomListener.onPullZoomEnd();
            }
            if (mPullZoom != null) {
              mPullZoom.onPullZoomEnd();
            }
            isZooming = false;
            return true;
          }
          return true;
        }
        break;
      }
    }
    return super.onTouchEvent(event);
  }

  public boolean isZooming() {
    return isZooming;
  }

  public boolean isPullToZoomEnabled() {
    return mZoomView != null && mPullZoom != null;
  }

  public boolean isReadyForPullStart() {
    return mPullZoom.isReadyForPullStart();
  }

  private void pullEvent() {
    final int newScrollValue;
    final float initialMotionValue, lastMotionValue;

    initialMotionValue = mInitialMotionY;
    lastMotionValue = mLastMotionY;

    newScrollValue = Math.round(Math.min(initialMotionValue - lastMotionValue, 0) / FRICTION);

    pullHeaderToZoom(newScrollValue);
    if (onPullZoomListener != null) {
      onPullZoomListener.onPullZooming(newScrollValue);
    }
    if (mPullZoom != null) {
      mPullZoom.onPullZooming(newScrollValue);
    }
  }

  /**
   * 不设置这些信息就没有zoom相关的效果
   *
   * @param zoomView
   * @param zoomViewHeight
   * @param maxZoomViewHeight 0为无穷大
   * @param pullZoom
   */
  public void setPullZoom(View zoomView, int zoomViewHeight, int maxZoomViewHeight,
      IPullZoom pullZoom) {
    this.mZoomView = zoomView;
    mZoomViewHeight = zoomViewHeight;
    mZoomViewMaxHeight = maxZoomViewHeight;
    mPullZoom = pullZoom;
    // 防止 onInterceptTouchEvent的ACTION_MOVE事件不执行，避免就是子view的down事件返回fasle
    mZoomView.setClickable(true);
  }

  public void setOnPullZoomListener(OnPullZoomListener onPullZoomListener) {
    this.onPullZoomListener = onPullZoomListener;
  }

  private void pullHeaderToZoom(int newScrollValue) {
    if (mScalingRunnable != null && !mScalingRunnable.isFinished()) {
      mScalingRunnable.abortAnimation();
    }

    ViewGroup.LayoutParams localLayoutParams = mZoomView.getLayoutParams();
    int toHeight = Math.abs(newScrollValue) + mZoomViewHeight;
    if (mZoomViewMaxHeight > 0 && toHeight > mZoomViewMaxHeight) {
      toHeight = mZoomViewMaxHeight;
    }
    localLayoutParams.height = toHeight;
    mZoomView.setLayoutParams(localLayoutParams);
  }

  private void smoothScrollToTop() {
    mScalingRunnable.startAnimation(100L);
  }

  public interface OnPullZoomListener {
    void onPullZooming(int newScrollValue);

    void onPullZoomEnd();
  }

  class ScalingRunnable implements Runnable {
    protected long mDuration;
    protected boolean mIsFinished = true;
    protected float mScale;
    protected long mStartTime;

    ScalingRunnable() {}

    public void abortAnimation() {
      mIsFinished = true;
    }

    public boolean isFinished() {
      return mIsFinished;
    }

    public void run() {
      if (mZoomView != null) {
        float f2;
        ViewGroup.LayoutParams localLayoutParams;
        if ((!mIsFinished) && (mScale > 1.0D)) {
          float f1 = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime)
              / (float) mDuration;
          f2 = mScale
              - (mScale - 1.0F) * PullToZoomCoordinatorLayout.sInterpolator.getInterpolation(f1);
          localLayoutParams = mZoomView.getLayoutParams();
          if (f2 > 1.0F) {
            localLayoutParams.height = ((int) (f2 * mZoomViewHeight));
            mZoomView.setLayoutParams(localLayoutParams);
            post(this);
            return;
          }
          mIsFinished = true;
        }
      }
    }

    public void startAnimation(long paramLong) {
      if (mZoomView != null) {
        mStartTime = SystemClock.currentThreadTimeMillis();
        mDuration = paramLong;
        mScale = ((float) (mZoomView.getBottom()) / mZoomViewHeight);
        mIsFinished = false;
        post(this);
      }
    }
  }
}
