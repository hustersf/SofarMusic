package com.sf.demo.view.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.List;

/**
 * Created by wanghan on 2017/2/8.
 */

public class LineChartView extends View {
    private Paint xLinePaint;
    private Paint xScaleTextPaint;
    private Paint yLinePaint;
    private Paint yScaleTextPaint;
    private Paint circlePaint;
    private Paint bgLinePaint;
    private Paint linePaint;
    private Paint layerLinePaint;


    private int yScaleAreaWidth = 100;
    private int xScaleAreaHeight;
    private float xScaleGap;
    private float yScaleGap;
    private int scaleHeight = 15;
    private int xScaleTextMarginTop = 20;
    private int yScaleTextMarginRight = 10;
    private int lineMarginTop = 20;
    private int lineAreaWidth;
    private int lineAreaHeight;
    private float maxYScale;
    private List<IncomeExpendInfo> infos;
    private String[] month;
    private float[] data;

    public static final int TYPE_INCOME = 0;
    public static final int TYPE_EXPEND = 1;

    private final int COLOR_DEFAULT_INCOME = Color.parseColor("#1E902E");
    private final int COLOR_DEFAULT_EXPEND = Color.parseColor("#FA1100");

    private int type = TYPE_INCOME;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewConfig();
    }

    private void initViewConfig() {
        xLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        xLinePaint.setColor(Color.parseColor("#696969"));
        xLinePaint.setStyle(Paint.Style.STROKE);
        xLinePaint.setStrokeWidth(1);

        xScaleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        xScaleTextPaint.setColor(Color.parseColor("#96969e"));
        xScaleTextPaint.setStyle(Paint.Style.STROKE);
        xScaleTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));

        yLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        yLinePaint.setColor(Color.parseColor("#696969"));
        yLinePaint.setStyle(Paint.Style.STROKE);
        yLinePaint.setStrokeWidth(1);

        yScaleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        yScaleTextPaint.setColor(Color.parseColor("#96969e"));
        yScaleTextPaint.setTextAlign(Paint.Align.RIGHT);
        yScaleTextPaint.setStyle(Paint.Style.STROKE);
        yScaleTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));


        bgLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        bgLinePaint.setColor(Color.parseColor("#d1d1d1"));
        bgLinePaint.setStyle(Paint.Style.STROKE);
        bgLinePaint.setStrokeWidth(1);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        circlePaint.setColor(type == TYPE_INCOME ? COLOR_DEFAULT_INCOME : COLOR_DEFAULT_EXPEND);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setStrokeWidth(3);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        linePaint.setColor(type == TYPE_INCOME ? COLOR_DEFAULT_INCOME : COLOR_DEFAULT_EXPEND);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);

        layerLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        layerLinePaint.setColor(type == TYPE_INCOME ? COLOR_DEFAULT_INCOME : COLOR_DEFAULT_EXPEND);
        layerLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        String textMonth = "12月";
        Rect textMonthArea = new Rect();
        xScaleTextPaint.getTextBounds(textMonth, 0, textMonth.length(), textMonthArea);
        xScaleAreaHeight = scaleHeight + textMonthArea.height() + 2 * xScaleTextMarginTop;
        lineAreaWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - yScaleAreaWidth;
        lineAreaHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - xScaleAreaHeight - lineMarginTop;
        yScaleGap = lineAreaHeight / 6.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景横线
        drawXBackgroundLine(canvas);
        if (infos != null && infos.size() != 0) {
            xScaleGap = (lineAreaWidth / (float) (infos.size() + 1));
            //绘制背景纵线
            drawYBackgroundLine(canvas);
            drawXLine(canvas);
            getMaxValue(data);
            drawYText(canvas);
            drawLine(canvas);
        }

    }

    private void drawYText(Canvas canvas) {
        int yValueGap = getYScaleMaxValue(maxYScale) / 6;
        for (int i = 0; i < 6; i++) {
            String text = String.valueOf((i + 1) * yValueGap);
            Rect textArea = new Rect();
            yScaleTextPaint.getTextBounds(text, 0, text.length(), textArea);
            yScaleTextPaint.setTextAlign(Paint.Align.RIGHT);
            rePaintSize(text, yScaleAreaWidth - yScaleTextMarginRight);
            canvas.drawText(text, yScaleAreaWidth - yScaleTextMarginRight , lineAreaHeight + lineMarginTop - (i + 1) * yScaleGap + textArea.height() / 2, yScaleTextPaint);
            //重置Paint
            yScaleTextPaint.setTextSize(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        }
    }

    private void rePaintSize(String text, float maxWidth) {
        float textWidth = yScaleTextPaint.measureText(text);
        int textSizeInDp = 10;

        if (textWidth > maxWidth) {
            for (; textSizeInDp > 0; textSizeInDp--) {
                yScaleTextPaint.setTextSize(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, textSizeInDp, getResources().getDisplayMetrics()));
                textWidth = yScaleTextPaint.measureText(text);
                if (textWidth <= maxWidth) {
                    break;
                }
            }
        }
    }

    private void drawLine(Canvas canvas) {
        float yMaxValue = getYScaleMaxValue(maxYScale);
        //绘制阴影
        Path shaderPath = new Path();
        shaderPath.moveTo(yScaleAreaWidth + xScaleGap, lineAreaHeight + lineMarginTop);
        for (int i = 0; i < data.length; i++) {
            float yPoint = lineMarginTop + (lineAreaHeight) * (1 - (data[i] / yMaxValue));
            if (i == 0) {
                shaderPath.lineTo(yScaleAreaWidth + (i + 1) * xScaleGap, yPoint);
            } else {
                shaderPath.lineTo(yScaleAreaWidth + (i + 1) * xScaleGap, yPoint);
            }
        }
        shaderPath.lineTo(yScaleAreaWidth + (data.length) * xScaleGap, lineAreaHeight + lineMarginTop);
        shaderPath.close();
        layerLinePaint.setAlpha(15);
        canvas.drawPath(shaderPath, layerLinePaint);

        //绘制折现
        Path path = new Path();
        for (int i = 0; i < data.length; i++) {
            float yPoint = lineMarginTop + (lineAreaHeight) * (1 - (data[i] / yMaxValue));
            if (i == 0) {
                path.moveTo(yScaleAreaWidth + (i + 1) * xScaleGap, yPoint);
            } else {
                path.lineTo(yScaleAreaWidth + (i + 1) * xScaleGap, yPoint);
            }
        }
        canvas.drawPath(path, linePaint);

        //绘制圆点
        Path circlePath = new Path();
        for (int i = 0; i < data.length; i++) {
            float yPoint = lineMarginTop + (lineAreaHeight) * (1 - (data[i] / yMaxValue));
            if (i == 0) {
                circlePath.moveTo(yScaleAreaWidth + (i + 1) * xScaleGap, yPoint);
                circlePath.addCircle(yScaleAreaWidth + (i + 1) * xScaleGap, yPoint, 10, Path.Direction.CCW);
            } else {
                circlePath.addCircle(yScaleAreaWidth + (i + 1) * xScaleGap, yPoint, 10, Path.Direction.CCW);
            }
        }
        canvas.drawPath(circlePath, circlePaint);

    }

    private void drawXLine(Canvas canvas) {
        //绘制X轴刻度
        if (month != null && month.length != 0) {
            for (int i = 0; i < month.length + 2; i++) {
                canvas.drawLine(yScaleAreaWidth + i * xScaleGap, lineAreaHeight + lineMarginTop, yScaleAreaWidth + i * xScaleGap, lineAreaHeight + lineMarginTop + scaleHeight, xLinePaint);
            }
            //绘制X轴刻度文本
            for (int i = 0; i < month.length; i++) {
                Rect textMonthArea = new Rect();
                xScaleTextPaint.getTextBounds(month[i], 0, month[i].length(), textMonthArea);
                canvas.drawText(month[i], yScaleAreaWidth + (i + 1) * xScaleGap - textMonthArea.width() / 2, lineAreaHeight + lineMarginTop + scaleHeight + xScaleTextMarginTop + textMonthArea.height(), xScaleTextPaint);
            }
        }


    }

    private void drawXBackgroundLine(Canvas canvas) {
        //绘制背景线
        for (int i = 0; i < 6; i++) {
            canvas.drawLine(yScaleAreaWidth, lineAreaHeight + lineMarginTop - (i + 1) * yScaleGap, lineAreaWidth + yScaleAreaWidth, lineAreaHeight + lineMarginTop - (i + 1) * yScaleGap, bgLinePaint);
        }
        //绘制X轴
        canvas.drawLine(yScaleAreaWidth, lineAreaHeight + lineMarginTop, yScaleAreaWidth + lineAreaWidth, lineAreaHeight + lineMarginTop, xLinePaint);
    }

    private void drawYBackgroundLine(Canvas canvas) {
        //绘制背景线
        for (int i = 0; i < data.length; i++) {
            canvas.drawLine(yScaleAreaWidth + (i + 1) * xScaleGap, lineAreaHeight + lineMarginTop, yScaleAreaWidth + (i + 1) * xScaleGap, lineMarginTop, bgLinePaint);
        }
    }

    private int getYScaleMaxValue(float maxData) {
        int len = (String.valueOf((int) Math.ceil(maxData))).length();
        int y;
        if (len > 1) {
            y = (int) Math.pow(10, len - 2);
        } else {
            y = (int) Math.pow(10, 0);
        }
        return (((int) maxData / 6 / y + 1) * y * 6);
    }

    private void getMaxValue(float[] data) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] >= maxYScale) {
                maxYScale = data[i];
            }
        }
    }

    private void invalidateView() {
        if (Looper.myLooper() == Looper.getMainLooper())
            invalidate();
        else
            postInvalidate();
    }

    public int getType() {
        return type;
    }

    public void setData(int type, List<IncomeExpendInfo> infos) {
        this.type = type;
        this.infos = infos;
        this.maxYScale = 0;
        initData(type, infos);
        circlePaint.setColor(type == TYPE_INCOME ? COLOR_DEFAULT_INCOME : COLOR_DEFAULT_EXPEND);
        linePaint.setColor(type == TYPE_INCOME ? COLOR_DEFAULT_INCOME : COLOR_DEFAULT_EXPEND);
        layerLinePaint.setColor(type == TYPE_INCOME ? COLOR_DEFAULT_INCOME : COLOR_DEFAULT_EXPEND);
        invalidateView();
    }

    public void initData(int type, List<IncomeExpendInfo> infos) {
        if (infos == null || infos.size() == 0) {
            month = null;
            data = null;
            return;
        }
        month = new String[infos.size()];
        data = new float[infos.size()];
        for (int i = 0; i < infos.size(); i++) {
            IncomeExpendInfo info = infos.get(i);
            month[i] = info.month;
            if (type == TYPE_INCOME) {
                data[i] = Float.parseFloat(info.income);
            } else if (type == TYPE_EXPEND) {
                data[i] = Float.parseFloat(info.expend);
            }
        }
    }

}
