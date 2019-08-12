package com.sf.widget.flowlayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.sf.utility.DensityUtil;
import com.sf.widget.R;

/**
 * tag标签布局样例
 * 如果想改变样式，可参考此类替换其中的一些资源文件即可
 */
public class FlowTagList extends FlowLayout {
  private String[] mTags;
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

  /**
   * 设置tag数据源
   * 
   * @param tags
   */
  public void setTags(String[] tags) {
    mTags = tags;
    refreshView();
  }

  private void refreshView() {
    removeAllViews();
    LayoutInflater inflater = LayoutInflater.from(getContext());
    for (int i = 0; i < mTags.length; i++) {
      TextView tv = (TextView) inflater.inflate(R.layout.layout_flow_tag, this, false);

      updateDrawable(tv);
      updateTextColor(tv);

      tv.setText(mTags[i]);
      addView(tv);
      initEvent(tv, i);
    }
  }

  private void updateDrawable(TextView tv) {
    GradientDrawable normalDrawable =
        (GradientDrawable) getResources().getDrawable(R.drawable.tag_bg_normal);
    normalDrawable.setColor(Color.WHITE);
    normalDrawable.setStroke(mStrokeWidth, mColor);

    GradientDrawable pressedDrawable =
        (GradientDrawable) getResources().getDrawable(R.drawable.tag_bg_pressed);
    pressedDrawable.setColor(mColor);

    StateListDrawable stateListDrawable = new StateListDrawable();
    stateListDrawable.addState(new int[] {-android.R.attr.state_pressed}, normalDrawable);
    stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, pressedDrawable);
    tv.setBackground(stateListDrawable);
  }

  private void updateTextColor(TextView tv) {
    int[] colors = new int[] {mColor, Color.WHITE};
    int[][] states = new int[2][];
    states[0] = new int[] {-android.R.attr.state_pressed};
    states[1] = new int[] {android.R.attr.state_pressed};
    ColorStateList colorStateList = new ColorStateList(states, colors);
    tv.setTextColor(colorStateList);
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


  /**
   * tag点击监听
   * 
   * @param onTagClickListener
   */
  public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
    mOnTagClickListener = onTagClickListener;
  }

  public interface OnTagClickListener {
    void OnTagClick(String text, int position);
  }

  /**
   * 设置tab主色
   * 
   * @param color
   */
  public void setColor(int color) {
    mColor = color;
  }

}
