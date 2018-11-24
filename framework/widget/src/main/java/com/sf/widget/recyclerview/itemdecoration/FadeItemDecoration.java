package com.sf.widget.recyclerview.itemdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;

/**
 * 实现边缘渐变效果
 */
public class FadeItemDecoration extends RecyclerView.ItemDecoration {

  private Paint mPaint;
  private LinearGradient mGradient;
  private int mOrientation;
  private int[] mColors;

  private int mFadeWidth = 50; // 50dp
  private int mWidth;

  public FadeItemDecoration(Context context, int orientation) {
    mPaint = new Paint();
    mOrientation = orientation;
    mWidth = DeviceUtil.getMetricsWidth(context);
    mFadeWidth = DensityUtil.dp2px(context, mFadeWidth);
    if (mOrientation == OrientationHelper.HORIZONTAL) {
      mColors = new int[] {Color.parseColor("#00FFFFFF"), Color.parseColor("#FFFFFFFF")};
      mGradient = new LinearGradient(mWidth - mFadeWidth, 0, mWidth, 0, mColors, null,
          Shader.TileMode.CLAMP);
    } else {
      mColors = new int[] {Color.parseColor("#FFFFFFFF"), Color.parseColor("#00FFFFFF")};
      mGradient = new LinearGradient(0, 0, 0, mFadeWidth, mColors, null, Shader.TileMode.CLAMP);
    }
    mPaint.setShader(mGradient);
  }

  @Override
  public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDrawOver(c, parent, state);
    if (mOrientation == OrientationHelper.HORIZONTAL) {
      c.drawRect(mWidth - mFadeWidth, 0, mWidth, parent.getHeight(), mPaint);
    } else {
      c.drawRect(0, 0, mWidth, mFadeWidth, mPaint);
    }
  }

}
