package com.sf.sofarmusic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sufan on 16/11/9.
 */

public class HorizontalColorBar extends View implements LinkColorBar.OnColorAdjustListener{
    private Paint mPaint;
    private int mPaintWidth = 4;   //画笔宽度


    private Shader mShader;
    private int mBarHeight = 10;   //bar的高度，长度等于圆的一半
    private boolean isFirst = true;

    private static final int[] COLORS = new int[]{0xFFFF0000, 0xFFFF00FF,
            0xFF0000FF, 0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000};

    //圆圈的颜色
    private int mCircleEdgeColor = 0xFF999999;
    private int mFillColor = 0xFFFFFFFF;

    private int mCurrentX;  //当前滑块的X值
    private int mCurrentColor;
    private int mLeft, mRight;  //限制滑块滑动的区间
    private int mRealWidth;
    private OnColorChangeListener mOnColorChangeListener;
    private LinkColorBar mLinkColorBar;

    public HorizontalColorBar(Context context) {
        this(context, null);
    }

    public HorizontalColorBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalColorBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBarHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBarHeight, getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setAntiAlias(true);    //抗锯齿
        mPaint.setDither(true);       //抗抖动
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);   //使得边缘为圆弧，但是画矩形没效果


    }

    @Override
    protected void onDraw(Canvas canvas) {

        int realWitdh = getWidth() - getPaddingLeft() - getPaddingRight();  //横条真正的长度
        int realHeigh = getHeight() - getPaddingTop() - getPaddingBottom();

        mRealWidth = realWitdh;

        if (isFirst) {
            mCurrentX = realWitdh / 2;
            if(mLinkColorBar!=null){
                mLinkColorBar.setNewColor(getCurrentColor());
            }
        }

        mLeft = mBarHeight / 2 + getPaddingLeft();
        mRight = getWidth() - getPaddingRight() - mBarHeight / 2;

        //画颜色条
        int x1 = mBarHeight / 2 + getPaddingLeft();
        int x2 = getWidth() - getPaddingRight() - mBarHeight / 2;
        mPaint.setStrokeWidth(mBarHeight);
        mPaint.setShader(mShader);
        canvas.drawLine(x1, getHeight() / 2, x2, getHeight() / 2, mPaint);

        //画移动圈圈
        mPaint.setShader(null);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setColor(mCircleEdgeColor);
        canvas.drawCircle(mCurrentX, getHeight() / 2, mBarHeight, mPaint);
        mPaint.setColor(mFillColor);
        canvas.drawCircle(mCurrentX, getHeight() / 2, mBarHeight - mPaintWidth / 2, mPaint);

        isFirst = false;

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthValue = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthValue, height);

        int x1 = 0;
        int x2 = widthValue - getPaddingLeft() - getPaddingRight();
        mShader = new LinearGradient(x1, 0, x2, 0, COLORS, null, Shader.TileMode.CLAMP);
        mPaint.setShader(mShader);

    }

    private int measureHeight(int heightMeasureSpec) {

        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);


        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int h = getPaddingTop() + getPaddingBottom() + mBarHeight * 2;
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
                if (x >= mLeft && x <= mRight) {
                    mCurrentX = x;
                    invalidate();
                    if (mOnColorChangeListener != null) {
                        mOnColorChangeListener.onColorBar(getCurrentColor());
                        if(mLinkColorBar!=null){
                            mLinkColorBar.setNewColor(getCurrentColor());
                        }
                   }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    public void onAdjustColor(int color) {
        if(mOnColorChangeListener!=null){
            mOnColorChangeListener.onColorBar(color);
            mCurrentColor=color;
        }

    }

    public interface OnColorChangeListener {
        void onColorBar(int color);
    }

    public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener) {
        mOnColorChangeListener = onColorChangeListener;
    }

    public void add(LinkColorBar linkColorBar){
        mLinkColorBar=linkColorBar;
        mLinkColorBar.setOnColorAdjustListener(this);
    }

    private int getCurrentColor() {
        int unit = mRealWidth / (COLORS.length - 1);
        int position = mCurrentX - mBarHeight;
        int i = position / unit;
        int step = position % unit;
        if (i >= COLORS.length - 1) return COLORS[COLORS.length - 1];
        int c0 = COLORS[i];
        int c1 = COLORS[i + 1];

        int a = ave(Color.alpha(c0), Color.alpha(c1), unit, step);
        int r = ave(Color.red(c0), Color.red(c1), unit, step);
        int g = ave(Color.green(c0), Color.green(c1), unit, step);
        int b = ave(Color.blue(c0), Color.blue(c1), unit, step);

        return Color.argb(a, r, g, b);
    }

    private int ave(int s, int t, int unit, int step) {
        return s + (t - s) * step / unit;
    }
}
