package com.sf.sofarmusic.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.sf.utility.DensityUtil;
import com.sf.utility.LogUtil;

/**
 * 用于替换主页左边头像按钮原来的帧动画效果
 */
public class KoinAvatarView extends ImageView {

    private int mRadius;
    private Paint mPaint;
    private RectF mRectF;
    private int mColor = 0xFFFFFFFF;
    private int mWidth;
    private int mHeight;
    private int mPaintWidth; // 画笔宽度
    private Context mContext;

    private final float MIN_VALUE = 0.6f;
    private final float MAX_VALUE = 1.0f;//
    private final float MID_VALUE = 0.8f;
    private float mValue = MIN_VALUE; // 当前value值，在min和max之间,用于scale

    private ValueAnimator mAnim;
    private boolean mIsAnimating = false;
    private boolean mVisible; // View是否可见
    private long mCurrentPlayTime;
    private boolean mIsStopAnim = true; // 默认不开启动画

    private SweepGradient mSweepGradient;
    private int[] mColors = new int[]{0xffeab748, 0xffe6b44d, 0xffe39447, 0xffe39447, 0xffeab748};


    public KoinAvatarView(Context context) {
        this(context, null);
    }

    public KoinAvatarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KoinAvatarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaintWidth = DensityUtil.dp2px(mContext, 4);
        mRadius = DensityUtil.dp2px(mContext, 50);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mRectF = new RectF();

        initAnim();
    }

    private void initAnim() {
        mAnim = ValueAnimator.ofFloat(MIN_VALUE, MID_VALUE, MAX_VALUE);
        mAnim.setRepeatCount(ValueAnimator.INFINITE);// 设置无限重复
        mAnim.setRepeatMode(ValueAnimator.RESTART);// 设置重复模式
        mAnim.setDuration(1008);
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mValue = (Float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mIsAnimating) {
            return;
        }
        mWidth = getWidth();
        mHeight = mWidth;
        mRadius = mWidth / 2;

        if (mSweepGradient == null) {
            mSweepGradient = new SweepGradient(mRadius, mRadius, mColors, null);
            mPaint.setShader(mSweepGradient);
        }

        // value从0.6到0.8，paintWidth从0.6到1,对应值依据minValue,midValue,maxValue
        mPaintWidth = (int) (getHeight() / 20 * (2 * mValue - 0.6));
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setAlpha(255);
        mRectF.left = mPaintWidth / 2 + mRadius * (MAX_VALUE - mValue);
        mRectF.top = mPaintWidth / 2 + mRadius * (MAX_VALUE - mValue);
        mRectF.right = mWidth - mPaintWidth / 2 - mRadius * (MAX_VALUE - mValue);
        mRectF.bottom = mHeight - mPaintWidth / 2 - mRadius * (MAX_VALUE - mValue);

        if ((float) Math.round(mValue * 100) / 100 >= MID_VALUE) {
            // value从0.8到1，paintWidth从1到0.6，aplpha从255到0
            mPaintWidth = (int) (getHeight() / 20 * (2.6 - 2 * mValue));
            int alpha = (int) (255 * (5 - 5 * mValue));
            if (alpha > 255) {
                alpha = 255;
            }
            LogUtil.d("KoinAvatarView", "alpha:" + alpha + " value:" + mValue);
            mPaint.setStrokeWidth(mPaintWidth);
            mPaint.setAlpha(alpha);
        }
        canvas.drawArc(mRectF, 0, 360, true, mPaint);
    }


    /**
     * 该方法在当前View或其祖先的可见性改变时被调用
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisible = visibility == VISIBLE ? true : false;
        if (mVisible) {
            animatorResume();
        } else {
            animatorPause();
        }
    }


    /**
     * 该方法在包含当前View的window可见性改变时被调用
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == VISIBLE ? true : false;
        if (mVisible) {
            animatorResume();
        } else {
            animatorPause();
        }
    }

    /**
     * 启动动画
     */
    public void startAnimation() {
        mIsStopAnim = false;
        animatorResume();
    }

    /**
     * 终止动画
     */
    public void stopAnimation() {
        mIsStopAnim = true;
        animatorPause();
    }


    private void animatorResume() {
        if (mIsAnimating || mIsStopAnim) {
            return;
        }
        mAnim.setCurrentPlayTime(mCurrentPlayTime);
        mAnim.start();
        mIsAnimating = true;
        LogUtil.d("KoinAvatarView", "animatorResume:" + mCurrentPlayTime);
    }

    private void animatorPause() {
        if (!mIsAnimating) {
            return;
        }
        mCurrentPlayTime = mAnim.getCurrentPlayTime();
        mAnim.cancel();
        mIsAnimating = false;
        LogUtil.d("KoinAvatarView", "animatorPause:" + mCurrentPlayTime);
    }
}
