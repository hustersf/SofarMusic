package com.sf.libskin.base;

import java.util.List;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sf.libskin.attr.base.DynamicAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.listener.IDynamicNewView;
import com.sf.libskin.listener.ISkinUpdate;
import com.sf.libskin.loader.SkinInflaterFactory;
import com.sf.libskin.loader.SkinManager;
import com.sf.utility.statusbar.StatusBarUtil;
import com.sf.libskin.utils.SkinL;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:24
 * 需要实现换肤功能的Activity就需要继承于这个Activity
 */
public class SkinBaseActivity extends RxAppCompatActivity implements ISkinUpdate, IDynamicNewView {

  private SkinInflaterFactory mSkinInflaterFactory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    mSkinInflaterFactory = new SkinInflaterFactory();
    mSkinInflaterFactory.setAppCompatActivity(this);
    LayoutInflaterCompat.setFactory(getLayoutInflater(), mSkinInflaterFactory);
    super.onCreate(savedInstanceState);

    StatusBarUtil statusBarUtil = new StatusBarUtil(this);
    statusBarUtil.setStatusBarTransparent();
  }

  @Override
  protected void onResume() {
    super.onResume();
    SkinManager.getInstance().attach(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    SkinManager.getInstance().detach(this);
    mSkinInflaterFactory.clean();
  }

  @Override
  public void onThemeUpdate() {
    Log.i("SkinBaseActivity", "onThemeUpdate");
    mSkinInflaterFactory.applySkin();
  }


  @Override
  public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
    mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
  }

  @Override
  public void dynamicAddView(View view, String attrName, int attrValueResId) {
    mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
  }

  @Override
  public void dynamicAddFontView(TextView textView) {
    mSkinInflaterFactory.dynamicAddFontEnableView(textView);
  }

}
