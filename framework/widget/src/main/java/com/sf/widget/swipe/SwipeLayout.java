package com.sf.widget.swipe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.utility.LogUtil;

/**
 * 滑动返回布局
 */
public class SwipeLayout extends FrameLayout {

  private static final String TAG = "SwipeLayout";

  private OnSwipedListener listener;
  private ViewDragHelper dragHelper;

  private boolean enable = true; // 是否支持滑动
  private boolean fromEdge = false; // 是否只支持从边缘滑动
  private @SwipeDirection int swipeDirection = SwipeDirection.RIGHT; // 滑动方向
  private float swipeThreshold = 0.5f; // 触发滑动完成的阈值

  private Drawable shadowDrawable; // 边缘绘制的阴影
  private int shadowWidth;
  private int viewLeft;
  private int viewTop;

  private int navigationHeight; // 底部导航栏高度

  public SwipeLayout(@NonNull Context context) {
    this(context, null);
  }

  public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    dragHelper = ViewDragHelper.create(this, new ViewDragCallback());
    dragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    shadowWidth = DensityUtil.dp2px(getContext(), 10);
    if (DeviceUtil.checkDeviceHasNavigationBar(getContext())) {
      navigationHeight = DeviceUtil.getNavigationBarHeight(getContext());
    }
  }

  @Override
  public void computeScroll() {
    if (dragHelper != null && dragHelper.continueSettling(true)) {
      invalidate();
    }
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (enable) {
      return dragHelper.shouldInterceptTouchEvent(ev);
    }
    return super.onInterceptTouchEvent(ev);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (enable) {
      dragHelper.processTouchEvent(event);
      return true;
    }
    return super.onTouchEvent(event);
  }


  @Override
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    if (shadowDrawable != null) {
      if (swipeDirection == SwipeDirection.LEFT) {
        shadowDrawable.setBounds(getWidth() + viewLeft, 0, getWidth() + shadowWidth + viewLeft,
            getHeight());
      } else if (swipeDirection == SwipeDirection.RIGHT) {
        shadowDrawable.setBounds(viewLeft - shadowWidth, 0, viewLeft, getHeight());
      } else if (swipeDirection == SwipeDirection.UP) {
        shadowDrawable.setBounds(0, getHeight() + viewTop - navigationHeight, getWidth(),
            getHeight() + viewTop + shadowWidth - navigationHeight);
      } else if (swipeDirection == SwipeDirection.DOWN) {
        shadowDrawable.setBounds(0, viewTop - shadowWidth, getWidth(), viewTop);
      }
      shadowDrawable.draw(canvas);
    }
  }

  private class ViewDragCallback extends ViewDragHelper.Callback {

    /**
     * true表示可以拖动
     */
    @Override
    public boolean tryCaptureView(@NonNull View child, int pointerId) {
      return !fromEdge;
    }

    @Override
    public void onEdgeDragStarted(int edgeFlags, int pointerId) {
      super.onEdgeDragStarted(edgeFlags, pointerId);
      LogUtil.d(TAG, "onEdgeDragStarted");
      dragHelper.captureChildView(SwipeLayout.this.getChildAt(0), pointerId);
    }

    @Override
    public void onEdgeTouched(int edgeFlags, int pointerId) {
      super.onEdgeTouched(edgeFlags, pointerId);
      LogUtil.d(TAG, "edgeFlags:" + edgeFlags);
    }

    @Override
    public void onViewDragStateChanged(int state) {
      super.onViewDragStateChanged(state);
    }

    /**
     * child 位置发生变化时回调
     */
    @Override
    public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx,
        int dy) {
      super.onViewPositionChanged(changedView, left, top, dx, dy);
      viewLeft = left;
      viewTop = top;
      invalidate();
    }

    /**
     * 手指离开当前捕获的View的时候，会回调这个方法
     */
    @Override
    public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
      super.onViewReleased(releasedChild, xvel, yvel);
      LogUtil.d(TAG, "onViewReleased");
      boolean swiped = false;
      if (swipeDirection == SwipeDirection.LEFT) {
        swiped = releasedChild.getRight() < getWidth() * swipeThreshold;
      } else if (swipeDirection == SwipeDirection.RIGHT) {
        swiped = releasedChild.getLeft() > getWidth() * swipeThreshold;
      } else if (swipeDirection == SwipeDirection.UP) {
        swiped = releasedChild.getBottom() < getHeight() * swipeThreshold;
      } else if (swipeDirection == SwipeDirection.DOWN) {
        swiped = releasedChild.getTop() > getHeight() * swipeThreshold;
      }
      if (swiped) {
        if (listener != null) {
          listener.onSwiped();
        }
      } else {
        // 结合computeScroll回到初始位置
        dragHelper.settleCapturedViewAt(0, 0);
        invalidate();
      }
    }


    /**
     * 返回一个大于0的数，则横向可滑动
     */
    @Override
    public int getViewHorizontalDragRange(@NonNull View child) {
      return 1;
    }

    /**
     * 返回一个大于0的数，则纵向可滑动
     */
    @Override
    public int getViewVerticalDragRange(@NonNull View child) {
      return 1;
    }

    /**
     * 控制child横向移动的边界
     * left表示即将移动到的位置。比如右滑返回场景，
     * 即：最小>=0，最大<=屏幕宽度
     */
    @Override
    public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
      int x = 0;
      if (swipeDirection == SwipeDirection.LEFT) {
        if (left > 0) {
          x = 0;
        } else if (-left > getWidth()) {
          x = -getWidth();
        } else {
          x = left;
        }
      } else if (swipeDirection == SwipeDirection.RIGHT) {
        if (left < 0) {
          x = 0;
        } else if (left > getWidth()) {
          x = getWidth();
        } else {
          x = left;
        }
      }
      return x;
    }

    /**
     * 控制child纵向移动的边界
     */
    @Override
    public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
      int y = 0;
      if (swipeDirection == SwipeDirection.UP) {
        if (top > 0) {
          y = 0;
        } else if (-top > getHeight()) {
          y = -getHeight();
        } else {
          y = top;
        }
      } else if (swipeDirection == SwipeDirection.DOWN) {
        if (top < 0) {
          y = 0;
        } else if (top > getHeight()) {
          y = getHeight();
        } else {
          y = top;
        }
      }
      return y;
    }
  }

  /**
   * 是否允许滑动
   * 
   * @param enable
   */
  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  /**
   * true表示只有触摸边界时才出发右滑返回
   * 
   * @param edge
   */
  public void setFromEdge(boolean edge) {
    this.fromEdge = edge;
  }

  /**
   * 设置边缘阴影
   */
  public void setEdgeShadow(Drawable shadow) {
    shadowDrawable = shadow;
  }

  /**
   * 设置手势滑动的方向
   */
  public void setDirection(@SwipeDirection int direction) {
    swipeDirection = direction;
  }

  /**
   * 设置触发释放后自动滑动返回的阈值
   */
  public void setSwipeThreshold(@FloatRange(from = 0.0f, to = 1.0f) float threshold) {
    swipeThreshold = threshold;
  }

  public @interface SwipeDirection {
    int LEFT = 0;
    int RIGHT = 1;
    int UP = 2;
    int DOWN = 3;
  }


  public void setOnSwipedListener(OnSwipedListener listener) {
    this.listener = listener;
  }

  public interface OnSwipedListener {
    void onSwiped();
  }

}
