package com.sf.sofarmusic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ProgressBar;

import com.sf.sofarmusic.R;


/**
 * Created by sufan on 16/11/17.
 * 可以滑动的ProgressBar
 */

public class DoubleMusicProgress extends ProgressBar {
    private static final int F_REACH_COLOR = 0xFFFF0000;     //第一进度的颜色   //播放进度
    private static final int S_REACH_COLOR = 0xC8FFFFFF;     //第二进度的颜色  //下载进度
    private static final int UNREACH_COLOR = 0x7EFFFFFF;   //未完成进度的颜色
    private static final int CIRCLE_COLOR = 0xFFFFFFFF;   //圆圈的颜色
    private static final int F_REACH_HEIGHT = 2;      //dp,决定已完成ProgressBar的高度
    private static final int S_REACH_HEIGHT = 2;      //dp,决定已完成ProgressBar的高度
    private static final int UNREACH_HEIGHT = 2;      //dp,决定未完成ProgressBar的高度
    private static final int RADIUS = 7;      //dp,决定未完成ProgressBar的高度


    protected int mfReachColor = F_REACH_COLOR;
    protected int msReachColor = S_REACH_COLOR;
    protected int mUnreachColor = UNREACH_COLOR;
    protected int mCircleColor = CIRCLE_COLOR;
    protected int mfRechHeight = dp2px(F_REACH_HEIGHT);
    protected int msRechHeight = dp2px(S_REACH_HEIGHT);
    protected int mUnreachHeight = dp2px(UNREACH_HEIGHT);
    protected int mRadius = dp2px(RADIUS);

    private Paint mPaint;
    private int mRealWidth;      //进度条的真正长度

    private OnProgressListener mOnProgressListener;


    public DoubleMusicProgress(Context context) {
        this(context, null);
    }

    public DoubleMusicProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleMusicProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义的属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MusicProgress);

        mfReachColor = ta.getColor(R.styleable.DoubleMusicProgress_fReachColor, mfReachColor);
        msReachColor = ta.getColor(R.styleable.DoubleMusicProgress_sReachColor, msReachColor);
        mUnreachColor = ta.getColor(R.styleable.DoubleMusicProgress_dUnreachColor, mUnreachColor);
        mCircleColor = ta.getColor(R.styleable.DoubleMusicProgress_circleColor, mCircleColor);
        mfRechHeight = (int) (ta.getDimension(R.styleable.DoubleMusicProgress_fReachHeight, mfRechHeight));
        msRechHeight = (int) (ta.getDimension(R.styleable.DoubleMusicProgress_sReachHeight, msRechHeight));
        mUnreachHeight = (int) (ta.getDimension(R.styleable.DoubleMusicProgress_dUnreachHeight, mUnreachHeight));
        mRadius = (int) (ta.getDimension(R.styleable.DoubleMusicProgress_radius, mRadius));
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);    //抗锯齿
        mPaint.setDither(true);       //抗抖动
        mPaint.setStrokeCap(Paint.Cap.ROUND);  //边缘为圆
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        mRealWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int fProgressX = (int) (getProgress() * 1.0f / getMax() * mRealWidth);
        int sProgressX = (int) (getSecondaryProgress() * 1.0f / getMax() * mRealWidth);

        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        //画第一进度
        mPaint.setColor(mfReachColor);
        mPaint.setStrokeWidth(mfRechHeight);
        canvas.drawLine(0, 0, fProgressX, 0, mPaint);

        if (sProgressX > 0) {
            //画第二进度
            mPaint.setColor(msReachColor);
            mPaint.setStrokeWidth(mfRechHeight);
            canvas.drawLine(fProgressX, 0, sProgressX, 0, mPaint);

            //画unReachLine
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachHeight);
            canvas.drawLine(sProgressX, 0, mRealWidth, 0, mPaint);
        }else {
            //画unReachLine
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachHeight);
            canvas.drawLine(fProgressX, 0, mRealWidth, 0, mPaint);
        }


        //画圆圈
        mPaint.setColor(mCircleColor);
        canvas.drawCircle(fProgressX, 0, mRadius, mPaint);
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
            int h = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mfRechHeight, msRechHeight),
                    Math.max(2 * mRadius, mUnreachHeight));
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

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        mOnProgressListener = onProgressListener;
    }


    public interface OnProgressListener {
        void onProgress(int progress);
    }

}
