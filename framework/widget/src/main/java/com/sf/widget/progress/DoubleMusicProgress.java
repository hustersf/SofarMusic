package com.sf.widget.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

import com.sf.utility.DensityUtil;
import com.sf.widget.R;

/**
 * Created by sufan on 16/11/17.
 * 可以滑动的ProgressBar，并且带双重进度
 */

public class DoubleMusicProgress extends ProgressBar {
  // 第一进度的默认颜色，播放进度
  private static final int DEFAULT_FIRST_REACH_COLOR = 0xFFFF0000;
  // 第二进度的默认颜色 ，下载进度
  private static final int DEFAULT_SECOND_REACH_COLOR = 0xC8FFFFFF;
  // 未完成进度的默认颜色
  private static final int UNREACH_COLOR = 0x7EFFFFFF;
  // 拖动圆圈的默认颜色
  private static final int CIRCLE_COLOR = 0xFFFFFFFF;

  private static final int DEFAULT_FIRST_REACH_HEIGHT = 2; // dp,决定已完成ProgressBar的高度
  private static final int DEFAULT_SECOND_REACH_HEIGHT = 2; // dp,决定已完成ProgressBar的高度
  private static final int UNREACH_HEIGHT = 2; // dp,决定未完成ProgressBar的高度
  private static final int RADIUS = 7; // dp,决定未完成ProgressBar的高度

  protected int mFirstReachColor;
  protected int mSecondReachColor;
  protected int mUnreachColor;
  protected int mCircleColor;
  protected int mFirstRechHeight;
  protected int mSecondRechHeight;
  protected int mUnreachHeight;
  protected int mRadius;

  private Paint mPaint;
  private int mRealWidth; // 进度条的真正长度

  private OnProgressListener mOnProgressListener;

  public DoubleMusicProgress(Context context) {
    this(context, null);
  }

  public DoubleMusicProgress(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DoubleMusicProgress(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();

    // 获取自定义的属性
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MusicProgress);
    mFirstReachColor = ta.getColor(R.styleable.DoubleMusicProgress_fReachColor, mFirstReachColor);
    mSecondReachColor = ta.getColor(R.styleable.DoubleMusicProgress_sReachColor, mSecondReachColor);
    mUnreachColor = ta.getColor(R.styleable.DoubleMusicProgress_dUnreachColor, mUnreachColor);
    mCircleColor = ta.getColor(R.styleable.DoubleMusicProgress_circleColor, mCircleColor);
    mFirstRechHeight =
        (int) (ta.getDimension(R.styleable.DoubleMusicProgress_fReachHeight, mFirstRechHeight));
    mSecondRechHeight =
        (int) (ta.getDimension(R.styleable.DoubleMusicProgress_sReachHeight, mSecondRechHeight));
    mUnreachHeight =
        (int) (ta.getDimension(R.styleable.DoubleMusicProgress_dUnreachHeight, mUnreachHeight));
    mRadius = (int) (ta.getDimension(R.styleable.DoubleMusicProgress_radius, mRadius));
    ta.recycle();

    // 定义画笔
    mPaint = new Paint();
    mPaint.setAntiAlias(true); // 抗锯齿
    mPaint.setDither(true); // 抗抖动
    mPaint.setStrokeCap(Paint.Cap.ROUND); // 边缘为圆
    mPaint.setStyle(Paint.Style.FILL);
  }

  // 定义一些初始值
  private void init() {
    mFirstReachColor = DEFAULT_FIRST_REACH_COLOR;
    mSecondReachColor = DEFAULT_SECOND_REACH_COLOR;
    mUnreachColor = UNREACH_COLOR;
    mCircleColor = CIRCLE_COLOR;
    mFirstRechHeight = DensityUtil.dp2px(getContext(), DEFAULT_FIRST_REACH_HEIGHT);
    mSecondRechHeight = DensityUtil.dp2px(getContext(), DEFAULT_SECOND_REACH_HEIGHT);
    mUnreachHeight = DensityUtil.dp2px(getContext(), UNREACH_HEIGHT);
    mRadius = DensityUtil.dp2px(getContext(), RADIUS);
  }


  @Override
  protected synchronized void onDraw(Canvas canvas) {
    mRealWidth = getWidth() - getPaddingLeft() - getPaddingRight();
    int fProgressX = (int) (getProgress() * 1.0f / getMax() * mRealWidth);
    int sProgressX = (int) (getSecondaryProgress() * 1.0f / getMax() * mRealWidth);

    canvas.save();
    canvas.translate(getPaddingLeft(), getHeight() / 2);
    // 画第一进度
    mPaint.setColor(mFirstReachColor);
    mPaint.setStrokeWidth(mFirstRechHeight);
    canvas.drawLine(0, 0, fProgressX, 0, mPaint);

    if (sProgressX > 0) {
      // 画第二进度
      mPaint.setColor(mSecondReachColor);
      mPaint.setStrokeWidth(mSecondRechHeight);
      canvas.drawLine(fProgressX, 0, sProgressX, 0, mPaint);

      // 画unReachLine
      mPaint.setColor(mUnreachColor);
      mPaint.setStrokeWidth(mUnreachHeight);
      canvas.drawLine(sProgressX, 0, mRealWidth, 0, mPaint);
    } else {
      // 画unReachLine
      mPaint.setColor(mUnreachColor);
      mPaint.setStrokeWidth(mUnreachHeight);
      canvas.drawLine(fProgressX, 0, mRealWidth, 0, mPaint);
    }


    // 画圆圈
    mPaint.setColor(mCircleColor);
    canvas.drawCircle(fProgressX, 0, mRadius, mPaint);
    canvas.restore();

  }

  @Override
  protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthValue = MeasureSpec.getSize(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);
    setMeasuredDimension(widthValue, height);
  }

  private int measureHeight(int heightMeasureSpec) {

    int result = 0;
    int mode = MeasureSpec.getMode(heightMeasureSpec);
    int size = MeasureSpec.getSize(heightMeasureSpec);


    if (mode == MeasureSpec.EXACTLY) {
      result = size;
    } else {
      int h = getPaddingTop() + getPaddingBottom()
          + Math.max(Math.max(mFirstRechHeight, mSecondRechHeight),
              Math.max(2 * mRadius, mUnreachHeight));
      result = h;
      if (mode == MeasureSpec.AT_MOST) {
        result = Math.min(size, h);
      }
    }
    return result;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int x = (int) event.getX();
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_MOVE:
        if (x < 0) {
          x = 0;
        } else if (x > mRealWidth) {
          x = mRealWidth;
        }
        int progress = (int) ((x * 1.0f) / mRealWidth * 100);
        setProgress(progress);
        if (mOnProgressListener != null) {
          mOnProgressListener.onProgress(getProgress());
        }

        break;
      case MotionEvent.ACTION_UP:
        break;
    }
    return true;
  }

  public void setOnProgressListener(OnProgressListener onProgressListener) {
    mOnProgressListener = onProgressListener;
  }


  public interface OnProgressListener {
    void onProgress(int progress);
  }

}
