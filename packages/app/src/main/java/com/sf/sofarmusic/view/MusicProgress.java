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
 */

public class MusicProgress extends ProgressBar {
    private static final int REACH_COLOR = 0xFFFF1493;     //已完成进度的颜色
    private static final int UNREACH_COLOR = 0x90FF1493;   //未完成进度的颜色
    private static final int REACH_HEIGHT = 5;      //dp,决定已完成ProgressBar的高度
    private static final int UNREACH_HEIGHT = 5;      //dp,决定未完成ProgressBar的高度

    protected int mReachColor = REACH_COLOR;
    protected int mUnreachColor = UNREACH_COLOR;
    protected int mRechHeight = dp2px(REACH_HEIGHT);
    protected int mUnreachHeight = dp2px(UNREACH_HEIGHT);
    protected boolean mRoundConner;

    private Paint mPaint;
    private int mRealWidth;      //进度条的真正长度


    public MusicProgress(Context context) {
        this(context, null);
    }

    public MusicProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义的属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MusicProgress);

        mReachColor = ta.getColor(R.styleable.MusicProgress_reachColor, mReachColor);
        mUnreachColor = ta.getColor(R.styleable.MusicProgress_unreachColor, mUnreachColor);
        mRechHeight = (int) (ta.getDimension(R.styleable.MusicProgress_reachHeight, mRechHeight));
        mUnreachHeight = (int) (ta.getDimension(R.styleable.MusicProgress_unreachHeight, mUnreachHeight));
        mRoundConner=ta.getBoolean(R.styleable.MusicProgress_roundconner,false);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);    //抗锯齿
        mPaint.setDither(true);       //抗抖动
        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        mRealWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int progressX = (int) (getProgress() * 1.0f / getMax() * mRealWidth);

        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        if(mRoundConner){
            mPaint.setStrokeCap(Paint.Cap.ROUND);  //边缘为圆
        }

        //画ReachLine
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mRechHeight);
        canvas.drawLine(0, 0, progressX, 0, mPaint);

        //画unReachLine
        mPaint.setColor(mUnreachColor);
        mPaint.setStrokeWidth(mUnreachHeight);
        canvas.drawLine(progressX, 0, mRealWidth, 0, mPaint);


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
            int h = getPaddingTop() + getPaddingBottom() + Math.max(mRechHeight, mUnreachHeight);
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

    public void setReachColor(int color){
        mReachColor=color;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
