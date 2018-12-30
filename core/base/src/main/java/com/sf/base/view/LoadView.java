package com.sf.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.sf.base.R;
import com.sf.utility.DeviceUtil;


/**
 * Created by sufan on 16/8/8.
 * 第一次显示文字时，显示不全（存在问题）
 */
public class LoadView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
  private SurfaceHolder mHolder;
  private Canvas mCanvas;
  private Thread mThread;
  private boolean isRunning;

  private static final int DEFAULT_COLOR = 0xFFFF1493; // 默认线条的颜色
  private static final int TEXT_COLOR = 0xFF333333; // 文字颜色
  private static final float DEFAULT_WIDTH = 2; // dp，默认线条的宽度
  private static final int DEFAULT_HEIGHT = 15; // dp ，默认线条的高度
  private Paint mPaint;

  private String text = "努力加载中";
  private int mTextSize = sp2px(14); // 字体大小
  private int mCount = 4; // 线条数量
  private int mDotCount = 3; // 点的数量
  private float mRadius = dp2px(1);// 点的半径
  private float mSpace = dp2px(3); // 线条之间的间隔
  private int mColor = DEFAULT_COLOR; // 颜色
  private int mTextColor = TEXT_COLOR;
  private float mLineWidth = dp2px(DEFAULT_WIDTH); // 线条宽度
  private boolean mRoundConner = true; // 线条边缘是否是圆角

  private float[] randoms = {0.0f, 0.3f, 0.6f, 0.9f};
  private int k = 0;


  // 当宽度为match也让view显示在中央
  private float mContentWidth; // view中内容的真正宽度
  private float mLeft; // 画布距离左边


  public LoadView(Context context) {
    this(context, null);
  }

  public LoadView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    // 获取自定义属性
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadView);
    mColor = ta.getColor(R.styleable.LoadView_loadColor, mColor);
    mLineWidth = (int) ta.getDimension(R.styleable.LoadView_loadLineWidth, mLineWidth);
    mRoundConner = ta.getBoolean(R.styleable.LoadView_loadConner, mRoundConner);

    ta.recycle();
    mPaint = new Paint();
    mPaint.setAntiAlias(true); // 抗锯齿
    mPaint.setDither(true); // 抗抖动
    mPaint.setStyle(Paint.Style.FILL);

    if (mRoundConner) {
      mPaint.setStrokeCap(Paint.Cap.ROUND); // 边缘为圆
    }
    mPaint.setStrokeWidth(mLineWidth);
    mPaint.setTextSize(mTextSize);
    init();
  }

  private void init() {

    mHolder = getHolder(); // 拿到surfaceview的持有者
    mHolder.addCallback(this); // 添加回调

    setZOrderOnTop(true);// 设置画布 背景透明
    mHolder.setFormat(PixelFormat.TRANSLUCENT);

    // 设置可获得焦点
    setFocusable(true);
    setFocusableInTouchMode(true);

    // 设置屏幕常亮
    setKeepScreenOn(true);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = measureWidth(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);

    setMeasuredDimension(width, height);
  }

  private int measureWidth(int widthMeasureSpec) {

    int result = 0;
    int mode = MeasureSpec.getMode(widthMeasureSpec);
    int size = MeasureSpec.getSize(widthMeasureSpec);

    mContentWidth = getPaddingLeft() + getPaddingRight() + mCount * (mLineWidth + mSpace) +
        mPaint.measureText(text) + mSpace + 5 * 2 * mRadius;


    // 当width为wrap_content时，0，1，2均走(很奇怪)，为match_parent时，只走0
    if (mode == MeasureSpec.EXACTLY) {
      result = size;
      mLeft = (DeviceUtil.getMetricsWidth(getContext()) - mContentWidth) / 2;
      // Log.i("TAG","0");

    } else {
      // Log.i("TAG","1");
      int w = (int) (getPaddingLeft() + getPaddingRight() + mCount * (mLineWidth + mSpace) +
          mPaint.measureText(text) + mSpace + 5 * 2 * mRadius);
      result = w;
      if (mode == MeasureSpec.AT_MOST) {
        // Log.i("TAG","2");
        result = Math.min(size, w);
        mLeft = 0;
      }
    }


    return result;
  }

  private int measureHeight(int heightMeasureSpec) {

    int result = 0;
    int mode = MeasureSpec.getMode(heightMeasureSpec);
    int size = MeasureSpec.getSize(heightMeasureSpec);


    if (mode == MeasureSpec.EXACTLY) {
      result = size;
    } else {
      int h = (int) (getPaddingTop() + getPaddingBottom() + dp2px(DEFAULT_HEIGHT));
      result = h;
      if (mode == MeasureSpec.AT_MOST) {
        result = Math.min(size, h);
      }
    }
    return result;
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {

    // 开启一个线程
    isRunning = true;
    mThread = new Thread(this);
    mThread.start();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    // 关闭线程
    isRunning = false;
  }

  @Override
  public void run() {
    while (isRunning) {

      // 每50ms秒绘制一次
      long start = System.currentTimeMillis();
      draw();
      long end = System.currentTimeMillis();
      try {
        if (end - start < 100) {
          Thread.sleep(100 - (end - start));
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void draw() {
    try {
      // 获得画布
      mCanvas = mHolder.lockCanvas();
      if (mCanvas != null) {
        // draw something
        drawLine();
        drawDes();
        drawDot();
      }


    } catch (Exception e) {

    } finally {
      if (mCanvas != null) {
        mHolder.unlockCanvasAndPost(mCanvas);
      }
    }
  }

  private void drawLine() {
    mPaint.setColor(mColor);
    mCanvas.save();
    mCanvas.translate(getPaddingLeft() + mLineWidth / 2 + mLeft, 0);
    int realHeight = getHeight() - getPaddingTop() - getPaddingBottom();
    for (int i = 0; i < mCount; i++) {
      float x = i * (mSpace + mLineWidth);
      float y1 = (realHeight * 1.0f) * (randoms[k]) + mLineWidth / 2;
      float y2 = realHeight - mLineWidth / 2;
      mCanvas.drawLine(x, y1, x, y2, mPaint);
      k = (++k) % mCount;
    }
    k = (++k) % mCount;
    mCanvas.restore();
  }

  private void drawDes() {
    mCanvas.save();
    mCanvas.translate(getPaddingLeft() + mLineWidth / 2 + mLeft, getHeight() / 2);
    mPaint.setColor(mTextColor);
    mCanvas.drawText(text, mCount * (mSpace + mLineWidth),
        -(mPaint.ascent() + mPaint.descent()) / 2, mPaint);
  }

  private void drawDot() {
    mPaint.setColor(mTextColor);

    float x = mCount * (mSpace + mLineWidth) + mPaint.measureText(text) + mSpace;
    for (int i = 0; i < mDotCount; i++) {
      mCanvas.drawCircle(x + i * 4 * mRadius, 0, mRadius, mPaint);
    }
    mCanvas.restore();
  }

  private float dp2px(float v) {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v,
        getResources().getDisplayMetrics());
  }

  private int sp2px(float v) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v,
        getResources().getDisplayMetrics());
  }

  public void setLoadColor(int color) {
    mColor = color;
  }

  public void setTextColor(int color) {
    mTextColor = color;
  }
}
