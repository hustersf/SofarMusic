package com.sf.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * 解决某些场景下ViewPager高度设置为wrap_content时不显示的问题
 *
 * 场景1 RecyclerView嵌套ViewPager
 */
public class WrapContentHeightViewPager extends ViewPager {
  public WrapContentHeightViewPager(@NonNull Context context) {
    super(context);
  }

  public WrapContentHeightViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int height = 0;
    // 下面遍历所有child的高度
    for (int i = 0; i < getChildCount(); i++) {
      View child = getChildAt(i);
      child.measure(widthMeasureSpec,
          MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
      int h = child.getMeasuredHeight();
      if (h > height) // 采用最大的view的高度。
        height = h;
    }
    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
        MeasureSpec.EXACTLY);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }
}
