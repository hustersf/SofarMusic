package com.sf.widget.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.sf.utility.DensityUtil;

/**
 * 橘色按钮：#FF5800 透明度15%
 * 白色按钮：#FFFFFF 透明度25%
 */
public class ButtonProgress extends ProgressBar {

  private int mButtonColor = 0xFFFF5800;
  private int mProgressColor = 0x26FF5800;
  private int mReachHeight = 20; // dp
  private float mPaintWidth = 1; // dp
  private int mButtonCorner = 2;

  // text相关属性
  private int mTextColor = mButtonColor;
  private int mTextSize = 12; // sp
  private String mInitText; // 进度为0时的文字
  private String mFinishText; // 进度为100时的文字
  private String mPauseText; // 暂停时显示的文字

  private Paint mPaint;
  private RectF mRectF;
  private int mRealWidth; // 进度条的真正长度
  private int mRealHeight; // 进度条的真正高度

  public @Status int mStatus;

  public ButtonProgress(Context context) {
    this(context, null);
  }

  public ButtonProgress(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ButtonProgress(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initConfig();
    initPaint();
  }

  private void initConfig() {
    mReachHeight = DensityUtil.dp2px(getContext(), mReachHeight);
    mPaintWidth = DensityUtil.dp2px(getContext(), mPaintWidth);
    mTextSize = DensityUtil.sp2px(getContext(), mTextSize);
    mButtonCorner = DensityUtil.dp2px(getContext(), mButtonCorner);

    mInitText = "立即下载";
    mFinishText = "立即安装";
    mPauseText = "继续下载";
    mStatus = Status.INIT;
  }

  private void initPaint() {
    // 初始化画笔
    mPaint = new Paint();
    mPaint.setAntiAlias(true); // 抗锯齿
    mPaint.setDither(true); // 抗抖动
    mPaint.setStrokeWidth(mPaintWidth);
    mRectF = new RectF();
  }

  @Override
  protected synchronized void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    mRealWidth = getWidth() - getPaddingLeft() - getPaddingRight();
    mRealHeight = getHeight() - getPaddingTop() - getPaddingBottom();

    // 绘制圆角矩形
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setColor(mButtonColor);
    mRectF.left = mPaintWidth / 2;
    mRectF.top = mPaintWidth / 2;
    mRectF.right = mRealWidth - mPaintWidth / 2;
    mRectF.bottom = mRealHeight - mPaintWidth / 2;
    canvas.drawRoundRect(mRectF, mButtonCorner, mButtonCorner, mPaint);

    // 绘制进度
    int progressX = (int) (getProgress() * 1.0f / getMax() * (mRealWidth - mPaintWidth));
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setColor(mProgressColor);
    mRectF.left = mPaintWidth;
    mRectF.top = mPaintWidth;
    mRectF.right = progressX;
    mRectF.bottom = mRealHeight - mPaintWidth;
    canvas.drawRect(mRectF, mPaint);

    // 绘制文字
    mPaint.setColor(mTextColor);
    mPaint.setTextSize(mTextSize);
    String text;
    if (mStatus == Status.INIT) {
      text = mInitText;
    } else if (mStatus == Status.PAUSE) {
      text = mPauseText;
    } else if (mStatus == Status.FINISH) {
      text = mFinishText;
    } else {
      text = getProgress() + "%";
    }
    float x = mRealWidth / 2 - mPaint.measureText(text) / 2; // 水平居中

    Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
    float textHeight = fontMetrics.descent - fontMetrics.ascent;
    float y = mRealHeight / 2 + textHeight / 2 - fontMetrics.descent; // 竖直居中
    canvas.drawText(text, x, y, mPaint);
  }


  @Override
  protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthValue = MeasureSpec.getSize(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);
    setMeasuredDimension(widthValue, height);
  }

  private int measureHeight(int heightMeasureSpec) {
    int result;
    int mode = MeasureSpec.getMode(heightMeasureSpec);
    int size = MeasureSpec.getSize(heightMeasureSpec);


    if (mode == MeasureSpec.EXACTLY) {
      result = size;
    } else {
      int h =
          getPaddingTop() + getPaddingBottom() + mReachHeight;
      result = h;
      if (mode == MeasureSpec.AT_MOST) {
        result = Math.min(size, h);
      }
    }
    return result;
  }

  /**
   * 设置button的颜色
   */
  public void setButtonColor(int color) {
    mButtonColor = color;
    mTextColor = color;
  }

  /**
   * 设置进度条的颜色
   */
  public void setProgressColor(int color) {
    mProgressColor = color;
  }

  /**
   * 设置进度为0时的文字
   */
  public void setInitText(String text) {
    mInitText = text;
  }

  /**
   * 设置暂停下载时的文字
   */
  public void sePauseText(String text) {
    mPauseText = text;
  }

  /**
   * 设置进度为100时的文字
   */
  public void setFinishText(String text) {
    mFinishText = text;
  }

  public void setStatus(@Status int status) {
    mStatus = status;
    invalidate();
  }

  /**
   * 设置文字大小,sp
   */
  public void setTextSize(int textSize) {
    mTextSize = DensityUtil.sp2px(getContext(), textSize);
  }


  public @interface Status {
    int INIT = 0; // 初始状态
    int PROGRESS = 1; // 下载中
    int PAUSE = 2; // 暂停
    int FINISH = 3; // 下载完成
  }
}
