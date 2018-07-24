package com.sf.demo.viewpager.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sf.demo.R;
import com.sf.utility.DensityUtil;


/**
 *
 */

public class BannerIndicator extends LinearLayout implements LoopVPAdapter.BannerIndicator {

  private int count;

  public BannerIndicator(Context context) {
    this(context, null);
  }

  public BannerIndicator(Context context, AttributeSet attrs) {
    super(context, attrs);
  }


  @Override
  public void InitIndicatorItems(int itemsNumber) {
    count = itemsNumber;
    removeAllViews();
    for (int i = 0; i < itemsNumber; i++) {
      ImageView imageView = new ImageView(getContext());
      int width = DensityUtil.dp2px(getContext(), 12);
      int height = DensityUtil.dp2px(getContext(), 3);
      LayoutParams lp = new LayoutParams(width, height);
      lp.leftMargin = DensityUtil.dp2px(getContext(), 2);
      lp.rightMargin = DensityUtil.dp2px(getContext(), 2);
      imageView.setLayoutParams(lp);
      imageView.setImageResource(R.drawable.banner_indicator_unselected);
      addView(imageView);
    }
  }

  @Override
  public void setIndicator(int position) {
    ImageView imageView = null;
    for (int i = 0; i < count; i++) {
      imageView = (ImageView) getChildAt(i);
      if (i == position) {
        imageView.setImageResource(R.drawable.banner_indicator_selected);
      } else {
        imageView.setImageResource(R.drawable.banner_indicator_unselected);
      }
    }
  }


}
