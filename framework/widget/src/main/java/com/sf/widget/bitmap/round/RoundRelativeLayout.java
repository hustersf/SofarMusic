package com.sf.widget.bitmap.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.sf.widget.R;

public class RoundRelativeLayout extends RelativeLayout {

  private Path roundPath;
  private RectF rectF;


  private float roundLayoutRadius = 14f;


  public RoundRelativeLayout(Context context) {
    this(context, null);
  }

  public RoundRelativeLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RoundRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundRelativeLayout);
    roundLayoutRadius =
        ta.getDimension(R.styleable.RoundRelativeLayout_relativeLayoutRadius, roundLayoutRadius);
    ta.recycle();

    init();
  }

  private void init() {
    setWillNotDraw(false);
    rectF = new RectF();
    roundPath = new Path();
  }

  private void setRoundPath() {
    // 添加一个圆角矩形到path中, 如果要实现任意形状的View, 只需要手动添加path就行
    roundPath.addRoundRect(rectF, roundLayoutRadius, roundLayoutRadius, Path.Direction.CW);
  }

  public void setRoundLayoutRadius(float roundLayoutRadius) {
    this.roundLayoutRadius = roundLayoutRadius;
    setRoundPath();
    postInvalidate();
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    rectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
    setRoundPath();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (roundLayoutRadius > 0) {
      canvas.clipPath(roundPath);
    }
    super.onDraw(canvas);
  }
}
