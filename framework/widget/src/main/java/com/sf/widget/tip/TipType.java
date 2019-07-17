package com.sf.widget.tip;

import android.content.Context;

import com.sf.widget.R;

public enum TipType {

  LOADING(R.layout.tip_loading),
  LOADING_FAILED(R.layout.tip_loading_failed),
  EMPTY(R.layout.tip_empty);

  private int mLayoutRes;

  TipType(int layoutRes) {
    mLayoutRes = layoutRes;
  }

  public Tip createTip(Context context) {
    return new Tip(context, mLayoutRes);
  }

  public Tip createTip(Context context, boolean hideTarget) {
    return new Tip(context, mLayoutRes, hideTarget);
  }
}
