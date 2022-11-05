package com.sf.sofarmusic.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.sf.sofarmusic.menu.profile.SpringScaleInterpolator;


/**
 * 带动画效果的ImageView
 */
public class ExpectAnimView extends AppCompatImageView {
  private boolean mVisble; // View是否可见
  private boolean mIsAnimStart; // 动画是否已经start
  private boolean mIsInScreen = true; // View是否在屏幕内

  private AnimatorSet mAnimSet;

  public ExpectAnimView(Context context) {
    this(context, null);
  }

  public ExpectAnimView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ExpectAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAnim();
  }

  private void initAnim() {
    // 动画初始化
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1, 1.1f);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1, 1.1f);
    scaleX.setRepeatCount(ValueAnimator.INFINITE);// 设置无限重复
    scaleX.setRepeatMode(ValueAnimator.REVERSE);// 设置重复模式
    scaleY.setRepeatCount(ValueAnimator.INFINITE);// 设置无限重复
    scaleY.setRepeatMode(ValueAnimator.REVERSE);// 设置重复模式
    mAnimSet = new AnimatorSet();
    mAnimSet.setDuration(1000);
    mAnimSet.setInterpolator(new SpringScaleInterpolator(0.4f));
    mAnimSet.playTogether(scaleX, scaleY);
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
    if (mIsAnimStart) {
      return;
    }
    mAnimSet.start();
    mIsAnimStart = true;
  }

  // 暂停
  private void animatorPause() {
    if (!mIsAnimStart) {
      return;
    }
    mAnimSet.cancel();
    mIsAnimStart = false;
  }
}
