package com.sf.sofarmusic.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class OverScrollLayer extends FrameLayout implements NestedScrollingParent {

  private NestedScrollingParentHelper mNestedScrollHelper = new NestedScrollingParentHelper(this);

  private boolean isTouchScroll = false;

  private ArrayList<OverScrollListener> mOverScrollListeners = new ArrayList<>();

  private int threshold;

  private boolean isReachedThreshold = false;
  private boolean canOverScroll = true;

  public OverScrollLayer(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    threshold = Math.max(36, getSuggestedMinimumWidth());
  }

  public OverScrollLayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    threshold = Math.max(36, getSuggestedMinimumWidth());
  }

  @Override
  public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
    boolean accept = (axes & ViewCompat.SCROLL_AXIS_HORIZONTAL) != 0;
    isTouchScroll = accept;
    isReachedThreshold = false;
    if (target instanceof RecyclerView) {
      isTouchScroll = false;
      ((RecyclerView) target).addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
          isTouchScroll = newState == RecyclerView.SCROLL_STATE_DRAGGING;
          if (isTouchScroll) {
            if (!recyclerView.canScrollHorizontally(1) || recyclerView.getTranslationX() < 0) {
              requestDisallowInterceptTouchEvent(true);
            }
            target.animate().cancel();
          }
        }
      });
    }
    target.animate().setInterpolator(new Interpolator() {
      private Interpolator interpolator = new AccelerateDecelerateInterpolator();

      @Override
      public float getInterpolation(float input) {
        notifyOverScroll(-target.getTranslationX() * 1.0f / threshold);
        return interpolator.getInterpolation(input);
      }
    });
    return accept && canOverScroll;
  }

  @Override
  public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes) {
    mNestedScrollHelper.onNestedScrollAccepted(child, target, axes);
    target.animate().cancel();
  }

  @Override
  public void onStopNestedScroll(@NonNull View target) {
    mNestedScrollHelper.onStopNestedScroll(target);
    if (target.getTranslationX() != 0) {
      target.animate().translationX(0);
    }
  }

  @Override
  public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
      int dyUnconsumed) {}

  @Override
  public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
    if (target.canScrollHorizontally(1)
        || dx < 0 && target.getTranslationX() <= 0
        || !isTouchScroll) {
      return;
    }
    int offset = 0;
    if (dx > 0) {
      if (target.getTranslationX() <= 0) {
        float maxOffset = Math.min(dx / 2, target.getTranslationX() + threshold);
        target.setTranslationX(target.getTranslationX() - maxOffset);
        offset = dx;
        notifyOverScroll(-target.getTranslationX() * 1.0f / threshold);
      }
    } else if (dx < 0) {
      if (target.getTranslationX() <= 0) {
        float maxOffset = (int) Math.max(target.getTranslationX(), dx / 2);
        target.setTranslationX(target.getTranslationX() - maxOffset);
        offset = dx;
        notifyOverScroll(-target.getTranslationX() * 1.0f / threshold);
      }
    }
    consumed[0] = offset;
  }

  @Override
  public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY,
      boolean consumed) {
    return false;
  }

  @Override
  public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
    return false;
  }

  @Override
  public int getNestedScrollAxes() {
    return mNestedScrollHelper.getNestedScrollAxes();
  }

  public interface OverScrollListener {
    /**
     * @param distance from 0 to 1
     */
    void onOverScroll(float distance);
  }

  public void addOverScrollListener(@NonNull OverScrollListener listener) {
    if (!mOverScrollListeners.contains(listener)) {
      mOverScrollListeners.add(listener);
    }
  }

  public void removeOverScrollListener(OverScrollListener listener) {
    mOverScrollListeners.remove(listener);
  }

  private void notifyOverScroll(float ratio) {
    if (!mOverScrollListeners.isEmpty()) {
      if (ratio >= 1f) {
        if (!isReachedThreshold) {
          isReachedThreshold = true;
          for (OverScrollListener l : mOverScrollListeners) {
            l.onOverScroll(ratio);
          }
        }
      } else {
        for (OverScrollListener l : mOverScrollListeners) {
          l.onOverScroll(ratio);
        }
      }
    }
  }

  /**
   * 是否允许OverScroll
   */
  public void setCanOverScroll(boolean canOverScroll) {
    this.canOverScroll = canOverScroll;
  }

}
