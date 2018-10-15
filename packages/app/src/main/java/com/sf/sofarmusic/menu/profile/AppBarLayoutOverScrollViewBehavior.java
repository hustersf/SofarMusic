package com.sf.sofarmusic.menu.profile;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class AppBarLayoutOverScrollViewBehavior extends AppBarLayout.Behavior {

  private static final String TAG = "overScroll";

  private View mTargetView;
  private int mParentHeight; // AppBarLayout的初始高度
  private int mTargetViewHeight; // 目标View的高度

  private static final float TARGET_HEIGHT = 500; // 最大滑动距离
  private float mTotalDy; // 总滑动的像素数
  private float mLastScale; // 最终放大比例
  private int mLastBottom; // AppBarLayout的最终Bottom值

  private boolean isAnimate; // 是否有动画

  public AppBarLayoutOverScrollViewBehavior() {}

  public AppBarLayoutOverScrollViewBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
    boolean result = super.onLayoutChild(parent, abl, layoutDirection);
    // 需要在调用过super.onLayoutChild()方法之后获取
    if (mTargetView == null) {
      mTargetView = parent.findViewWithTag(TAG);
      if (mTargetView != null) {
        init(abl);
      }

    }
    return result;
  }

  @Override
  public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child,
      View directTargetChild, View target, int nestedScrollAxes, int type) {
    // 开始滑动时，启用动画
    isAnimate = true;
    return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes,
        type);
  }

  private void init(AppBarLayout abl) {
    // 必须设置ClipChildren为false，这样目标View在放大时才能超出布局的范围
    abl.setClipChildren(false);
    mParentHeight = abl.getHeight();
    mTargetViewHeight = mTargetView.getHeight();
  }


  // 重写onNestedPreScroll()修改AppBarLayou滑动到顶部后的行为
  @Override
  public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
      View target, int dx, int dy, int[] consumed, int type) {
    // 1. mTargetView不为null
    // 2. 是向下滑动，dy<0表示向下滑动
    // 3. AppBarLayout已经完全展开，child.getBottom>=mParentHeight
    if (mTargetView != null && dy < 0 && child.getBottom() >= mParentHeight) {
      down(dy, child);
    } else if (mTargetView != null && dy > 0 && child.getBottom() >= mParentHeight) {
      up(dy, child, target);
    } else {
      super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    }
  }

  // 下滑逻辑
  private void down(int dy, AppBarLayout child) {
    // 累加垂直方向上滑动的像素数
    mTotalDy += -dy;
    // 不能大于最大滑动距离
    mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
    // 计算目标View缩放比例，不能小于1
    mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
    // 缩放目标View
    mTargetView.setScaleX(mLastScale);
    mTargetView.setScaleY(mLastScale);
    // 计算目标View放大后增加的高度
    mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
    // 修改AppBarLayout的高度
    child.setBottom(mLastBottom);

  }

  // 上滑逻辑
  private void up(int dy, AppBarLayout child, View target) {
    // 累减垂直方向上滑动的像素数
    mTotalDy -= dy;
    // 计算目标View缩放比例，不能小于1
    mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
    // 缩放目标View
    mTargetView.setScaleX(mLastScale);
    mTargetView.setScaleY(mLastScale);
    // 计算目标View缩小后减少的高度
    mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
    // 修改AppBarLayout的高度
    child.setBottom(mLastBottom);
    // 保持target不滑动
    target.setScrollY(0);
  }

  @Override
  public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout,
      @NonNull AppBarLayout child, @NonNull View target, float velocityX, float velocityY) {
    // 如果触发了快速滚动且垂直方向上速度大于100，则禁用动画
    if (velocityY > 100) {
      isAnimate = false;
    }
    return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
  }

  /**
   * 还原
   * 当AppBarLayout处于越界时，如果用户松开手指，此时应该让目标View和AppBarLayout都还原到原始状态
   */
  @Override
  public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target,
      int type) {
    recovery(abl);
    super.onStopNestedScroll(coordinatorLayout, abl, target, type);
  }


  private void recovery(final AppBarLayout abl) {
    if (mTotalDy > 0) {
      mTotalDy = 0;
      if (isAnimate) {
        ValueAnimator anim = ValueAnimator.ofFloat(mLastScale, 1f).setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            mTargetView.setScaleX(value);
            mTargetView.setScaleY(value);
            abl.setBottom((int) (mLastBottom
                - (mLastBottom - mParentHeight) * animation.getAnimatedFraction()));
          }
        });
        anim.start();
      } else {
        mTargetView.setScaleX(1);
        mTargetView.setScaleY(1);
        abl.setBottom(mParentHeight);
      }
    }
  }


}
