package com.sf.widget.highlight;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.utility.ViewUtil;
import com.sf.utility.statusbar.StatusBarUtil;

/**
 * 高亮引导的遮罩层
 */
class MaskView extends ViewGroup {

  private static final String TAG = "MaskView";

  private final RectF mTargetRect = new RectF();
  private final RectF mChildTmpRect = new RectF();
  private final Paint mFullingPaint = new Paint();

  private int mPadding = 0;
  private int mPaddingLeft = 0;
  private int mPaddingTop = 0;
  private int mPaddingRight = 0;
  private int mPaddingBottom = 0;

  private boolean mOverlayTarget;
  private int mCorner = 0;
  private int mStyle = Component.ROUNDRECT;
  private Paint mEraserPaint;
  private Bitmap mEraserBitmap;
  private Canvas mEraserCanvas;

  // 在目标区域外围绘制虚线装饰一下
  private Paint mDashedPaint;
  private RectF mDashedRect = new RectF();
  private boolean mDashedDecoration = false;
  private int mDashedSpace;

  private boolean mTargetViewRectMax = true; // 在绘制圆形高亮样式时，半径是否取view的宽高中大的一方

  public MaskView(Context context) {
    this(context, null);
  }

  public MaskView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MaskView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setWillNotDraw(false);

    mEraserPaint = new Paint();
    mEraserPaint.setColor(0xFFFFFFFF);
    mEraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    mEraserPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

    mDashedPaint = new Paint();
    mDashedPaint.setColor(0xFFFFFFFF);
    mDashedPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    int dash = DensityUtil.dp2px(context, 4);
    mDashedPaint.setStrokeWidth(DensityUtil.dp2px(context, 2));
    mDashedPaint.setStyle(Paint.Style.STROKE);
    mDashedPaint.setPathEffect(new DashPathEffect(new float[] {2 * dash, dash}, 0));
    mDashedSpace = DensityUtil.dp2px(context, 5);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    try {
      clearFocus();
      mEraserCanvas.setBitmap(null);
      mEraserBitmap = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    final int w = MeasureSpec.getSize(widthMeasureSpec);
    final int h = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(w, h);

    final int count = getChildCount();
    View child;
    for (int i = 0; i < count; i++) {
      child = getChildAt(i);
      if (child != null) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp == null) {
          child.setLayoutParams(lp);
        }
        measureChild(child, w + MeasureSpec.AT_MOST, h + MeasureSpec.AT_MOST);
      }
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int count = getChildCount();
    final float density = getResources().getDisplayMetrics().density;
    View child;
    for (int i = 0; i < count; i++) {
      child = getChildAt(i);
      if (child == null) {
        continue;
      }
      LayoutParams lp = (LayoutParams) child.getLayoutParams();
      if (lp == null) {
        continue;
      }
      switch (lp.targetAnchor) {
        case LayoutParams.ANCHOR_LEFT:// 左
          mChildTmpRect.right = mTargetRect.left;
          if (mDashedDecoration) {
            mChildTmpRect.right -= mDashedSpace;
          }
          mChildTmpRect.left = mChildTmpRect.right - child.getMeasuredWidth();
          verticalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition);
          break;
        case LayoutParams.ANCHOR_TOP:// 上
          mChildTmpRect.bottom = mTargetRect.top;
          if (mDashedDecoration) {
            mChildTmpRect.bottom -= mDashedSpace;
          }
          mChildTmpRect.top = mChildTmpRect.bottom - child.getMeasuredHeight();
          horizontalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition);
          break;
        case LayoutParams.ANCHOR_RIGHT:// 右
          mChildTmpRect.left = mTargetRect.right;
          if (mDashedDecoration) {
            mChildTmpRect.left += mDashedSpace;
          }
          mChildTmpRect.right = mChildTmpRect.left + child.getMeasuredWidth();
          verticalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition);
          break;
        case LayoutParams.ANCHOR_BOTTOM:// 下
          mChildTmpRect.top = mTargetRect.bottom;
          if (mDashedDecoration) {
            mChildTmpRect.top += mDashedSpace;
          }
          mChildTmpRect.bottom = mChildTmpRect.top + child.getMeasuredHeight();
          horizontalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition);
          break;
        case LayoutParams.ANCHOR_OVER:// 中心
          mChildTmpRect.left = ((int) mTargetRect.width() - child.getMeasuredWidth()) >> 1;
          mChildTmpRect.top = ((int) mTargetRect.height() - child.getMeasuredHeight()) >> 1;
          mChildTmpRect.right = ((int) mTargetRect.width() + child.getMeasuredWidth()) >> 1;
          mChildTmpRect.bottom = ((int) mTargetRect.height() + child.getMeasuredHeight()) >> 1;
          mChildTmpRect.offset(mTargetRect.left, mTargetRect.top);
          break;
      }
      // 额外的xy偏移
      mChildTmpRect.offset((int) (density * lp.offsetX + 0.5f),
          (int) (density * lp.offsetY + 0.5f));
      child.layout((int) mChildTmpRect.left, (int) mChildTmpRect.top, (int) mChildTmpRect.right,
          (int) mChildTmpRect.bottom);
    }
  }

  private void horizontalChildPositionLayout(View child, RectF rect, int targetParentPosition) {
    switch (targetParentPosition) {
      case LayoutParams.PARENT_START:
        rect.left = mTargetRect.left;
        rect.right = rect.left + child.getMeasuredWidth();
        break;
      case LayoutParams.PARENT_CENTER:
        rect.left = (mTargetRect.width() - child.getMeasuredWidth()) / 2;
        rect.right = (mTargetRect.width() + child.getMeasuredWidth()) / 2;
        rect.offset(mTargetRect.left, 0);
        break;
      case LayoutParams.PARENT_END:
        rect.right = mTargetRect.right;
        rect.left = rect.right - child.getMeasuredWidth();
        break;
    }
  }

  private void verticalChildPositionLayout(View child, RectF rect, int targetParentPosition) {
    switch (targetParentPosition) {
      case LayoutParams.PARENT_START:
        rect.top = mTargetRect.top;
        rect.bottom = rect.top + child.getMeasuredHeight();
        break;
      case LayoutParams.PARENT_CENTER:
        rect.top = (mTargetRect.width() - child.getMeasuredHeight()) / 2;
        rect.bottom = (mTargetRect.width() + child.getMeasuredHeight()) / 2;
        rect.offset(0, mTargetRect.top);
        break;
      case LayoutParams.PARENT_END:
        rect.bottom = mTargetRect.bottom;
        rect.top = mTargetRect.bottom - child.getMeasuredHeight();
        break;
    }
  }

  private void resetOutPath() {
    resetPadding();
  }

  /**
   * 设置padding
   */
  private void resetPadding() {
    if (mPadding != 0 && mPaddingLeft == 0) {
      mTargetRect.left -= mPadding;
    }
    if (mPadding != 0 && mPaddingTop == 0) {
      mTargetRect.top -= mPadding;
    }
    if (mPadding != 0 && mPaddingRight == 0) {
      mTargetRect.right += mPadding;
    }
    if (mPadding != 0 && mPaddingBottom == 0) {
      mTargetRect.bottom += mPadding;
    }
    if (mPaddingLeft != 0) {
      mTargetRect.left -= mPaddingLeft;
    }
    if (mPaddingTop != 0) {
      mTargetRect.top -= mPaddingTop;
    }
    if (mPaddingRight != 0) {
      mTargetRect.right += mPaddingRight;
    }
    if (mPaddingBottom != 0) {
      mTargetRect.bottom += mPaddingBottom;
    }
  }

  @Override
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
  }

  @Override
  protected void dispatchDraw(Canvas canvas) {
    final long drawingTime = getDrawingTime();
    try {
      View child;
      for (int i = 0; i < getChildCount(); i++) {
        child = getChildAt(i);
        drawChild(canvas, child, drawingTime);
      }
    } catch (NullPointerException e) {

    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    int width = canvas.getWidth();
    int height = canvas.getHeight();
    if (width == 0 || height == 0) {
      width = DeviceUtil.getMetricsWidth(getContext());
      height = DeviceUtil.getMetricsHeight(getContext())
          + StatusBarUtil.getStatusBarHeight(getContext());
    }
    mEraserBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    mEraserCanvas = new Canvas(mEraserBitmap);
    mEraserBitmap.eraseColor(Color.TRANSPARENT);
    mEraserCanvas.drawColor(mFullingPaint.getColor());

    // 虚线装饰的区域
    mDashedRect.set(mTargetRect.left - mDashedSpace, mTargetRect.top - mDashedSpace,
        mTargetRect.right + mDashedSpace, mTargetRect.bottom + mDashedSpace);

    if (!mOverlayTarget) {
      switch (mStyle) {
        case Component.ROUNDRECT:
          mEraserCanvas.drawRoundRect(mTargetRect, mCorner, mCorner, mEraserPaint);
          if (mDashedDecoration) {
            mEraserCanvas.drawRoundRect(mDashedRect, mCorner, mCorner, mDashedPaint);
          }
          break;
        case Component.CIRCLE:
          float radius = mTargetViewRectMax
              ? Math.max(mTargetRect.width(), mTargetRect.height()) / 2
              : Math.min(mTargetRect.width(), mTargetRect.height()) / 2;
          mEraserCanvas.drawCircle(mTargetRect.centerX(), mTargetRect.centerY(),
              radius, mEraserPaint);
          if (mDashedDecoration) {
            mEraserCanvas.drawCircle(mDashedRect.centerX(), mDashedRect.centerY(),
                radius + mDashedSpace, mDashedPaint);
          }
          break;
        case Component.OVAL:
          mEraserCanvas.drawOval(mTargetRect, mEraserPaint);
          if (mDashedDecoration) {
            mEraserCanvas.drawOval(mDashedRect, mDashedPaint);
          }
          break;
        default:
          mEraserCanvas.drawRoundRect(mTargetRect, mCorner, mCorner, mEraserPaint);
          if (mDashedDecoration) {
            mEraserCanvas.drawRoundRect(mDashedRect, mCorner, mCorner, mDashedPaint);
          }
          break;
      }
      canvas.drawBitmap(mEraserBitmap, 0, 0, null);
    }
  }

  public void setTargetRect(Rect rect) {
    mTargetRect.set(rect);
    resetOutPath();
    invalidate();
  }

  public void setFullingAlpha(int alpha) {
    mFullingPaint.setAlpha(alpha);
    invalidate();
  }

  public void setFullingColor(int color) {
    mFullingPaint.setColor(color);
    invalidate();
  }

  public void setHighTargetCorner(int corner) {
    this.mCorner = corner;
  }

  public void setHighTargetGraphStyle(int style) {
    this.mStyle = style;
  }

  public void setOverlayTarget(boolean b) {
    mOverlayTarget = b;
  }

  public void setDashedDecoration(boolean b) {
    mDashedDecoration = b;
  }

  public void setPadding(int padding) {
    this.mPadding = padding;
  }

  public void setPaddingLeft(int paddingLeft) {
    this.mPaddingLeft = paddingLeft;
  }

  public void setPaddingTop(int paddingTop) {
    this.mPaddingTop = paddingTop;
  }

  public void setPaddingRight(int paddingRight) {
    this.mPaddingRight = paddingRight;
  }

  public void setPaddingBottom(int paddingBottom) {
    this.mPaddingBottom = paddingBottom;
  }

  public void setTargetViewRectMax(boolean targetViewRectMax) {
    mTargetViewRectMax = targetViewRectMax;
  }

  static class LayoutParams extends ViewGroup.LayoutParams {

    public static final int ANCHOR_LEFT = 0x01;
    public static final int ANCHOR_TOP = 0x02;
    public static final int ANCHOR_RIGHT = 0x03;
    public static final int ANCHOR_BOTTOM = 0x04;
    public static final int ANCHOR_OVER = 0x05;

    public static final int PARENT_START = 0x10;
    public static final int PARENT_CENTER = 0x20;
    public static final int PARENT_END = 0x30;

    public int targetAnchor = ANCHOR_BOTTOM;
    public int targetParentPosition = PARENT_CENTER;
    public int offsetX = 0;
    public int offsetY = 0;

    public LayoutParams(Context c, AttributeSet attrs) {
      super(c, attrs);
    }

    public LayoutParams(int width, int height) {
      super(width, height);
    }

    public LayoutParams(ViewGroup.LayoutParams source) {
      super(source);
    }
  }
}
