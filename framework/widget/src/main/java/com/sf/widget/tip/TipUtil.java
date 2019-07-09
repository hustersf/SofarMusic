package com.sf.widget.tip;

import android.view.View;
import android.view.ViewGroup;

public class TipUtil {

  public static View showTip(View targetView, TipType tipType) {
    Tip tip = tipType.createTip(targetView.getContext());
    return tip.applyTo(targetView, tipType.ordinal());
  }

  public static View showTip(View targetView, TipType tipsType, boolean hideTarget) {
    Tip tip = tipsType.createTip(targetView.getContext(), hideTarget);
    return tip.applyTo(targetView, tipsType.ordinal());
  }

  public static void showTip(View targetView, View tipView) {
    new Tip(tipView).applyTo(targetView, tipView.hashCode());
  }

  public static void hideTip(View targetView, View tipView) {
    hideTip(targetView, tipView.getId());
  }

  public static void hideTip(View targetView, TipType... tipTypes) {
    if (targetView == null || tipTypes == null || tipTypes.length == 0) {
      return;
    }

    for (TipType tipType : tipTypes) {
      hideTip(targetView, tipType.ordinal());
    }
  }

  private static void hideTip(View targetView, int tipId) {
    ViewGroup tipContainer = (ViewGroup) targetView.getParent();
    if (!(tipContainer instanceof TipContainer)) {
      return;
    }

    View tipView = findChildViewById(tipContainer, tipId);
    hideTipInternal(targetView, tipView);
  }

  private static void hideTipInternal(View targetView, View tipView) {
    ViewGroup tipContainer = (ViewGroup) targetView.getParent();
    if (!(tipContainer instanceof TipContainer)) {
      return;
    }

    tipContainer.removeView(tipView);

    boolean hideTarget = false;
    for (int i = 0; i < tipContainer.getChildCount(); ++i) {
      Tip tip = (Tip) tipContainer.getChildAt(i).getTag();
      if (tip == null) {
        continue;
      }
      hideTarget = tip.mHideTarget;
      if (hideTarget) {
        break;
      }
    }
    targetView.setVisibility(hideTarget ? View.INVISIBLE : View.VISIBLE);

    if (tipContainer.getChildCount() == 1) {
      removeTipContainer(tipContainer, targetView);
    }
  }

  private static void removeTipContainer(ViewGroup tipContainer, View targetView) {
    ViewGroup parent = (ViewGroup) tipContainer.getParent();
    ViewGroup.LayoutParams targetParams = tipContainer.getLayoutParams();
    int index = parent.indexOfChild(tipContainer);
    parent.removeViewAt(index);
    if (targetView.getParent() != null) {
      ((ViewGroup) targetView.getParent()).removeView(targetView);
    }
    parent.addView(targetView, index, targetParams);
  }

  static View findChildViewById(ViewGroup parent, int id) {
    final int count = parent.getChildCount();
    for (int i = 0; i < count; ++i) {
      View child = parent.getChildAt(i);
      if (child.getId() == id) {
        return child;
      }
    }
    return null;
  }
}
