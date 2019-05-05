package com.sf.demo.list.drama;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.sf.utility.DensityUtil;

public class DramaBoardSnapHelper extends PagerSnapHelper {

  @Nullable
  @Override
  public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
      @NonNull View targetView) {
    int[] out = super.calculateDistanceToFinalSnap(layoutManager, targetView);
    out[0] += DensityUtil.dp2px(targetView.getContext(), 8);
    return out;
  }

  @Nullable
  @Override
  public View findSnapView(RecyclerView.LayoutManager layoutManager) {
    return super.findSnapView(layoutManager);
  }

  @Override
  public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX,
      int velocityY) {
    int targrtPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
    if (velocityX > 0 && layoutManager instanceof GridLayoutManager) {
      targrtPosition = targrtPosition - 1 + ((GridLayoutManager) layoutManager).getSpanCount();
    }
    return targrtPosition;
  }
}
