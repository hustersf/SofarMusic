package com.sf.demo.window.download;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by sufan on 17/8/16.
 */

public class CerProgress extends ProgressBar {

    private static final int REACH_COLOR = 0xFFE43026;     //已完成进度的颜色
    private static final int OUT_HEIGHT = 10;      //dp
    private static final int IN_HEIGHT = 6;      //dp
    private static final int WHITE_HEIGHT = 9;      //dp

    protected int mReachColor = REACH_COLOR;
    protected int mOutHeight = dp2px(OUT_HEIGHT);
    protected int mInHeight = dp2px(IN_HEIGHT);
    protected int mWhiteHeight = dp2px(WHITE_HEIGHT);

    private Paint mPaint;
    private int mRealWidth;      //进度条的真正长度


    public CerProgress(Context context) {
        this(context, null);
    }

    public CerProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CerProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);    //抗锯齿
        mPaint.setDither(true);       //抗抖动
        mPaint.setStrokeCap(Paint.Cap.ROUND);  //边缘为圆,只对画线有用
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRealWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int progressX = (int) (getProgress() * 1.0f / getMax() * mRealWidth);
        if(progressX<(mOutHeight+4)){
            progressX=(mOutHeight+4);
        }

        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        //画进度条外侧
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mOutHeight);
        canvas.drawLine(mOutHeight/2, 0, mRealWidth-mOutHeight/2, 0, mPaint);

        //画白色进度条(内侧背景)
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mWhiteHeight);
        canvas.drawLine(mOutHeight/2, 0, mRealWidth-mOutHeight/2, 0, mPaint);


        //画进度条内侧(即已经加载的进度)
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mInHeight);
        canvas.drawLine(mOutHeight/2, 0, progressX-mOutHeight/2, 0, mPaint);

        canvas.restore();
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
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
            int h = getPaddingTop() + getPaddingBottom() + Math.max(mOutHeight, mInHeight);
            result = h;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, h);
            }
        }
        return result;
    }

    private int dp2px(float v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, getResources().getDisplayMetrics());
    }

    private int sp2px(float v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, getResources().getDisplayMetrics());
    }
}
