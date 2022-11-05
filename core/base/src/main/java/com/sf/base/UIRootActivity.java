package com.sf.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sf.base.util.FontUtil;

/**
 * Created by sufan on 2018/4/18.
 * 将公共的UI抽出来
 * 标题栏，统一app的标题栏
 */
public abstract class UIRootActivity extends BaseActivity {

  // 公共标题
  protected TextView mHeadBackTv, mHeadTitleTv, mHeadRightTv;
  protected Toolbar mToolbar;
  private LinearLayout mRootLayout;


  protected abstract int getLayoutId();

  protected abstract void initTitle();

  protected abstract void initView();

  protected abstract void initData();

  protected abstract void initEvent();


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ui_root);
    mRootLayout = findViewById(R.id.ll_root);
    int subRootViewId = getLayoutId();
    if (subRootViewId > 0) {
      getLayoutInflater().inflate(subRootViewId, mRootLayout);
    }

    setTitle();
    initTitle();
    initView();
    initData();
    initEvent();
  }



  private void setTitle() {
    mHeadBackTv = findViewById(R.id.head_back);
    mHeadTitleTv = findViewById(R.id.head_title);
    mHeadRightTv = findViewById(R.id.head_right);
    mToolbar = findViewById(R.id.toolbar);
    dynamicAddView(mToolbar, "background", R.color.head_title_bg_color);
    Typeface iconFont = FontUtil.setIconFont(this);
    mHeadBackTv.setTypeface(iconFont);
    mHeadBackTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }
}
