package com.sf.sofarmusic.demo.window.update;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by sufan on 16/8/3.
 */
public class HorizontalProgress extends ProgressBar {
    private static final int REACH_COLOR = 0xFFE43026;     //已完成进度的颜色
    private static final int UNREACH_COLOR = 0xFF999999;   //未完成进度的颜色
    private static final int REACH_HEIGHT = 5;      //dp,决定已完成ProgressBar的高度
    private static final int UNREACH_HEIGHT = 5;      //dp,决定未完成ProgressBar的高度
    private static final int TXET_COLOR = 0xFFE43026;      //字体颜色
    private static final int TEXT_SIZE = 12;        //字体大小，SP
    private static final int TEXT_OFFSET = 10;        //字和进度条的间距


    protected int mReachColor = REACH_COLOR;
    protected int mUnReachColor = UNREACH_COLOR;
    protected int mReachHeight = dp2px(REACH_HEIGHT);
    protected int mUnReachHeight = dp2px(UNREACH_HEIGHT);
    protected int mTextColor = TXET_COLOR;
    protected int mTextSize = sp2px(TEXT_SIZE);
    protected int mTextOffset = dp2px(TEXT_OFFSET);

    private Paint mPaint;
    private int mRealWidth;      //进度条的真正长度

    public HorizontalProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setAntiAlias(true);    //抗锯齿
        mPaint.setDither(true);       //抗抖动
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        mRealWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);

        boolean needUnReach = true;
        int progressX = (int) (getProgress() * 1.0f / getMax() * mRealWidth - mTextOffset / 2 - textWidth);
        if (progressX < 0) {
            progressX = 0;
        }

        if (progressX >= mRealWidth - mTextOffset / 2 - textWidth) {
            needUnReach = false;
        }
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        //画ReachLine
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        canvas.drawLine(0, 0, progressX, 0, mPaint);
        //画ReachLine

        //画text
        mPaint.setColor(mTextColor);
        canvas.drawText(text, progressX + mTextOffset / 2, -(mPaint.ascent() + mPaint.descent()) / 2, mPaint);
        //画text

        //画unReachLine
        if (needUnReach) {
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(progressX + textWidth + mTextOffset, 0, mRealWidth, 0, mPaint);
        }
        //画unReachLine
        canvas.restore();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

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
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            int h = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mReachHeight, mUnReachHeight), textHeight);
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
