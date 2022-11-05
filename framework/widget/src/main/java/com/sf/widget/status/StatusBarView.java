package com.sf.widget.status;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.sf.utility.statusbar.StatusBarUtil;

public class StatusBarView extends View {
  public StatusBarView(Context context) {
    super(context);
  }

  public StatusBarView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public StatusBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int height = 0;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      height = StatusBarUtil.getStatusBarHeight(getContext());
    }
    setMeasuredDimension(widthMeasureSpec, height);
  }
}

