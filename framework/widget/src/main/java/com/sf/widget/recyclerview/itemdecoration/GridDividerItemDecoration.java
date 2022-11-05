package com.sf.widget.recyclerview.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 实现gird模式下的间隔，包括GridLayoutManager和StaggeredGridLayoutManager
 * list模式，可用{@link DividerItemDecoration}
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

  private final Drawable mDivider;

  private final Rect mBounds = new Rect();

  public GridDividerItemDecoration(int dividerWidth, int dividerColor) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setShape(GradientDrawable.RECTANGLE);
    drawable.setColor(dividerColor);
    drawable.setSize(dividerWidth, dividerWidth);
    mDivider = drawable;
  }

  public GridDividerItemDecoration(Drawable divider) {
    mDivider = divider;
  }

  /**
   * 绘制在item之下
   */
  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    drawHorizontal(c, parent);
    drawVertical(c, parent);
  }

  /**
   * 画水平线
   */
  private void drawHorizontal(Canvas canvas, RecyclerView parent) {
    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);
      parent.getDecoratedBoundsWithMargins(child, mBounds);
      final int left = mBounds.left;
      final int right = mBounds.right;
      final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
      final int top = bottom - mDivider.getIntrinsicHeight();
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(canvas);
    }

  }

  /**
   * 画垂直线
   */
  private void drawVertical(Canvas canvas, RecyclerView parent) {
    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);
      parent.getDecoratedBoundsWithMargins(child, mBounds);
      final int right = mBounds.right + Math.round(child.getTranslationX());
      final int left = right - mDivider.getIntrinsicWidth();
      final int top = mBounds.top;
      final int bottom = mBounds.bottom;
      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(canvas);
    }
  }

  /**
   * 绘制在item之上
   */
  @Override
  public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDrawOver(c, parent, state);
  }

  /**
   * outRect 可以设置item的paddingLeft，paddingTop， paddingRight， paddingBottom
   */
  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
    int spanCount = getSpanCount(parent);
    int childCount = parent.getAdapter().getItemCount();
    if (isLastColumn(parent, pos, spanCount, childCount)) {
      // 最后一列，不绘制右边
      outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());

    } else if (isLastRaw(parent, pos, spanCount, childCount)) {
      // 最后一行，不绘制底部
      outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
    } else {
      outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
    }
  }

  /**
   * 是否是最后一列
   */
  private boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
      {
        return true;
      }
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      int orientation = ((StaggeredGridLayoutManager) layoutManager)
          .getOrientation();
      if (orientation == StaggeredGridLayoutManager.VERTICAL) {
        if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
        {
          return true;
        }
      } else {
        childCount = childCount - childCount % spanCount;
        if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
          return true;
      }
    }
    return false;
  }

  /**
   * 是否是最后一行
   */
  private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      childCount = childCount - childCount % spanCount;
      if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
        return true;
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      int orientation = ((StaggeredGridLayoutManager) layoutManager)
          .getOrientation();
      // StaggeredGridLayoutManager 且纵向滚动
      if (orientation == StaggeredGridLayoutManager.VERTICAL) {
        childCount = childCount - childCount % spanCount;
        // 如果是最后一行，则不需要绘制底部
        if (pos >= childCount)
          return true;
      } else
      // StaggeredGridLayoutManager 且横向滚动
      {
        // 如果是最后一行，则不需要绘制底部
        if ((pos + 1) % spanCount == 0) {
          return true;
        }
      }
    }
    return false;
  }

  private int getSpanCount(RecyclerView parent) {
    // 列数
    int spanCount = -1;
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      spanCount = ((StaggeredGridLayoutManager) layoutManager)
          .getSpanCount();
    }
    return spanCount;
  }
}
