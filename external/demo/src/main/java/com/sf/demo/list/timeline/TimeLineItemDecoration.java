package com.sf.demo.list.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.sf.utility.DensityUtil;
import com.sf.utility.LogUtil;

public class TimeLineItemDecoration extends RecyclerView.ItemDecoration {
  private static final String TAG = "TimeLineItemDecoration";

  private int leftOffset;
  private int topOffset;
  private int radius;

  private Paint linePaint;
  private Paint circlePaint;

  public TimeLineItemDecoration(Context context) {
    leftOffset = DensityUtil.dp2px(context, 50);
    topOffset = DensityUtil.dp2px(context, 20);
    radius = DensityUtil.dp2px(context, 10);

    linePaint = new Paint();
    linePaint.setColor(Color.RED);
    linePaint.setStrokeWidth(DensityUtil.dp2px(context, 2));

    circlePaint = new Paint();
    circlePaint.setColor(Color.YELLOW);
    circlePaint.setStyle(Paint.Style.FILL);
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);
    outRect.left = leftOffset;
    outRect.top = topOffset;
  }

  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDraw(c, parent, state);
    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);
      if (child == null) {
        continue;
      }

      LogUtil.d(TAG, child.getTop() + ":" + child.getLeft() + ":" + child.getHeight());
      float x = leftOffset / 2;
      c.drawLine(x, child.getTop(), x, child.getTop() + child.getHeight() + topOffset, linePaint);

      float cx = x;
      float cy = child.getTop() + radius;
      c.drawCircle(cx, cy, radius, circlePaint);

    }
  }
}
