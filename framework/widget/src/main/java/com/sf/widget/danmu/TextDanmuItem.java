package com.sf.widget.danmu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.sf.utility.DensityUtil;
import com.sf.utility.LogUtil;

/**
 * 文本类型的弹幕
 */
public class TextDanmuItem implements IDanmuItem {

  private final static String TAG = "TextDanmuItem";

  private static int sBaseSpeed = 3;
  private float mFactor = 1.0f;

  private int mTextColor = Color.WHITE; // 文本颜色
  private int mTextSize = 14; // 文本大小

  // 文本外围绘制一个矩形框框
  private int mLineWidth; // 矩形框框的宽度，为0则不绘制
  private int mCorner; // 矩形框框的圆角

  private RectF mRectF; // 文本区域
  private Paint mPaint;

  private Context mContext;

  private int mContainerWidth, mContainerHeight; // 弹幕容器的宽高
  private int mItemWidth, mItemHeight; // 弹幕item的宽高
  private int mCurrX, mCurrY; // 弹幕的偏移量
  private String mText; // 文本内容

  /**
   * 构建弹幕
   */
  public static TextDanmuItem buildItem(Context context, String text) {
    TextDanmuItem item = new TextDanmuItem(context, text);
    return item;
  }

  public TextDanmuItem(Context context, String text) {
    mContext = context;
    mText = text;
    init();
  }

  private void init() {
    mPaint = new Paint();
    mPaint.setAntiAlias(true); // 抗锯齿
    mPaint.setDither(true); // 抗抖动
    setTextColor(mTextColor);
    setTextSize(mTextSize);

    buildTextRect();
  }

  private void buildTextRect() {
    mItemHeight = DensityUtil.dp2px(mContext, 33);
    mItemWidth = (int) (mPaint.measureText(mText) + DensityUtil.dp2px(mContext, 2 * 10));
    mRectF = new RectF();
  }


  @Override
  public void doDraw(Canvas canvas) {
    if (mContainerWidth != canvas.getWidth() || mContainerHeight != canvas.getHeight()) {
      mContainerWidth = canvas.getWidth();
      mContainerHeight = canvas.getHeight();
    }

    LogUtil.d(TAG, "doDraw:" + mCurrX);
    canvas.save();
    canvas.translate(mCurrX, mCurrY);

    // 绘制文本外面的矩形框框
    if (mLineWidth > 0) {
      mPaint.setStyle(Paint.Style.STROKE);
      mRectF.left = 0;
      mRectF.top = mLineWidth / 2;
      mRectF.right = mItemWidth;
      mRectF.bottom = mItemHeight - mLineWidth / 2;
      canvas.drawRoundRect(mRectF, mCorner, mCorner, mPaint);
    }
    // 绘制文本
    mPaint.setStyle(Paint.Style.FILL);
    float x = mItemWidth / 2 - mPaint.measureText(mText) / 2;
    Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
    float textHeight = fontMetrics.descent - fontMetrics.ascent;
    float y = mItemHeight / 2 + textHeight / 2 - fontMetrics.descent;
    canvas.drawText(mText, x, y, mPaint);

    canvas.restore();

    mCurrX = (int) (mCurrX - sBaseSpeed * mFactor);
  }

  @Override
  public void setStartPosition(int x, int y) {
    mCurrX = x;
    mCurrY = y;
  }

  @Override
  public boolean isOut() {
    return mCurrX < 0 && Math.abs(mCurrX) > mItemWidth;
  }

  @Override
  public int getHeight() {
    return mItemHeight;
  }

  @Override
  public int getWidth() {
    return mItemWidth;
  }

  @Override
  public int getCurrX() {
    return mCurrX;
  }

  @Override
  public int getCurrY() {
    return mCurrY;
  }

  @Override
  public void setSpeedFactor(float factor) {
    mFactor = factor;
  }

  @Override
  public float getSpeedFactor() {
    return mFactor;
  }

  @Override
  public boolean willHit(IDanmuItem runningItem) {
    if (runningItem.getCurrX() + runningItem.getWidth() > mContainerWidth) {
      return true;
    }
    if (runningItem.getSpeedFactor() >= mFactor) {
      return false;
    }

    float len1 = runningItem.getCurrX() + runningItem.getWidth();
    float t1 = len1 / (runningItem.getSpeedFactor() * sBaseSpeed);
    float len2 = t1 * mFactor * sBaseSpeed;
    if (len2 > len1) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 设置文本颜色
   */
  public void setTextColor(int textColor) {
    mTextColor = textColor;
    mPaint.setColor(mTextColor);
  }

  /**
   * 设置文本大小.dp
   */
  public void setTextSize(int textSize) {
    mTextSize = DensityUtil.dp2px(mContext, textSize);
    mPaint.setTextSize(mTextSize);
  }

  /**
   * 设置矩形框框宽度.dp
   */
  public void setLineWidth(int lineWidth) {
    mLineWidth = DensityUtil.dp2px(mContext, lineWidth);
    mPaint.setStrokeWidth(mLineWidth);
  }

  /**
   * 设置矩形框框圆角,dp
   */
  public void setCorner(int corner) {
    mCorner = DensityUtil.dp2px(mContext, corner);
  }
}
