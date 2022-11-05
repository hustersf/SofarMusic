package com.sf.demo.md.share_elemnt;

import android.view.View;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;

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
  }

  @Override
  protected void initData() {

  }

  @Override
  protected void initEvent() {


  }
}
