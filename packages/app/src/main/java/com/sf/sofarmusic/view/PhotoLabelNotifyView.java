package com.sf.sofarmusic.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import com.sf.sofarmusic.util.DeviceUtil;


/**
 * 最小的时候的半径是宽高的1/3
 * 用于视频详情页中标签的提醒动画
 */
public class PhotoLabelNotifyView extends View {

    private int mRadius; // 圆的半径
    private Paint mPaint;
    private int mColor = 0xB8FF7F00; // argb 184,255,127,0,0xB8FF7F00
    private int mWidth;
    private int mHeight;
    private int mPaintWidth; // 画笔宽度

    private final float MAX_VALUE = 3.0f;
    private final float MID_VALUE = 1.0f;
    private final float MIN_VALUE = 0.8f;
    private float mValue = MID_VALUE;

    private ValueAnimator mAnimator;
    private long mCurrentPlayTime;
    private int[] mLocation = new int[2];
    private int mScreenHeight;

    private boolean mVisble;      //View是否可见
    private boolean mIsAnimStart; //动画是否已经start
    private boolean mIsInScreen;  //View是否在屏幕内

    public PhotoLabelNotifyView(Context context) {
        this(context, null);
    }

    public PhotoLabelNotifyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoLabelNotifyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        mRadius = dp2px(50);
        mPaintWidth = dp2px(4);

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setStyle(Paint.Style.FILL);

        mScreenHeight = DeviceUtil.getMetricsHeight(context);
        initAnim();

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                PhotoLabelNotifyView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                resumeOrPause();
            }
        });

        this.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                resumeOrPause();
            }
        });
    }

    private void resumeOrPause() {
        this.getLocationInWindow(mLocation);
        if (mLocation[1] < mScreenHeight) {
            mIsInScreen = true;
            animatorResume();
        } else {
            mIsInScreen = false;
            animatorPause();
        }
    }

    private void initAnim() {
        //动画初始化
        mAnimator = ValueAnimator.ofFloat(MID_VALUE, MIN_VALUE, MAX_VALUE);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);// 设置无限重复
        mAnimator.setRepeatMode(ValueAnimator.RESTART);// 设置重复模式
        mAnimator.setDuration(1200);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        mHeight = mWidth;
        mRadius = (int) (mWidth / 2 / MAX_VALUE);

        // alpha的初始值是184，不是255
        if (mValue > MID_VALUE) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(mPaintWidth);
            mPaint.setAlpha(184);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius * MID_VALUE, mPaint);

            mPaint.setStyle(Paint.Style.STROKE);
            // 1到3，width 1-0 ,线性关系 y=ax+b，基准是getWidth/2-mRadius*midValue
            int paintWidth = (int) ((3 - mValue) / 2 * (getWidth() / 2 - mRadius));
            mPaint.setStrokeWidth(paintWidth);
            // 1到3，alpha 1-0 ,线性关系 y=ax+b,基准是184
            int alpha = (int) ((3 - mValue) / 2 * 184);
            mPaint.setAlpha(alpha);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius * mValue, mPaint);
        } else {

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAlpha(184);
            mPaint.setStrokeWidth(mPaintWidth);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius * mValue, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 因为是圆，所以宽高相同
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(height, height);
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int h = (int) (MAX_VALUE * 2 * mRadius + getPaddingTop() + getPaddingBottom());
            result = h;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, h);
            }
        }
        return result;
    }


    //该方法在当前View或其祖先的可见性改变时被调用
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisble = visibility == VISIBLE ? true : false;
        if (mVisble && mIsInScreen) {
            animatorResume();
        } else {
            animatorPause();
        }
    }

    //该方法在包含当前View的window可见性改变时被调用。
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisble = visibility == VISIBLE ? true : false;
        if (mVisble && mIsInScreen) {
            animatorResume();
        } else {
            animatorPause();
        }
    }

    public void startAnimation() {
        animatorResume();
    }

    //恢复
    private void animatorResume() {
        if (mIsAnimStart) {
            return;
        }
        mAnimator.setCurrentPlayTime(mCurrentPlayTime);
        mAnimator.start();
        mIsAnimStart = true;
        Log.d("sufan", "animatorResume:" + mCurrentPlayTime);
    }

    // 暂停
    private void animatorPause() {
        if (!mIsAnimStart) {
            return;
        }
        mCurrentPlayTime = mAnimator.getCurrentPlayTime();
        mAnimator.cancel();
        mIsAnimStart = false;
        Log.d("sufan", "animatorPause:" + mCurrentPlayTime);
    }

    private int dp2px(float v) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v,
                getResources().getDisplayMetrics());
    }
}
