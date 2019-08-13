package com.sf.demo.widget.round;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.widget.round.RoundImageView;

public class RoundActivity extends UIRootActivity {

  private RoundImageView image1;
  private RoundImageView image2;
  private RoundImageView image3;
  private RoundImageView image4;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_bitmap_round;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("圆角图片");
  }

  @Override
  protected void initView() {
    image1 = findViewById(R.id.image1);
    image2 = findViewById(R.id.image2);
    image3 = findViewById(R.id.image3);
    image4 = findViewById(R.id.image4);
  }

  @Override
  protected void initData() {
  }

  @Override
  protected void initEvent() {

  }
}
