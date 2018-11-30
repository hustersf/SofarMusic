package com.sf.sofarmusic.menu.profile.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AppBarBehavior extends AppBarLayout.Behavior {
  private boolean flag;

  public AppBarBehavior() {

  }

  public AppBarBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }


  /**
   * AppBarLayout布局时调用
   *
   * @param parent 父布局CoordinatorLayout
   * @param abl 使用此Behavior的AppBarLayout
   * @param layoutDirection 布局方向
   * @return 返回true表示子View重新布局，返回false表示请求默认布局
   */
  @Override
  public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
    // Log.d("appBar", "onLayoutChild");
    return super.onLayoutChild(parent, abl, layoutDirection);
  }

  /**
   * 当CoordinatorLayout的子View尝试发起嵌套滚动时调用
   *
   * @param parent 父布局CoordinatorLayout
   * @param child 使用此Behavior的AppBarLayout
   * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
   * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
   * @param nestedScrollAxes 嵌套滚动的方向
   * @return 返回true表示接受滚动
   */
  @Override
  public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child,
      View directTargetChild, View target, int nestedScrollAxes, int type) {
    Log.d("appBar", "onStartNestedScroll");
    child.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (-verticalOffset > 743 - 197) {
          flag = false;
        } else {
          flag = true;
        }
      }
    });
    if (flag) {
      return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes,
          type);
    } else {
      return false;
    }
  }



  /**
   * 当嵌套滚动已由CoordinatorLayout接受时调用
   *
   * @param coordinatorLayout 父布局CoordinatorLayout
   * @param child 使用此Behavior的AppBarLayout
   * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
   * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
   */
  @Override
  public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout,
      @NonNull AppBarLayout child, @NonNull View directTargetChild, @NonNull View target, int axes,
      int type) {
    super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    Log.d("appBar", "onNestedScrollAccepted");
  }


  /**
   * 当准备开始嵌套滚动时调用
   *
   * @param coordinatorLayout 父布局CoordinatorLayout
   * @param child 使用此Behavior的AppBarLayout
   * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
   * @param dx 用户在水平方向上滑动的像素数
   * @param dy 用户在垂直方向上滑动的像素数
   * @param consumed 输出参数，consumed[0]为水平方向应该消耗的距离，consumed[1]为垂直方向应该消耗的距离
   */
  @Override
  public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
      View target, int dx, int dy, int[] consumed, int type) {
    super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
    // Log.d("appBar", "onNestedPreScroll:"+mY);
  }



  /**
   * 嵌套滚动时调用
   *
   * @param coordinatorLayout 父布局CoordinatorLayout
   * @param child 使用此Behavior的AppBarLayout
   * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
   * @param dxConsumed 由目标View滚动操作消耗的水平像素数
   * @param dyConsumed 由目标View滚动操作消耗的垂直像素数
   * @param dxUnconsumed 由用户请求但是目标View滚动操作未消耗的水平像素数
   * @param dyUnconsumed 由用户请求但是目标View滚动操作未消耗的垂直像素数
   */
  @Override
  public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
      int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
        dyUnconsumed, type);
    Log.d("appBar", "onNestedScroll:" + dyConsumed + "-" + dyUnconsumed);
  }

  /**
   * 当嵌套滚动的子View准备快速滚动时调用
   *
   * @param coordinatorLayout 父布局CoordinatorLayout
   * @param child 使用此Behavior的AppBarLayout
   * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
   * @param velocityX 水平方向的速度
   * @param velocityY 垂直方向的速度
   * @return 如果Behavior消耗了快速滚动返回true
   */
  @Override
  public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child,
      View target, float velocityX, float velocityY) {
    Log.d("appBar", "onNestedPreFling");
    return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
  }

  /**
   * 当嵌套滚动的子View快速滚动时调用
   *
   * @param coordinatorLayout 父布局CoordinatorLayout
   * @param child 使用此Behavior的AppBarLayout
   * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
   * @param velocityX 水平方向的速度
   * @param velocityY 垂直方向的速度
   * @param consumed 如果嵌套的子View消耗了快速滚动则为true
   * @return 如果Behavior消耗了快速滚动返回true
   */
  @Override
  public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
      float velocityX, float velocityY, boolean consumed) {
    Log.d("appBar", "onNestedFling");
    return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
  }


  /**
   * 当定制滚动时调用
   *
   * @param coordinatorLayout 父布局CoordinatorLayout
   * @param abl 使用此Behavior的AppBarLayout
   * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
   */
  @Override
  public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target,
      int type) {
    super.onStopNestedScroll(coordinatorLayout, abl, target, type);
    Log.d("appBar", "onStopNestedScroll");
  }
}
