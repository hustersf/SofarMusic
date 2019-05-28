package com.sf.demo.bitmap.round;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.widget.ImageView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.utility.DensityUtil;

public class RoundLayoutActivity extends UIRootActivity {

  private ImageView imageView;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_round_layout;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("圆角布局");
  }

  @Override
  protected void initView() {
    imageView = findViewById(R.id.image);
  }

  @Override
  protected void initData() {
    ObjectAnimator animator =
        ObjectAnimator.ofFloat(imageView, "translationX", -DensityUtil.dp2px(this,80), DensityUtil.dp2px(this, 200));
    animator.setDuration(1000);
    animator.setRepeatMode(ValueAnimator.RESTART);
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.start();
  }

  @Override
  protected void initEvent() {

  }
}
