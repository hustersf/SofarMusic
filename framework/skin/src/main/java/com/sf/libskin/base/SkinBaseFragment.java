package com.sf.libskin.base;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sf.libskin.attr.base.DynamicAttr;
import com.sf.libskin.listener.IDynamicNewView;
import com.trello.rxlifecycle2.components.support.RxFragment;



/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:35
 */
public class SkinBaseFragment extends RxFragment implements IDynamicNewView {

  private IDynamicNewView mIDynamicNewView;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    try {
      mIDynamicNewView = (IDynamicNewView) context;
    } catch (ClassCastException e) {
      mIDynamicNewView = null;
    }
  }

  @Override
  public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
    if (mIDynamicNewView == null) {
      throw new RuntimeException("IDynamicNewView should be implements !");
    } else {
      mIDynamicNewView.dynamicAddView(view, pDAttrs);
    }
  }

  @Override
  public void dynamicAddView(View view, String attrName, int attrValueResId) {
    mIDynamicNewView.dynamicAddView(view, attrName, attrValueResId);
  }

  @Override
  public void dynamicAddFontView(TextView textView) {
    mIDynamicNewView.dynamicAddFontView(textView);
  }

  public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
    LayoutInflater result = getActivity().getLayoutInflater();
    return result;
  }
}
