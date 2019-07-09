package com.sf.base.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.sf.base.util.FontUtil;

/**
 * 图标字体
 */
public class IconTextView extends AppCompatTextView {
  public IconTextView(Context context) {
    this(context, null);
  }

  public IconTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setTypeface(FontUtil.setIconFont(context));
  }
}
