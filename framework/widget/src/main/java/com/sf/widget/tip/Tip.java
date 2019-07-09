package com.sf.widget.tip;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sf.utility.ViewUtil;

public class Tip {

  public final View mView;
  public final boolean mHideTarget;
  private final FrameLayout.LayoutParams mLayoutParams;

  public Tip(View view) {
    this(view, true);
  }


  public Tip(Context context, int layoutRes) {
    this(context, layoutRes, true);
  }

  public Tip(Context context, int layoutRes, boolean hideTarget) {
    this(ViewUtil.inflate(new FrameLayout(context), layoutRes), hideTarget);
  }

  public Tip(View view, boolean hideTarget) {
    mHideTarget = hideTarget;
    mView = view;
    // add tag to this view.
    mView.setTag(this);
    ViewGroup.LayoutParams lp = view.getLayoutParams();
    if (lp instanceof FrameLayout.LayoutParams) {
      mLayoutParams = (FrameLayout.LayoutParams) lp;
    } else {
      mLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT);
    }
  }

  public View applyTo(View target, int tipId) {
    ViewGroup parent = (ViewGroup) target.getParent();
    if (parent == null) {
      return null;
    }

    View tipView;
    if (parent instanceof TipContainer) {
      tipView = addTipViewToContainer(target, parent, tipId);
    } else {
      TipContainer tipContainer = new TipContainer(target.getContext());
      ViewGroup.LayoutParams targetParams = target.getLayoutParams();
      int index = parent.indexOfChild(target);
      parent.removeViewAt(index);
      parent.addView(tipContainer, index, targetParams);
      Drawable background = target.getBackground();
      if (background != null) {
        tipContainer.setBackground(background);
      }
      tipView = addTipViewToContainer(target, tipContainer, tipId);
    }
    return tipView;
  }

  private View addTipViewToContainer(View target, ViewGroup tipContainer, int tipId) {
    View tipView = TipUtil.findChildViewById(tipContainer, tipId);
    if (tipView != null) {
      tipView.bringToFront();
      return tipView;
    } else {
      mView.setId(tipId);
      FrameLayout.LayoutParams targetViewLayoutParams = new FrameLayout.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      if (mHideTarget) {
        target.setVisibility(View.INVISIBLE);
      }
      if (tipContainer.indexOfChild(target) == -1) {
        tipContainer.addView(target, targetViewLayoutParams);
      } else {
        target.setLayoutParams(targetViewLayoutParams);
      }
      tipContainer.addView(mView, mLayoutParams);
      return mView;
    }
  }

}
