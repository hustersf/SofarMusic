package com.sf.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;

/**
 * {@link androidx.swiperefreshlayout.widget.SwipeRefreshLayout}
 */
public abstract class RefreshLayout extends ViewGroup
  implements NestedScrollingParent, NestedScrollingChild {
  private static final String LOG_TAG = "RefreshLayout";

  private static final int INVALID_POINTER = -1;
  private static final float DRAG_RATE = .5f;

  private boolean mRefreshing = false;
  private View mTarget;
  private View mRefreshView;
  private RefreshStatus mRefreshStatus;
  private OnRefreshListener mListener;
  private int mRefreshViewIndex = -1;
  private @MODE int mMode = MODE.NORMAL;

  // MotionEvent
  private int mTouchSlop;
  private float mInitialMotionY;
  private float mInitialDownY;
  private boolean mIsBeingDragged;
  private int mActivePointerId = INVALID_POINTER;

  // RefreshView和Target的滑动距离相关
  private float mTotalDragDistance = -1; // 最大能够下滑的距离
  private int mOriginOffsetY; // 初始状态时，RefreshView的偏移量
  private int mCurrentRefreshViewOffsetY; // 下滑过程中的偏移量

  // 动画相关
  private int mMediumAnimationDuration;
  private final DecelerateInterpolator mDecelerateInterpolator;
  private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
  private int mFrom;

  private final Animation mAnimateToStartPosition = new Animation() {
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
      moveToStart(interpolatedTime);
    }
  };

  private final Animation mAnimateToRefreshPosition = new Animation() {
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
      int endTarget = (int) (mTotalDragDistance - Math.abs(mOriginOffsetY));
      int targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
      int offset = targetTop - mRefreshView.getTop();
      setTargetAndRefreshViewOffsetY(offset);

    }
  };

  private Animation.AnimationListener mRefreshListener = new Animation.AnimationListener() {
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
      if (mRefreshing) {
        if (mListener != null) {
          mListener.onRefresh();
        }
        mCurrentRefreshViewOffsetY = mRefreshView.getTop();
      } else {
        reset();
      }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
  };


  public RefreshLayout(Context context) {
    this(context, null);
  }

  public RefreshLayout(Context context, AttributeSet attrs) {
    super(context, attrs);

    mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    mMediumAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
    mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

    createRefreshView();
  }

  protected abstract View onCreateRefreshView();

  private void createRefreshView() {
    mRefreshView = onCreateRefreshView();
    if (mRefreshView instanceof RefreshStatus) {
      mRefreshStatus = (RefreshStatus) mRefreshView;
    }
    mRefreshView.setVisibility(GONE);
    addView(mRefreshView);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    if (mTarget == null) {
      ensureTarget();
    }

    if (mTarget == null) {
      return;
    }

    measureTarget();
    measureRefreshView(mRefreshView, widthMeasureSpec, heightMeasureSpec);

    mRefreshViewIndex = -1;
    for (int i = 0; i < getChildCount(); i++) {
      if (getChildAt(i) == mRefreshView) {
        mRefreshViewIndex = i;
        break;
      }
    }

  }

  private void measureTarget() {
    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
      getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);
    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
      getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
    mTarget.measure(widthMeasureSpec, heightMeasureSpec);

  }

  private void measureRefreshView(View view, int widthMeasureSpec, int heightMeasureSpec) {
    final MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
    final int childWidthMeasureSpec;
    if (lp.width == LayoutParams.MATCH_PARENT) {
      final int width = Math.max(0, getMeasuredWidth() - getPaddingLeft() - getPaddingRight()
        - lp.leftMargin - lp.rightMargin);
      childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
    } else {
      childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
        getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
    }

    final int childHeightMeasureSpec;
    if (lp.height == LayoutParams.MATCH_PARENT) {
      final int height = Math.max(0, getMeasuredHeight() - getPaddingTop() - getPaddingBottom()
        - lp.topMargin - lp.bottomMargin);
      childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
    } else {
      childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
        getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
    }

    view.measure(childWidthMeasureSpec, childHeightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int width = getMeasuredWidth();
    final int height = getMeasuredHeight();
    if (getChildCount() == 0) {
      return;
    }
    if (mTarget == null) {
      ensureTarget();
    }
    if (mTarget == null) {
      return;
    }

    int refreshViewWidth = mRefreshView.getMeasuredWidth();
    int refreshViewHeight = mRefreshView.getMeasuredHeight();

    if (mTotalDragDistance == -1) {
      mTotalDragDistance = refreshViewHeight;
      mOriginOffsetY = mCurrentRefreshViewOffsetY = -refreshViewHeight;
    }

    int refreshViewTop = mCurrentRefreshViewOffsetY;
    int refreshViewBottom = mCurrentRefreshViewOffsetY + refreshViewHeight;
    mRefreshView.layout((width / 2 - refreshViewWidth / 2), refreshViewTop,
      (width / 2 + refreshViewWidth / 2), refreshViewBottom);

    final View child = mTarget;
    final int childLeft = getPaddingLeft();
    final int childTop;
    if (mMode == MODE.NORMAL) {
      childTop = getPaddingTop() + refreshViewBottom;
    } else {
      childTop = getPaddingTop();
    }
    final int childWidth = width - getPaddingLeft() - getPaddingRight();
    final int childHeight = height - getPaddingTop() - getPaddingBottom();
    child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

    Log.d(LOG_TAG, "onLayout:" + mCurrentRefreshViewOffsetY);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    ensureTarget();

    final int action = ev.getActionMasked();
    int pointerIndex;

    if (!isEnabled() || mRefreshing) {
      return false;
    }

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mActivePointerId = ev.getPointerId(0);
        mIsBeingDragged = false;

        pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex < 0) {
          return false;
        }
        mInitialDownY = ev.getY(pointerIndex);
        break;

      case MotionEvent.ACTION_MOVE:
        if (mActivePointerId == INVALID_POINTER) {
          Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
          return false;
        }

        pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex < 0) {
          return false;
        }
        final float y = ev.getY(pointerIndex);
        startDragging(y);
        break;

      case MotionEvent.ACTION_POINTER_UP:
        onSecondaryPointerUp(ev);
        break;

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        mIsBeingDragged = false;
        mActivePointerId = INVALID_POINTER;
        break;
    }

    return mIsBeingDragged;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    final int action = ev.getActionMasked();
    int pointerIndex;

    if (!isEnabled() || mRefreshing) {
      return false;
    }

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mActivePointerId = ev.getPointerId(0);
        mIsBeingDragged = false;
        break;

      case MotionEvent.ACTION_MOVE: {
        pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex < 0) {
          Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
          return false;
        }

        final float y = ev.getY(pointerIndex);
        startDragging(y);

        final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
        if (overScrollTop > 0) {
          moveSpinner(overScrollTop);
        } else {
          return false;
        }
        break;
      }

      case MotionEvent.ACTION_POINTER_DOWN:
        pointerIndex = ev.getActionIndex();
        if (pointerIndex < 0) {
          Log.e(LOG_TAG,
            "Got ACTION_POINTER_DOWN event but have an invalid action index.");
          return false;
        }
        mActivePointerId = ev.getPointerId(pointerIndex);
        break;

      case MotionEvent.ACTION_POINTER_UP:
        onSecondaryPointerUp(ev);
        break;

      case MotionEvent.ACTION_UP: {
        pointerIndex = ev.findPointerIndex(mActivePointerId);
        if (pointerIndex < 0) {
          Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
          return false;
        }

        if (mIsBeingDragged) {
          final float y = ev.getY(pointerIndex);
          final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
          mIsBeingDragged = false;
          finishSpinner(overScrollTop);
        }
        mActivePointerId = INVALID_POINTER;
        return false;
      }

      case MotionEvent.ACTION_CANCEL:
        return false;
    }
    return true;
  }

  private void startDragging(float y) {
    final float yDiff = y - mInitialDownY;
    if (yDiff > mTouchSlop && !mIsBeingDragged) {
      mInitialMotionY = mInitialDownY + mTouchSlop;
      mIsBeingDragged = true;
      mRefreshStatus.pull();
    }
  }

  private void onSecondaryPointerUp(MotionEvent ev) {
    final int pointerIndex = ev.getActionIndex();
    final int pointerId = ev.getPointerId(pointerIndex);
    if (pointerId == mActivePointerId) {
      // This was our active pointer going up. Choose a new
      // active pointer and adjust accordingly.
      final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
      mActivePointerId = ev.getPointerId(newPointerIndex);
    }
  }

  private void moveSpinner(float overScrollTop) {
    Log.d(LOG_TAG, "moveSpinner:" + overScrollTop);
    if (mRefreshView.getVisibility() != VISIBLE) {
      mRefreshView.setVisibility(VISIBLE);
    }

    // 随着值增大，变化越来越小(开平方)
    float adjustScrollTop = (float) Math.pow(overScrollTop, 0.9f);

    setTargetAndRefreshViewOffsetY((int) (Math.min(adjustScrollTop, mTotalDragDistance)
      - (mCurrentRefreshViewOffsetY - mOriginOffsetY)));

    if (canRelease()) {
      mRefreshStatus.release();
    }
  }


  /**
   * 是否拉到了刷新的临界值
   */
  private boolean canRefresh() {
    return Math.abs(mCurrentRefreshViewOffsetY - mOriginOffsetY) > mTotalDragDistance * 0.66;
  }

  /**
   * 是否到了松开刷新的临界值
   */
  private boolean canRelease() {
    return Math.abs(mCurrentRefreshViewOffsetY - mOriginOffsetY) > mTotalDragDistance * 0.9;
  }

  private void finishSpinner(float overScrollTop) {
    Log.d(LOG_TAG, "finishSpinner:" + overScrollTop);
    if (canRefresh()) {
      setRefreshing(true);
    } else {
      animToStart();
    }
  }

  public void setRefreshing(boolean refreshing) {
    if (mRefreshing == refreshing) {
      return;
    }

    if (mRefreshView.getVisibility() != VISIBLE) {
      mRefreshView.setVisibility(VISIBLE);
    }

    mRefreshing = refreshing;
    if (mRefreshing) {
      mRefreshStatus.refreshing();
      animToRefresh();
    } else {
      mRefreshStatus.complete();
      animToStart();
    }
  }

  /**
   * 恢复至初始状态
   */
  private void reset() {
    mRefreshStatus.reset();
    mRefreshView.clearAnimation();
    mRefreshView.setVisibility(GONE);
  }

  /**
   * 滑动至刷新点
   */
  private void animToRefresh() {
    mFrom = mCurrentRefreshViewOffsetY;
    mAnimateToRefreshPosition.reset();
    mAnimateToRefreshPosition.setDuration(mMediumAnimationDuration);
    mAnimateToRefreshPosition.setInterpolator(mDecelerateInterpolator);
    mRefreshView.clearAnimation();
    mRefreshView.startAnimation(mAnimateToRefreshPosition);

    mAnimateToRefreshPosition.setAnimationListener(mRefreshListener);
  }

  /**
   * 回到起始点
   */
  private void animToStart() {
    mFrom = mCurrentRefreshViewOffsetY;
    mAnimateToStartPosition.reset();
    mAnimateToStartPosition.setDuration(mMediumAnimationDuration);
    mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
    mRefreshView.clearAnimation();
    mRefreshView.startAnimation(mAnimateToStartPosition);
  }

  void moveToStart(float interpolatedTime) {
    int targetTop = (mFrom + (int) ((mOriginOffsetY - mFrom) * interpolatedTime));
    int offset = targetTop - mRefreshView.getTop();
    setTargetAndRefreshViewOffsetY(offset);
  }

  /**
   * 上下移动RefreshView和Target
   */
  private void setTargetAndRefreshViewOffsetY(int offsetY) {
    mRefreshView.bringToFront();
    ViewCompat.offsetTopAndBottom(mRefreshView, offsetY);
    if (mMode == MODE.NORMAL) {
      ViewCompat.offsetTopAndBottom(mTarget, offsetY);
    }
    mCurrentRefreshViewOffsetY = mRefreshView.getTop();
    Log.d(LOG_TAG, "offsetY:" + offsetY + " refreshTop:" + mCurrentRefreshViewOffsetY);
  }

  private void ensureTarget() {
    if (mTarget == null) {
      for (int i = 0; i < getChildCount(); i++) {
        View child = getChildAt(i);
        if (!child.equals(mRefreshView)) {
          mTarget = child;
          break;
        }
      }
    }
  }


  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    reset();
  }

  public static class LayoutParams extends MarginLayoutParams {

    public LayoutParams(Context c, AttributeSet attrs) {
      super(c, attrs);
    }

    public LayoutParams(int width, int height) {
      super(width, height);
    }

    public LayoutParams(MarginLayoutParams source) {
      super(source);
    }

    public LayoutParams(ViewGroup.LayoutParams source) {
      super(source);
    }
  }

  @Override
  protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
    return p instanceof LayoutParams;
  }

  @Override
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
  }

  @Override
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
    return new LayoutParams(p);
  }

  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new LayoutParams(getContext(), attrs);
  }

  /**
   * Set the listener to be notified when a refresh is triggered via the swipe
   * gesture.
   */
  public void setOnRefreshListener(@Nullable OnRefreshListener listener) {
    mListener = listener;
  }

  /**
   * Classes that wish to be notified when the swipe gesture correctly
   * triggers a refresh should implement this interface.
   */
  public interface OnRefreshListener {
    /**
     * Called when a swipe gesture triggers a refresh.
     */
    void onRefresh();
  }

  public void setMode(@MODE int mode) {
    mMode = mode;
  }

  /**
   * 下拉刷新模式
   */
  public @interface MODE {
    int NORMAL = 0; // refresh和target一起滑动
    int TARGET_FIXED = 1; // target保持不变
  }
}
