package com.sf.sofarmusic.view;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.sf.utility.LogUtil;

/**
 * 魔表icon动画，
 * 首页和拍摄界面
 */
public class MagicAnimImageView extends AppCompatImageView {

    private boolean mVisible; // View是否可见
    private long mCurrentPlayTime; // AnimatorSet设置当前播放时间，受限于api版本
    private boolean mIsAnimating = false;
    private boolean mIsStopAnim = true; // 默认不开启动画

    private @AnimType
    int mAnimType;
    private AnimatorSet mAnimSet;

    // home页动画所需参数
    private View mCameraButton; // CameraButton需要动画
    private int mAnim2Count = 3; // 第二阶段动画重复次数
    private int mStep = 1; // 当前动画处于第几阶段

    public MagicAnimImageView(Context context) {
        super(context);
    }

    public MagicAnimImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MagicAnimImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * @param type {@link AnimType}
     */
    public void setAnimType(@AnimType int type) {
        mAnimType = type;
        if (mAnimType == AnimType.HOME) {
            mAnimSet = getHomeAnim();
        } else if (mAnimType == AnimType.CAMERA) {
            mAnimSet = getCameraAnim();
        }
    }

    /**
     * @param view 首页的CameraButton
     */
    public void setCameraButton(@NonNull View view) {
        mCameraButton = view;
    }

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
     * 给外界一个开关，用来启动动画
     */
    public void startAnimation() {
        mIsStopAnim = false;
        animatorResume();
    }

    private void animatorResume() {
        if (mAnimSet == null || mIsAnimating || mIsStopAnim) {
            return;
        }
        mAnimSet.start();
        mIsAnimating = true;
        LogUtil.d("MagicAnimImageView", "animatorResume");
    }

    private void animatorPause() {
        if (mAnimSet == null || !mIsAnimating) {
            return;
        }
        mAnimSet.cancel();
        mIsAnimating = false;
        LogUtil.d("MagicAnimImageView", "animatorPause");
    }

    private AnimatorSet getHomeAnim() {
        AnimatorSet set = getChildHomeAnim1();
        return set;
    }

    // 第一阶段动画
    private AnimatorSet getChildHomeAnim1() {
        mStep = 1;
        mAnim2Count = 3;
        // 拍摄按钮的组合动画
        AnimatorSet cameraSet = new AnimatorSet();
        ObjectAnimator alphaAnim1 = ObjectAnimator.ofFloat(mCameraButton, "alpha", 1, 0.5f);
        ObjectAnimator scaleXAnim1 = ObjectAnimator.ofFloat(mCameraButton, "scaleX", 1, 0.8f);
        ObjectAnimator scaleYAnim1 = ObjectAnimator.ofFloat(mCameraButton, "scaleY", 1, 0.8f);
        cameraSet.playTogether(alphaAnim1, scaleXAnim1, scaleYAnim1);

        // 魔表组合动画
        AnimatorSet magicSet = new AnimatorSet();
        ObjectAnimator alphaAnim2 = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
        ObjectAnimator scaleXAnim2 = ObjectAnimator.ofFloat(this, "scaleX", 0.4f, 0.68f);
        ObjectAnimator scaleYAnim2 = ObjectAnimator.ofFloat(this, "scaleY", 0.4f, 0.68f);
        magicSet.playTogether(alphaAnim2, scaleXAnim2, scaleYAnim2);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(cameraSet, magicSet);
        set.setDuration(250);

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeRunnable();
                mCameraButton.setVisibility(GONE);
                MagicAnimImageView.this.postDelayed(mAnimRunnable, 1000);
            }
        });
        return set;
    }

    // 第二阶段动画
    private AnimatorSet getChildHomeAnim2() {
        mAnim2Count--;
        mStep = 2;
        ObjectAnimator scaleXAnim =
                ObjectAnimator.ofFloat(this, "scaleX", 0.68f, 0.74f, 0.68f);
        ObjectAnimator scaleYAnim =
                ObjectAnimator.ofFloat(this, "scaleY", 0.68f, 0.74f, 0.68f);
        scaleXAnim.setRepeatCount(1);
        scaleYAnim.setRepeatCount(1);
        scaleXAnim.setRepeatMode(ValueAnimator.RESTART);
        scaleYAnim.setRepeatMode(ValueAnimator.RESTART);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(340);
        set.playTogether(scaleXAnim, scaleYAnim);

        long delay = 2000;
        if (mAnim2Count == 0) {
            delay = 1000;
        }
        final long delayMillis = delay;

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeRunnable();
                MagicAnimImageView.this.postDelayed(mAnimRunnable, delayMillis);
            }
        });

        return set;
    }

    // 第三阶段动画
    private AnimatorSet getChildHomeAnim3() {
        mCameraButton.setVisibility(VISIBLE);
        mStep = 3;
        // 拍摄按钮的组合动画
        AnimatorSet cameraSet = new AnimatorSet();
        ObjectAnimator alphaAnim1 = ObjectAnimator.ofFloat(mCameraButton, "alpha", 0.5f, 1);
        ObjectAnimator scaleXAnim1 = ObjectAnimator.ofFloat(mCameraButton, "scaleX", 0.8f, 1);
        ObjectAnimator scaleYAnim1 = ObjectAnimator.ofFloat(mCameraButton, "scaleY", 0.8f, 1);
        cameraSet.playTogether(alphaAnim1, scaleXAnim1, scaleYAnim1);

        // 魔表组合动画
        AnimatorSet magicSet = new AnimatorSet();
        ObjectAnimator alphaAnim2 = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        ObjectAnimator scaleXAnim2 = ObjectAnimator.ofFloat(this, "scaleX", 0.68f, 0.4f);
        ObjectAnimator scaleYAnim2 = ObjectAnimator.ofFloat(this, "scaleY", 0.68f, 0.4f);
        magicSet.playTogether(alphaAnim2, scaleXAnim2, scaleYAnim2);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(cameraSet, magicSet);
        set.setDuration(250);

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeRunnable();
                MagicAnimImageView.this.postDelayed(mAnimRunnable, 3000);
            }
        });
        return set;
    }

    private AnimatorSet getCameraAnim() {
        // 设置魔表icon的动画效果
        AnimatorSet scaleSet = new AnimatorSet();
        ObjectAnimator scaleXAnim =
                ObjectAnimator.ofFloat(this, "scaleX", 1, 1.2f, 1);
        scaleXAnim.setRepeatCount(1);
        scaleXAnim.setRepeatMode(ValueAnimator.RESTART);
        scaleXAnim.setDuration(340);

        ObjectAnimator scaleYAnim =
                ObjectAnimator.ofFloat(this, "scaleY", 1, 1.2f, 1);
        scaleYAnim.setRepeatCount(1);
        scaleYAnim.setRepeatMode(ValueAnimator.RESTART);
        scaleYAnim.setDuration(340);

        scaleSet.playTogether(scaleXAnim, scaleYAnim);
        scaleSet.setStartDelay(1000);

        scaleSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeRunnable();
                MagicAnimImageView.this.postDelayed(mAnimRunnable, 2000);
            }
        });
        return scaleSet;
    }

    private void removeRunnable() {
        if (!mIsAnimating) {
            removeCallbacks(mAnimRunnable);
            return;
        }
        removeCallbacks(mAnimRunnable);
    }

    Runnable mAnimRunnable = new Runnable() {
        @Override
        public void run() {
            LogUtil.d("MagicAnimImageView", "postAnim");
            if (!mIsAnimating) {
                return;
            }
            if (mAnimType == AnimType.HOME) {
                switch (mStep) {
                    case 1:
                        getChildHomeAnim2().start();
                        break;
                    case 2:
                        if (mAnim2Count == 0) {
                            getChildHomeAnim3().start();
                        } else {
                            getChildHomeAnim2().start();
                        }
                        break;
                    case 3:
                        getChildHomeAnim1().start();
                        break;
                }
            } else if (mAnimType == AnimType.CAMERA) {
                mAnimSet.start();
            }
        }
    };


    @IntDef({AnimType.HOME, AnimType.CAMERA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimType {
        int HOME = 0; // 首页
        int CAMERA = 1; // 拍摄界面

    }
}
