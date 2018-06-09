package com.sf.sofarmusic.menu.poweroff;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ProgressBar;

import com.sf.sofarmusic.R;

/**
 * Created by sufan on 17/10/4.
 */

public class TimeProgress extends ProgressBar {
    private static final int REACH_COLOR = 0xFFFF1493;     //已完成进度的颜色
    private static final int UNREACH_COLOR = 0xFFC0C8CC;   //未完成进度的颜色
    private static final int REACH_HEIGHT = 8;      //dp,决定已完成ProgressBar的高度
    private static final int UNREACH_HEIGHT = 8;      //dp,决定未完成ProgressBar的高度
    private static final int RADIUS = 15;      //dp,决定未完成ProgressBar的高度

    protected int mReachColor = REACH_COLOR;
    protected int mUnReachColor = UNREACH_COLOR;
    protected int mReachHeight = dp2px(REACH_HEIGHT);
    protected int mUnReachHeight = dp2px(UNREACH_HEIGHT);
    protected int mRadius = dp2px(RADIUS);

    private Paint mPaint;
    private Paint mAlphaCirclePaint;
    private int mRealWidth;      //进度条的真正长度
    protected boolean mRoundConner;
    private boolean isTouch;

    private OnProgressListener mOnProgressListener;


    public TimeProgress(Context context) {
        this(context, null);
    }

    public TimeProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义的属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TimeProgress);

        mReachColor = ta.getColor(R.styleable.TimeProgress_timeReachColor, mReachColor);
        ta.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);    //抗锯齿
        mPaint.setDither(true);       //抗抖动
        mPaint.setStyle(Paint.Style.FILL);

        mAlphaCirclePaint = new Paint();
        mAlphaCirclePaint.setAntiAlias(true);
        mAlphaCirclePaint.setStyle(Paint.Style.FILL);
        mAlphaCirclePaint.setStrokeWidth(1);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        mRealWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int progressX = (int) (getProgress() * 1.0f / getMax() * mRealWidth);

        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        if (mRoundConner) {
            mPaint.setStrokeCap(Paint.Cap.ROUND);  //边缘为圆
        }

        //画ReachLine
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        canvas.drawLine(0, 0, progressX, 0, mPaint);

        //画unReachLine
        mPaint.setColor(mUnReachColor);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawLine(progressX, 0, mRealWidth, 0, mPaint);

        //画圆圈
        if (isTouch) {
            mPaint.setColor(Color.parseColor("#E2E2E2"));
        } else {
            mPaint.setColor(Color.parseColor("#FFFFFF"));
        }
        if (progressX < mRadius) {
            progressX = mRadius - dp2px(2);
        }
        if (progressX > mRealWidth - mRadius) {
            progressX = mRealWidth - mRadius + dp2px(2);
        }
        canvas.drawCircle(progressX, 0, mRadius, mPaint);   //最外层圆
        mAlphaCirclePaint.setColor(mReachColor);
        mAlphaCirclePaint.setAlpha(125);  //必须放在setColor后面
        canvas.drawCircle(progressX, 0, mRadius - dp2px(2), mAlphaCirclePaint);   //第二层圆
        mPaint.setColor(mReachColor);
        canvas.drawCircle(progressX, 0, mRadius / 2, mPaint);   //最里层圆

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
            int h = getPaddingTop() + getPaddingBottom() + Math.max(2 * mRadius, Math.max(mReachHeight, mUnReachHeight));
            result = h;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, h);
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (x < 0) {
                    x = 0;
                } else if (x > mRealWidth) {
                    x = mRealWidth;
                }
                int progress = (int) ((x * 1.0f) / mRealWidth * 100);
                setProgress(progress);
                if (mOnProgressListener != null) {
                    mOnProgressListener.onProgress(getProgress());
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                invalidate();
                break;
        }
        return true;
    }

    private int dp2px(float v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, getResources().getDisplayMetrics());
    }

    private int sp2px(float v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, getResources().getDisplayMetrics());
    }

    public void setReachColor(int color) {
        mReachColor = color;
        invalidate();
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        mOnProgressListener = onProgressListener;
    }


    public interface OnProgressListener {
        void onProgress(int progress);
    }
}
