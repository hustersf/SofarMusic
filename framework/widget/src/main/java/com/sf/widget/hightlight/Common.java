package com.sf.widget.hightlight;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sufan
 */
class Common {
  /**
   * 设置Component
   */
  static View componentToView(LayoutInflater inflater, Component c) {
    View view = c.getView(inflater);
    final MaskView.LayoutParams lp = new MaskView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.offsetX = c.getXOffset();
    lp.offsetY = c.getYOffset();
    lp.targetAnchor = c.getAnchor();
    lp.targetParentPosition = c.getFitPosition();
    view.setLayoutParams(lp);
    return view;
  }

  /**
   * 获取目标View的区域
   */
  static Rect getViewAbsRect(View view, int parentX, int parentY) {
    int[] loc = new int[2];
    view.getLocationInWindow(loc);
    Rect rect = new Rect();
    rect.set(loc[0], loc[1], loc[0] + view.getMeasuredWidth(), loc[1] + view.getMeasuredHeight());
    rect.offset(-parentX, -parentY);
    return rect;
  }
}
