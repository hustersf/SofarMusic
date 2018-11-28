package com.sf.sofarmusic.menu.profile.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sf.utility.DensityUtil;


public class BottomButtonBehavior extends CoordinatorLayout.Behavior {

  ObjectAnimator mAnimator;
  private int mScrollY;

  public BottomButtonBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
    mScrollY = DensityUtil.dp2px(context, 100);
  }

  @Override
  public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
      @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes,
      int type) {
    return axes == ViewCompat.SCROLL_AXIS_VERTICAL
        || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes,
            type);
  }


  /**
   * dyUnconsumed>0,上滑；<0,下滑
   */
  @Override
  public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
      @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
      int type) {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
        dyUnconsumed, type);
    if (dyConsumed > 0) {
      animateButton(child, mScrollY);
    } else if (dyConsumed < 0) {
      animateButton(child, 0);
    }
  }

  // 隐藏或显示时的动画
  private void animateButton(final View view, float destTranslateY) {
    if (view.getTranslationY() == destTranslateY) {
      return;
    }

    if (mAnimator != null && mAnimator.isRunning()) {
      return;
    }

    mAnimator = ObjectAnimator.ofFloat(view, "translationY",
        view.getTranslationY(),
        destTranslateY).setDuration(400);

    mAnimator.start();
  }
}
