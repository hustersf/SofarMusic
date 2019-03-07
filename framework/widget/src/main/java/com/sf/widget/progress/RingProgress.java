package com.sf.widget.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.sf.utility.DensityUtil;

/**
 * 圆环状进度条
 */
public class RingProgress extends ProgressBar {

  private static final int REACH_COLOR = 0xFFFF1493; // 已完成进度的默认颜色
  private static final int UNREACH_COLOR = 0x33FF1493; // 未完成进度的默认颜色

  private Paint mPaint;
  private RectF mRectF;

  private int mRadius = 50; // dp

  private int mReachColor;
  private int mUnreachColor;
  private int mStrokeWidth = 10; // dp


  public RingProgress(Context context) {
    this(context, null);
  }

  public RingProgress(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RingProgress(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    // 数据初始化
    mReachColor = REACH_COLOR;
    mUnreachColor = UNREACH_COLOR;
    mRadius = DensityUtil.dp2px(context, mRadius);
    mStrokeWidth = DensityUtil.dp2px(context, mStrokeWidth);

    // 初始化画笔
    mPaint = new Paint();
    mPaint.setAntiAlias(true); // 抗锯齿
    mPaint.setDither(true); // 抗抖动
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(mStrokeWidth);
    mPaint.setStrokeCap(Paint.Cap.ROUND);

    mRectF = new RectF();
  }


  @Override
  protected synchronized void onDraw(Canvas canvas) {
    mRadius = (getMeasuredHeight() - getPaddingLeft() - getPaddingRight()) / 2;
    mRectF.left = mStrokeWidth / 2;
    mRectF.top = mStrokeWidth / 2;
    mRectF.right = 2 * mRadius - mStrokeWidth / 2;
    mRectF.bottom = 2 * mRadius - mStrokeWidth / 2;

    // 绘制未完成的进度
    mPaint.setColor(mUnreachColor);
    canvas.drawArc(mRectF, 0, 360, false, mPaint);

    // 绘制已完成的进度
    mPaint.setColor(mReachColor);
    canvas.drawArc(mRectF, 0, 1.0f * getProgress() / getMax() * 360, false, mPaint);

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

  /**
   * 设置已完成进度颜色
   */
  public void setReachColor(int color) {
    mReachColor = color;
  }

  /**
   * 设置未完成进度颜色
   */
  public void setUnreachColor(int color) {
    mUnreachColor = color;
  }

  /**
   * 设置圆环的宽度
   */
  public void setRingWidth(int width) {
    mStrokeWidth = width;
  }
}
