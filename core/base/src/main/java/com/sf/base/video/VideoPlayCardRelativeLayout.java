package com.sf.base.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 保证宽高比 375/211
 */
public class VideoPlayCardRelativeLayout extends RelativeLayout {
  private boolean mFixSize = true;

  public VideoPlayCardRelativeLayout(Context context) {
    super(context);
  }

  public VideoPlayCardRelativeLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public VideoPlayCardRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setEnableFixSizeRatio(boolean fixSize) {
    mFixSize = fixSize;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (mFixSize) {
      int width = MeasureSpec.getSize(widthMeasureSpec);
      super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
          MeasureSpec.makeMeasureSpec((int) ((double) width / 375 * 211), MeasureSpec.EXACTLY));
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }
}
