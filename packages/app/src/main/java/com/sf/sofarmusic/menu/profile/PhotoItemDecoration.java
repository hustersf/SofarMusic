package com.sf.sofarmusic.menu.profile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.util.DensityUtil;

/**
 *
 */
public class PhotoItemDecoration extends RecyclerView.ItemDecoration {

  private Paint mPaint;
  private int mDividerWidth;

  public PhotoItemDecoration(Context context, int dividerWidth) {
    mDividerWidth = dividerWidth;
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(context.getResources().getColor(R.color.themeColor));
    mPaint.setStyle(Paint.Style.FILL);
  }

  // 类似绘制背景
  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDraw(c, parent, state);
    draw(c, parent);
  }

  // 绘制的内容在item上面
  @Override
  public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDrawOver(c, parent, state);
  }

  // 为item实现类似padding的效果
  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
    int spanCount = getSpanCount(parent); // 列数
    int childCount = parent.getAdapter().getItemCount(); // 子View总个数(包含看不见的)

    boolean isLastRow = isLastRow(parent, itemPosition, spanCount, childCount);
    boolean isLastColumn = isLastColumn(parent, itemPosition, spanCount, childCount);


    int column = itemPosition % spanCount;
    int bottom = mDividerWidth;

    // left很关键，这样设置的原因和adapter动态设置宽高为屏幕宽度的1/3有关
    int left = column * mDividerWidth;
    // 这个好像不起做用了，随便写
    int right = mDividerWidth - (column + 1) * mDividerWidth;

    // 最后一行不绘制底部
    if (isLastRow) {
      bottom = 0;
    }

    // 最后一列，不绘制右侧
    if (isLastColumn) {
      right = 0;
    }

    outRect.set(left, 0, right, bottom);

  }

  // 绘制横向 item 分割线
  private void draw(Canvas canvas, RecyclerView parent) {
    int childSize = parent.getChildCount();
    for (int i = 0; i < childSize; i++) {
      View child = parent.getChildAt(i);
      RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

      // 画水平分隔线
      int left = child.getLeft();
      int right = child.getRight();
      int top = child.getBottom() + layoutParams.bottomMargin;
      int bottom = top + mDividerWidth;
      if (mPaint != null) {
        canvas.drawRect(left, top, right, bottom, mPaint);
      }
      // 画垂直分割线
      top = child.getTop();
      bottom = child.getBottom() + mDividerWidth;
      left = child.getRight() + layoutParams.rightMargin;
      right = left + mDividerWidth;
      if (mPaint != null) {
        canvas.drawRect(left, top, right, bottom, mPaint);
      }
    }
  }

  private boolean isLastColumn(RecyclerView parent, int pos, int spanCount,
      int childCount) {
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
        return true;
      }
    }
    return false;
  }

  private boolean isLastRow(RecyclerView parent, int pos, int spanCount,
      int childCount) {
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      int lines =
          childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
      return lines == pos / spanCount + 1;
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
