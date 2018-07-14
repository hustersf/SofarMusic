package com.sf.sofarmusic.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * 带动画效果的ImageView
 */
public class LoveAnimView extends AppCompatImageView {
  private boolean mVisble; // View是否可见
  private boolean mIsAnimStart; // 动画是否已经start
  private boolean mIsInScreen = true; // View是否在屏幕内

  private AnimatorSet mAnimSet;

  public LoveAnimView(Context context) {
    this(context, null);
  }

  public LoveAnimView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LoveAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAnim();
  }

  private void initAnim() {
    post(new Runnable() {
      @Override
      public void run() {
        pointAnim();
      }
    });
  }

  private void pointAnim() {

    Log.d("TAG",
        "left:" + getLeft() + " r:" + getRight() + " top:" + getTop() + " bo:" + getBottom());

    long duration = 1000;
    PointF p0 = new PointF(getLeft(), getTop());
    PointF p1 = new PointF(getLeft() - getWidth() / 2, getTop() - getHeight()/2);
    PointF p2 = new PointF(getLeft() - getWidth(), getTop() + getHeight());

    ValueAnimator pointAnim = new ValueAnimator().setDuration(duration);
    pointAnim.setRepeatCount(ValueAnimator.INFINITE);// 设置无限重复
    pointAnim.setRepeatMode(ValueAnimator.RESTART);// 设置重复模式
    pointAnim.setObjectValues(p0, p1, p2);
    pointAnim.setEvaluator(new PointFEvaluator());
    pointAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        PointF point = (PointF) animation.getAnimatedValue();
        setX(point.x);
        setY(point.y);
      }
    });

    ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(this, "alpha", 0.6f, 0);
    alphaAnim.setDuration(duration);
    alphaAnim.setRepeatCount(ValueAnimator.INFINITE);// 设置无限重复
    alphaAnim.setRepeatMode(ValueAnimator.RESTART);// 设置重复模式

    mAnimSet = new AnimatorSet();
    mAnimSet.playTogether(pointAnim, alphaAnim);
    mAnimSet.start();
  }

  // 该方法在当前View或其祖先的可见性改变时被调用
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

  // 该方法在包含当前View的window可见性改变时被调用。
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

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
  }

  // 恢复
  private void animatorResume() {
    post(new Runnable() {
      @Override
      public void run() {
        if (mIsAnimStart) {
          return;
        }
        mAnimSet.start();
        mIsAnimStart = true;
      }
    });
  }

  // 暂停
  private void animatorPause() {
    post(new Runnable() {
      @Override
      public void run() {
        if (!mIsAnimStart) {
          return;
        }
        mAnimSet.cancel();
        mIsAnimStart = false;
      }
    });
  }

  class PointFEvaluator implements TypeEvaluator<PointF> {

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
      PointF pointF = new PointF();
      pointF.x = startValue.x + fraction * (endValue.x - startValue.x);// x方向匀速移动
      pointF.y = startValue.y + fraction * fraction * (endValue.y - startValue.y);// y方向抛物线加速移动
      return pointF;
    }
  }


}
