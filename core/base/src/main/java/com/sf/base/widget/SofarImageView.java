package com.sf.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.sf.widget.round.RoundImageView;

public class SofarImageView extends RoundImageView {

  public SofarImageView(Context context) {
    super(context);
  }

  public SofarImageView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public SofarImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void bindUrl(String url) {
    Glide.with(getContext()).load(url).into(this);
  }
}
