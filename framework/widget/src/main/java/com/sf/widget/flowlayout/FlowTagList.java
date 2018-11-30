package com.sf.widget.flowlayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sf.utility.DensityUtil;
import com.sf.widget.R;

/**
 * Created by sufan on 16/8/13.
 */
public class FlowTagList extends FlowLayout {
  private String[] mTags;
  private boolean[] mIsChecked;

  private OnTagCheckListener mOnTagCheckListener;
  private OnTagClickListener mOnTagClickListener;

  private int mColor = 0xFF3DDAE7;
  private int mStrokeWidth;

  public FlowTagList(Context context) {
    this(context, null);
  }

  public FlowTagList(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public FlowTagList(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mStrokeWidth = DensityUtil.dp2px(context, 1);
  }


  public void setTags(String[] tags) {
    mTags = tags;
    mIsChecked = new boolean[mTags.length];
    for (int i = 0; i < mTags.length; i++) {
      mIsChecked[i] = false;
    }
    refrsehView();
  }

  private void refrsehView() {
    removeAllViews();
    LayoutInflater inflater = LayoutInflater.from(getContext());
    for (int i = 0; i < mTags.length; i++) {
      TextView tv = (TextView) inflater.inflate(R.layout.layout_flow_tag, this, false);
      if (mIsChecked[i]) {
        tv.setBackgroundResource(R.drawable.tag_bg_press);
        GradientDrawable gradientDrawable = (GradientDrawable) tv.getBackground();
        gradientDrawable.setColor(mColor);
        gradientDrawable.setStroke(mStrokeWidth, mColor);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
      } else {
        tv.setBackgroundResource(R.drawable.tag_bg);
        GradientDrawable gradientDrawable = (GradientDrawable) tv.getBackground();
        gradientDrawable.setStroke(mStrokeWidth, mColor);
        tv.setTextColor(mColor);
      }
      tv.setText(mTags[i]);
      addView(tv);
      initEvent(tv, i);
    }
  }

  private void initEvent(final TextView tv, final int position) {
    tv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnTagClickListener != null) {
          mOnTagClickListener.OnTagClick(tv.getText().toString(), position);
        }

      }
    });
  }

  public void setOnTagCheckListener(OnTagCheckListener onTagCheckListener) {
    mOnTagCheckListener = onTagCheckListener;
  }

  public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
    mOnTagClickListener = onTagClickListener;
  }

  public boolean isChecked(int position) {

    return mIsChecked[position];
  }

  public void setChecked(boolean flag, int position) {
    mIsChecked[position] = flag;
  }


  public interface OnTagClickListener {
    void OnTagClick(String text, int position);
  }

  public interface OnTagCheckListener {

  }

  public void notifyAllTag() {
    refrsehView();
  }

  public void setColor(int color) {
    mColor = color;
  }


}
