package com.sf.sofarmusic.widget.swipe;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sf.utility.DeviceUtil;
import com.sf.utility.ViewUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *为页面加了一个滑动的监听，目前支持左右,且一次只能选择一个方向可以扩展上下方向
 *可以扩展上下两个方向，并且同时支持多个方向
 * 用法，自定义一个帮助类，将该布局加到
 */

@SuppressWarnings("deprecation")
public class SwipeLayout extends FrameLayout {

  public static final String DIRECTION_LEFT = "left";
  public static final String DIRECTION_RIGHT = "right";

  private static final float MAX_SWIPE_DISTANCE_FACTOR = .2f;
  private static final int INVALID_POINTER = -1;

  private int mSwipeTriggerDistance = 40;
  private View mTarget;
  private float mEdgeSlop;
  private int mScreenWidth;
  private float mDistanceToTriggerSync = -1;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private boolean mIsBeingDragged;
  private int mActivePointerId = INVALID_POINTER;
  private OnSwipedListener mListener;
  private List<View> mIgnoreViews = new ArrayList<>();
  private Direction mDirection; // 滑动方向
  private boolean mFromEdge; // 是否要求从屏幕边缘滑动

  public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initialize();
  }

  public SwipeLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SwipeLayout(Context context) {
    this(context, null, 0);
  }

  private void initialize() {
    mEdgeSlop = ViewUtil.getEdgeSlop(getContext());
    mScreenWidth = DeviceUtil.getMetricsWidth(getContext());
    mDirection=Direction.RIGHT;
    setWillNotDraw(true);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    try {
      return super.dispatchTouchEvent(ev);
    } catch (NullPointerException e) {
      e.printStackTrace();
      // https://bugly.qq.com/v2/crash-reporting/crashes/900014602/165342?pid=1
      return true;
    }
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    ensureTarget();

    final int action = MotionEventCompat.getActionMasked(ev);

    if (!isEnabled() || mListener == null || canChildScrollHorizontal(ev) || isInIgnoreArea(ev)) {
      // Fail fast if we're not in a state where a swipe is possible
      return false;
    }

    switch (action) {
      case MotionEvent.ACTION_DOWN:
        mInitialMotionX = ev.getX();
        mInitialMotionY = ev.getY();
        mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
        mIsBeingDragged = false;
        break;

      case MotionEvent.ACTION_MOVE:
        if (mActivePointerId == INVALID_POINTER) {
          return false;
        }

        final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
        if (pointerIndex < 0) {
          return false;
        }
        if (mFromEdge) {
          if (mDirection == Direction.RIGHT && mInitialMotionX > mEdgeSlop) {
            return false;
          } else if (mDirection == Direction.LEFT && mInitialMotionX < mScreenWidth - mEdgeSlop) {
            return false;
          }
        }

        final float x = MotionEventCompat.getX(ev, pointerIndex);
        final float y = MotionEventCompat.getY(ev, pointerIndex);
        final float xDiff = x - mInitialMotionX;
        // y轴不区分方向
        final float yDiff = Math.abs(y - mInitialMotionY);
        mIsBeingDragged = computeBeingDragged(xDiff, yDiff);

        break;

      case MotionEventCompat.ACTION_POINTER_UP:
        onSecondaryPointerUp(ev);
        break;

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        mIsBeingDragged = false;
        mActivePointerId = INVALID_POINTER;
        break;
      case MotionEvent.ACTION_POINTER_DOWN:
        break;
      default:
        break;
    }

    return mIsBeingDragged;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    final int action = MotionEventCompat.getActionMasked(ev);

    if (!isEnabled() || canChildScrollHorizontal(ev) || isInIgnoreArea(ev)) {
      // Fail fast if we're not in a state where a swipe is possible
      return false;
    }

    switch (action) {
      case MotionEvent.ACTION_DOWN:

        mInitialMotionX = ev.getX();
        mInitialMotionY = ev.getY();

        mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
        mIsBeingDragged = false;
        break;

      case MotionEvent.ACTION_MOVE:
        if (mActivePointerId == INVALID_POINTER) {
          return false;
        }

        final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
        if (pointerIndex < 0) {
          return false;
        }

        if (mFromEdge) {
          if (mDirection == Direction.RIGHT && mInitialMotionX > mEdgeSlop) {
            return false;
          } else if (mDirection == Direction.LEFT && mInitialMotionX < mScreenWidth - mEdgeSlop) {
            return false;
          }
        }

        final float x = MotionEventCompat.getX(ev, pointerIndex);
        final float y = MotionEventCompat.getY(ev, pointerIndex);

        final float xDiff = x - mInitialMotionX;
        // y轴不区分方向
        final float yDiff = Math.abs(y - mInitialMotionY);

        if (!mIsBeingDragged) {
          mIsBeingDragged = computeBeingDragged(xDiff, yDiff);
        }

        if (mIsBeingDragged) {
          // User velocity passed min velocity; trigger a swipe
          swipe();
          mIsBeingDragged = false;
          mActivePointerId = INVALID_POINTER;
        }
        break;

      case MotionEventCompat.ACTION_POINTER_DOWN: {
        final int index = MotionEventCompat.getActionIndex(ev);
        mActivePointerId = MotionEventCompat.getPointerId(ev, index);
        break;
      }

      case MotionEventCompat.ACTION_POINTER_UP:
        onSecondaryPointerUp(ev);
        break;

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        mIsBeingDragged = false;
        mActivePointerId = INVALID_POINTER;
        return false;
    }

    return true;
  }

  public void setDirection(Direction direction) {
    mDirection = direction;
  }

  public void setFromEdge(boolean fromEdge) {
    mFromEdge = fromEdge;
  }

  public Direction getDirection() {
    return mDirection;
  }

  /**
   * Set the listener to be notified when a swipe-left is triggered via the swipe gesture.
   */
  public void setOnSwipedListener(OnSwipedListener listener) {
    mListener = listener;
  }

  public void addIgnoreView(View view) {
    mIgnoreViews.add(view);
  }

  public void removeIgnoreView(View view) {
    mIgnoreViews.remove(view);
  }

  public void clearIgnoreViews() {
    mIgnoreViews.clear();
  }

  private void ensureTarget() {
    // Don't bother getting the parent width if the parent hasn't been laid out yet.
    if (mTarget == null) {
      if (getChildCount() > 1 && !isInEditMode()) {
        throw new IllegalStateException("SwipeLayout can host only one direct child");
      }
      mTarget = getChildAt(0);
    }
    if (mDistanceToTriggerSync == -1) {
      if (getParent() != null && ((View) getParent()).getWidth() > 0) {
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mDistanceToTriggerSync =
            (int) Math.min(((View) getParent()).getWidth() * MAX_SWIPE_DISTANCE_FACTOR,
                mSwipeTriggerDistance * metrics.density);
      }
    }
  }

  private boolean canChildScrollHorizontal(MotionEvent ev) {
    // RIGHT, 指的是从左向右滑动
    int dx = mDirection == Direction.RIGHT ? -1 : 0;
    return canChildScrollHorizontally(mTarget, dx, (int) ev.getX(), (int) ev.getY());
  }

  protected boolean canChildScrollHorizontally(View v, int dx, int x, int y) {
    if (v instanceof ViewGroup) {
      final ViewGroup group = (ViewGroup) v;

      final int count = group.getChildCount();
      // Count backwards - let topmost views consume scroll distance first.
      for (int i = count - 1; i >= 0; i--) {
        final View child = group.getChildAt(i);

        final int childLeft = child.getLeft() + (int) ViewCompat.getTranslationX(child);
        final int childRight = child.getRight() + (int) ViewCompat.getTranslationX(child);
        final int childTop = child.getTop() + (int) ViewCompat.getTranslationY(child);
        final int childBottom = child.getBottom() + (int) ViewCompat.getTranslationY(child);

        if (x >= childLeft && x < childRight && y >= childTop && y < childBottom
            && canChildScrollHorizontally(child, dx, x - childLeft, y - childTop)) {
          return true;
        }
      }
    }
    // ViewPager做特殊处理
    if (v instanceof ViewPager) {
      int currentPosition = ((ViewPager) v).getCurrentItem();
      return currentPosition != 0 || dx > 0;
    } else {
      return ViewCompat.canScrollHorizontally(v, dx);
    }
  }

  private void onSecondaryPointerUp(MotionEvent ev) {
    final int pointerIndex = MotionEventCompat.getActionIndex(ev);
    final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
    if (pointerId == mActivePointerId) {
      // This was our active pointer going up. Choose a new
      // active pointer and adjust accordingly.
      final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
      mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
    }
  }


  private boolean computeBeingDragged(float xDiff, float yDiff) {
    // 向右滑动
    if (mDirection == Direction.RIGHT && xDiff > 0) {
      float absXDiff = Math.abs(xDiff);
      return absXDiff > mDistanceToTriggerSync && yDiff * 2 < absXDiff;
    }
    // 向左滑动
    if (mDirection == Direction.LEFT && xDiff < 0) {
      float absXDiff = Math.abs(xDiff);
      return absXDiff > mDistanceToTriggerSync && yDiff * 2 < absXDiff;
    }

    return false;
  }

  // add by liuwei. 长图滑动太灵敏，需要调整滑动最小距离
  public void setSwipeTriggerDistance(int newDistance) {
    mSwipeTriggerDistance = newDistance;
    if (mDistanceToTriggerSync > 0) {
      if (getParent() != null && ((View) getParent()).getWidth() > 0) {
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mDistanceToTriggerSync =
            (int) Math.min(((View) getParent()).getWidth() * MAX_SWIPE_DISTANCE_FACTOR,
                mSwipeTriggerDistance * metrics.density);
      }
    }
  }


  private boolean isInIgnoreArea(MotionEvent event) {
    Rect rect = new Rect();
    for (View view : mIgnoreViews) {
      view.getGlobalVisibleRect(rect);
      if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
        return true;
      }
    }
    return false;
  }

  private void swipe() {
    OnSwipedListener l = mListener;
    if (l != null) {
      l.onSwiped();
    }
  }

  public interface OnSwipedListener {
    void onSwiped();
  }

  public enum Direction {
    LEFT,
    RIGHT
  }
}
