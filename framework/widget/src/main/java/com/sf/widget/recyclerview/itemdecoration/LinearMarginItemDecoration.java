package com.sf.widget.recyclerview.itemdecoration;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 * 处理 {@link RecyclerView} 的分割，控制分割的大小.
 */
public class LinearMarginItemDecoration extends RecyclerView.ItemDecoration {

  private final int mBetweenSpace;
  private final int mFirstSpace;
  private final int mLastSpace;
  private final int mOrientation;

  /**
   * 构造函数.
   * 
   * @param orientation 方向 {@link OrientationHelper#HORIZONTAL}
   *          或者 {@link OrientationHelper#VERTICAL}
   * @param betweenSpace
   */
  public LinearMarginItemDecoration(int orientation, int betweenSpace) {
    this(orientation, 0, 0, betweenSpace);
  }

  /**
   * 构造函数.
   * 
   * @param orientation 方向
   * @param sideSpace 离两端（上下or左右）的距离
   * @param betweenSpace 每个 Item 之间的距离
   */
  public LinearMarginItemDecoration(int orientation, int sideSpace, int betweenSpace) {
    this(orientation, sideSpace, sideSpace, betweenSpace);
  }

  /**
   * 构造函数
   * 
   * @param orientation 方向
   * @param firstSpace 第一个 item 离头部的距离.
   * @param lastSpace 最后一个 item 离尾部的距离
   * @param betweenSpace 每个 item 之间的间隔
   */
  public LinearMarginItemDecoration(int orientation, int firstSpace, int lastSpace, int betweenSpace) {
    this.mOrientation = orientation;
    this.mFirstSpace = firstSpace;
    this.mLastSpace = lastSpace;
    this.mBetweenSpace = betweenSpace;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                             RecyclerView.State state) {
    final int totalCount = parent.getAdapter().getItemCount();
    final int childPosition = parent.getChildAdapterPosition(view);
    if (mOrientation == OrientationHelper.HORIZONTAL) {
      outRect.left = childPosition == 0 ? mFirstSpace : mBetweenSpace;
      outRect.right = childPosition == totalCount - 1 ? mLastSpace : 0;
    } else {
      outRect.top = childPosition == 0 ? mFirstSpace : mBetweenSpace;
      outRect.bottom = childPosition == totalCount - 1 ? mLastSpace : 0;
    }
  }
}
