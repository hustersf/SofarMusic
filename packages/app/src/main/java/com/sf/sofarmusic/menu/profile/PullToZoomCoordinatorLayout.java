package com.sf.sofarmusic.menu.profile;

import android.content.Context;
import android.os.SystemClock;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toolbar;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.util.ViewUtil;

public class PullToZoomCoordinatorLayout extends PullToZoomBase<CoordinatorLayout> {

  private static Interpolator sInterpolator = new DecelerateInterpolator(4f);

  private int mDeltaY;
  private int mHeaderViewHeight;
  private int mZoomViewHeight;
  private ScalingRunnable mScalingRunnable;
  private int[] mLocationHeader = new int[2];


  // 这些都是为了判断将MotionEvent交给本布局还是它的子布局
  private RecyclerView mRecyclerView;
  private View mTabView;
  private int mTabHeight;
  private View mChildView;
  private int[] mLocationChild = new int[2];


  public PullToZoomCoordinatorLayout(Context context) {
    super(context);
  }

  public PullToZoomCoordinatorLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    mScalingRunnable = new ScalingRunnable();
  }

  @Override
  protected CoordinatorLayout onCreateRootView(Context context, AttributeSet attrs) {
    CoordinatorLayout layout = ViewUtil.inflate(context, R.layout.layout_profile_root);
    return layout;
  }

  @Override
  protected HeaderViewHolder onCreateHeaderView(Context context, AttributeSet attrs) {
    View headerView = mRootView.findViewById(R.id.ll_head);
    View zoomView = mRootView.findViewById(R.id.iv_background);
    mRecyclerView = mRootView.findViewById(R.id.rv_profile);
    mTabView = mRootView.findViewById(R.id.layout_tab);
    return new HeaderViewHolder(headerView, zoomView);
  }

  @Override
  protected void pullHeaderToZoom(int newScrollValue) {
    if (!mScalingRunnable.isFinished()) {
      // 此时禁止下拉
      return;
    }
    mDeltaY = Math.abs(newScrollValue);
    ViewGroup.LayoutParams lp = mZoomView.getLayoutParams();
    lp.height = mZoomViewHeight + mDeltaY;
    mZoomView.setLayoutParams(lp);

    // 横向扩展
    mZoomView.setScaleX(1 + mDeltaY * 1.0f / mZoomViewHeight);
  }

  @Override
  protected void smoothScrollToTop() {
    mScalingRunnable.startAnimation(100L);
  }

  // 返回true，则父布局拦截此事件，RecyclerView接受不到事件
  @Override
  protected boolean isReadyForPullStart() {
    if (mChildView == null) {
      mChildView = mRecyclerView.getLayoutManager().findViewByPosition(0);
    }
    mHeaderView.getLocationOnScreen(mLocationHeader);
    mChildView.getLocationOnScreen(mLocationChild);
    // Log.d("pull", "h=" + mHeaderViewHeight + " y=" + mLocationHeader[1] + " child"
    // + mLocationChild[1]);
    // mLocationHeader[1] == 0 表示头布局完全展开 ,后面表示RecyclerView的第一个child也全完展开
    if (mLocationHeader[1] == 0 && mLocationChild[1] == mHeaderViewHeight + mTabHeight) {
      return true;
    }
    return false;
  }



  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    if (mZoomView == null || mHeaderView == null) {
      return;
    }
    int zoomHeight = mZoomView.getHeight();
    // 此处只记录没有滑动前的初始高度，因此仅会赋值一次
    if (zoomHeight > 0 && mZoomViewHeight == 0) {
      mZoomViewHeight = zoomHeight;
    }

    int headerHeight = mHeaderView.getHeight();
    // 此处只记录没有滑动前的初始高度，因此仅会赋值一次
    if (headerHeight > 0 && mHeaderViewHeight == 0) {
      mHeaderViewHeight = headerHeight;
    }

    int tabHeight = mTabView.getHeight();
    // 此处只记录没有滑动前的初始高度，因此仅会赋值一次
    if (tabHeight > 0 && mTabHeight == 0) {
      mTabHeight = tabHeight;
    }
  }

  class ScalingRunnable implements Runnable {
    protected long mDuration;
    protected boolean mIsFinished = true;
    protected long mStartTime;

    public boolean isFinished() {
      return mIsFinished;
    }

    public void run() {
      if (mZoomView == null || mIsFinished || mDeltaY == 0) {
        mIsFinished = true;
        return;
      }
      ViewGroup.LayoutParams lp = mZoomView.getLayoutParams();
      float progress = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime)
          / (float) mDuration;
      if (progress < 1f) {
        progress = sInterpolator.getInterpolation(progress); // 计算加速度
        int deltaY = (int) (mDeltaY * (1f - progress));
        lp.height = mZoomViewHeight + deltaY;
        mZoomView.setLayoutParams(lp);

        // 横向缩小
        mZoomView.setScaleX(1 + deltaY * 1.0f / mZoomViewHeight);

        post(this);
        return;
      }
      lp.height = mZoomViewHeight;
      mZoomView.setLayoutParams(lp);
      // 横向缩小
      mZoomView.setScaleX(1);
      mDeltaY = 0;
      mIsFinished = true;
    }

    public void startAnimation(long duration) {
      if (mZoomView != null) {
        mStartTime = SystemClock.currentThreadTimeMillis();
        mDuration = duration;
        mIsFinished = false;
        post(this);
      }
    }
  }
}
