package com.sf.widget.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.sf.widget.R;

/**
 * 圆角/圆形ImageView
 */
public class RoundImageView extends AppCompatImageView {

  private boolean circle; // 是否显示为圆形，如果为圆形则设置的corner无效
  private float borderWidth; // 边框宽度
  private int borderColor = Color.WHITE; // 边框颜色
  private float cornerRadius; // 统一设置圆角半径，优先级高于单独设置每个角的半径
  private float cornerTopLeftRadius; // 左上角圆角半径
  private float cornerTopRightRadius; // 右上角圆角半径
  private float cornerBottomLeftRadius; // 左下角圆角半径
  private float cornerBottomRightRadius; // 右下角圆角半径
  private int maskColor; // 遮罩颜色

  private int width;
  private int height;
  private int radius;

  private Path path;
  private Paint paint;

  private RectF srcRectF; // 图片占的矩形区域
  private float[] srcRadii;

  public RoundImageView(Context context) {
    this(context, null);
  }

  public RoundImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    // 获取自定义属性
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
    circle = ta.getBoolean(R.styleable.RoundImageView_circle, circle);
    borderWidth = ta.getDimension(R.styleable.RoundImageView_borderWidth, borderWidth);
    borderColor = ta.getColor(R.styleable.RoundImageView_borderColor, borderColor);
    cornerRadius = ta.getDimension(R.styleable.RoundImageView_cornerRadius, cornerRadius);
    cornerTopLeftRadius =
        ta.getDimension(R.styleable.RoundImageView_cornerTopLeftRadius, cornerTopLeftRadius);
    cornerTopRightRadius =
        ta.getDimension(R.styleable.RoundImageView_cornerTopRightRadius, cornerTopRightRadius);
    cornerBottomLeftRadius =
        ta.getDimension(R.styleable.RoundImageView_cornerBottomLeftRadius, cornerBottomLeftRadius);
    cornerBottomRightRadius = ta.getDimension(R.styleable.RoundImageView_cornerBottomRightRadius,
        cornerBottomRightRadius);
    maskColor = ta.getColor(R.styleable.RoundImageView_maskColor, maskColor);
    ta.recycle();

    path = new Path();
    paint = new Paint();
    paint.setAntiAlias(true); // 抗锯齿
    paint.setDither(true); // 抗抖动

    srcRectF = new RectF();
    srcRadii = new float[8];

    calculateRadii();
  }

  /**
   * 在View大小发生变化时调用
   */
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    width = w;
    height = h;

    initSrcRectF();
  }

  /**
   * 这几个绘制调用顺序很重要
   */
  @Override
  protected void onDraw(Canvas canvas) {
    path.reset();
    if (circle) {
      path.addCircle(width / 2.0f, height / 2.0f, radius, Path.Direction.CCW);
    } else {
      path.addRoundRect(srcRectF, srcRadii, Path.Direction.CCW);
    }
    canvas.clipPath(path);

    super.onDraw(canvas);

    // 绘制遮罩
    if (maskColor != 0) {
      paint.setStyle(Paint.Style.FILL);
      paint.setColor(maskColor);
      canvas.drawPath(path, paint);
    }

    drawBorder(canvas);
  }

  /**
   * 绘制边框
   */
  private void drawBorder(Canvas canvas) {
    if (borderWidth <= 0) {
      return;
    }

    if (circle) {
      drawCircleBorder(canvas, borderWidth, borderColor, radius - borderWidth / 2.0f);
    } else {
      drawRectFBorder(canvas, borderWidth, borderColor, srcRectF, srcRadii);
    }
  }

  private void drawCircleBorder(Canvas canvas, float borderWidth, int borderColor, float radius) {
    initBorderPaint(borderWidth, borderColor);
    path.addCircle(width / 2.0f, height / 2.0f, radius, Path.Direction.CCW);
    canvas.drawPath(path, paint);
  }

  private void drawRectFBorder(Canvas canvas, float borderWidth, int borderColor, RectF rectF,
      float[] radii) {
    initBorderPaint(borderWidth, borderColor);
    path.addRoundRect(rectF, radii, Path.Direction.CCW);
    canvas.drawPath(path, paint);
  }


  private void initBorderPaint(float borderWidth, int borderColor) {
    path.reset();
    paint.setStrokeWidth(borderWidth);
    paint.setColor(borderColor);
    paint.setStyle(Paint.Style.STROKE);
  }


  /**
   * 计算图片原始区域的RectF
   */
  private void initSrcRectF() {
    if (circle) {
      radius = Math.min(width, height) / 2;
      srcRectF.set(width / 2.0f - radius, height / 2.0f - radius, width / 2.0f + radius,
          height / 2.0f + radius);
    } else {
      srcRectF.set(0, 0, width, height);
    }
  }


  private void calculateRadii() {
    if (circle) {
      return;
    }
    if (cornerRadius > 0) {
      for (int i = 0; i < srcRadii.length; i++) {
        srcRadii[i] = cornerRadius;
      }
    } else {
      srcRadii[0] = srcRadii[1] = cornerTopLeftRadius;
      srcRadii[2] = srcRadii[3] = cornerTopRightRadius;
      srcRadii[4] = srcRadii[5] = cornerBottomRightRadius;
      srcRadii[6] = srcRadii[7] = cornerBottomLeftRadius;
    }
  }
}
