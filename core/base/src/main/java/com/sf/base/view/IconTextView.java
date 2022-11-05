package com.sf.base.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

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
