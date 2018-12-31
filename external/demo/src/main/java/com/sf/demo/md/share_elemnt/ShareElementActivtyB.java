package com.sf.demo.md.share_elemnt;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.widget.swipe.SwipeBack;

public class ShareElementActivtyB extends UIRootActivity {
  ImageView iv_share;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_share_element_b;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("共享元素");
    mToolbar.setVisibility(View.GONE);
  }


  @Override
  protected void initView() {
    iv_share = findViewById(R.id.iv_share);
    ViewCompat.setTransitionName(iv_share, "share_element");
    SwipeBack.attach(this, new SwipeBack.SwipebackFunction() {
      @Override
      public void finish() {
        onBackPressed(); // 注意finsh和onBackPressed的区别
      }
    });

  }

  @Override
  protected void initData() {

  }

  @Override
  protected void initEvent() {


  }
}
