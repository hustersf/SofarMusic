package com.sf.sofarmusic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sf.utility.DensityUtil;

public class SlideQuadView extends View {

  private static final String TAG = "SlideQuadView";

  private Paint mPaint;
  private Path mQuadPath;

  private int mColor = 0xFF2ED4A9;

  private float progress;
  private String text = "查看更多";

  public SlideQuadView(Context context) {
    this(context, null);
  }

  public SlideQuadView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SlideQuadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setColor(mColor);
    mPaint.setStrokeWidth(1);
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setTextSize(DensityUtil.dp2px(getContext(), 12));
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int width = getWidth();
    int height = getHeight();

    int startX = width;
    int startY = 0;

    int endX = width;
    int endY = height;


    int quadX = (int) (width - width * progress * 3 / 2);
    int quadY = height / 2;
    mQuadPath = new Path();
    mQuadPath.moveTo(startX, startY);
    mQuadPath.quadTo(quadX, quadY, endX, endY);

    canvas.drawPath(mQuadPath, mPaint);
  }

  public void refreshView(float progress) {
    this.progress = progress;
    invalidate();
  }

  /**
   * 设置文字
   */
  public void setSlideText(String text) {
    this.text = text;
    invalidate();
  }

}
