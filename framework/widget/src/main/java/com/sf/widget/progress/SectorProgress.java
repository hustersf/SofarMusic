package com.sf.widget.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.sf.utility.DensityUtil;

/**
 * 扇形进度条
 */
public class SectorProgress extends ProgressBar {

  private Paint mPaint;
  private RectF mRectF;

  private int mColor = 0xFFFF4081;
  private float mStrokeWidth = 1.5f; // dp
  private float mSpace = 0.5f; // dp
  private int mRadius = 50; // dp

  public SectorProgress(Context context) {
    this(context, null);
  }

  public SectorProgress(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SectorProgress(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    mRadius = DensityUtil.dp2px(getContext(), mRadius);
    mStrokeWidth = DensityUtil.dp2px(getContext(), mStrokeWidth);
    mSpace = DensityUtil.dp2px(getContext(), mSpace);
    mPaint = new Paint();
    mPaint.setAntiAlias(true); // 抗锯齿
    mPaint.setDither(true); // 抗抖动
    mPaint.setColor(mColor);
    mPaint.setStrokeWidth(mStrokeWidth);

    mRectF = new RectF();
  }


  @Override
  protected synchronized void onDraw(Canvas canvas) {
    mRadius = (getMeasuredHeight() - getPaddingLeft() - getPaddingRight()) / 2;

    mRectF.left = getWidth() / 2 - mRadius + mStrokeWidth + mSpace;
    mRectF.top = mStrokeWidth + mSpace;
    mRectF.right = getWidth() / 2 + mRadius - mStrokeWidth - mSpace;
    mRectF.bottom = 2 * mRadius - mStrokeWidth - mSpace;

    mPaint.setStyle(Paint.Style.STROKE);
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius - mStrokeWidth / 2, mPaint);
    mPaint.setStyle(Paint.Style.FILL);
    canvas.drawArc(mRectF, 270, 1.0f * getProgress() / getMax() * 360, true, mPaint);
  }

  @Override
  protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int height = measureHeight(heightMeasureSpec);
    setMeasuredDimension(height, height);
  }

  private int measureHeight(int heightMeasureSpec) {
    int result = 0;
    int mode = MeasureSpec.getMode(heightMeasureSpec);
    int size = MeasureSpec.getSize(heightMeasureSpec);

    if (mode == MeasureSpec.EXACTLY) {
      result = size;
    } else {
      int h = getPaddingTop() + getPaddingBottom() + 2 * mRadius;
      result = h;
      if (mode == MeasureSpec.AT_MOST) {
        result = Math.min(size, h);
      }
    }
    return result;
  }

  public void setColor(int color) {
    mColor = color;
  }
}
