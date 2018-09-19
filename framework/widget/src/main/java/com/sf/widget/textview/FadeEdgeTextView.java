package com.sf.widget.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

import com.sf.utility.DensityUtil;

public class FadeEdgeTextView extends AppCompatTextView {

  private FadeEdgeHelper mFadingEdgeHelper;

  public FadeEdgeTextView(Context context) {
    this(context, null);
  }

  public FadeEdgeTextView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public FadeEdgeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }


  private void init() {
    mFadingEdgeHelper = new FadeEdgeHelper(getContext());
  }


  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    mFadingEdgeHelper.saveCanvasLayer(canvas, getWidth(), getHeight());

    mFadingEdgeHelper.drawHorizontal(canvas, getWidth(), getHeight());
    mFadingEdgeHelper.restoreCanvas(canvas);

  }

  private static class FadeEdgeHelper {

    private int fadeEdgeLength;
    private final Paint paint;
    private final Matrix matrix;
    private final Shader shader;
    private int saveCount;

    FadeEdgeHelper(Context context) {
      // fadeEdgeLength = ViewConfiguration.get(context).getScaledFadingEdgeLength();

      fadeEdgeLength = DensityUtil.dp2px(context, 50);
      paint = new Paint();
      matrix = new Matrix();
      shader = new LinearGradient(0, 0, 0, 1, Color.BLACK, Color.TRANSPARENT,
          Shader.TileMode.CLAMP);
      paint.setShader(shader);
      paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    public void saveCanvasLayer(Canvas canvas, int width, int height) {
      saveCount = canvas.getSaveCount();
      canvas.saveLayer(0, 0, fadeEdgeLength, height, null,
          Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
      canvas.saveLayer(width - fadeEdgeLength, 0, width, height, null,
          Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
    }

    public void restoreCanvas(Canvas canvas) {
      canvas.restoreToCount(saveCount);
    }

    public void drawHorizontal(Canvas canvas, int width, int height) {
      matrix.setScale(1, fadeEdgeLength);
      matrix.postRotate(-90);
      matrix.postTranslate(0, 0);
      shader.setLocalMatrix(matrix);
      paint.setShader(shader);
      canvas.drawRect(0, 0, fadeEdgeLength, height, paint);

      matrix.setScale(1, fadeEdgeLength);
      matrix.postRotate(90);
      matrix.postTranslate(width, 0);
      shader.setLocalMatrix(matrix);
      paint.setShader(shader);
      canvas.drawRect(width - fadeEdgeLength, 0, width, height, paint);
    }
  }
}
