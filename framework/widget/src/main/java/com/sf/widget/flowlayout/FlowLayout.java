package com.sf.widget.flowlayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sufan on 16/8/13.
 */
public class FlowLayout extends ViewGroup {


  public FlowLayout(Context context) {
    this(context, null);
  }

  public FlowLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  /**
   * 测量宽高，需要计算每一个子View的宽高，从而得出此从容器的宽高
   */
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);

    // wrap_content
    int width = 0; // 布局宽度
    int height = 0; // 布局高度

    int lineWidth = 0; // 行宽
    int lineHeight = 0; // 行高

    int cCount = getChildCount();
    for (int i = 0; i < cCount; i++) {
      View child = getChildAt(i);
      measureChild(child, widthMeasureSpec, heightMeasureSpec);

      MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
      int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
      int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

      // 换行
      if (lineWidth + childWidth + getPaddingLeft() + getPaddingRight() > widthSize) {
        width = Math.max(lineWidth, childWidth);
        height += lineHeight;

        lineWidth = childWidth;
        lineHeight = childHeight;
      } else {
        // 未换行
        lineWidth += childWidth;
        lineHeight = Math.max(lineHeight, childHeight);
      }

      if (i == cCount - 1) {
        width = Math.max(width, lineWidth);
        height += lineHeight;
      }
    }

    setMeasuredDimension(
        widthMode == MeasureSpec.EXACTLY ? widthSize : width + getPaddingLeft() + getPaddingRight(),
        heightMode == MeasureSpec.EXACTLY
            ? heightSize
            : height + getPaddingLeft() + getPaddingRight());
  }

  private List<List<View>> mAllViews = new ArrayList<>(); // 存储所有的VIEW
  private List<Integer> mLineHeights = new ArrayList<>(); // 存储每一行的高度

  /**
   * 放置每一个子View的位置
   */
  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    mAllViews.clear();
    mLineHeights.clear();

    List<View> lineViews = new ArrayList<>();

    int lineWidth = 0;
    int lineHeight = 0;

    int cCount = getChildCount();
    for (int i = 0; i < cCount; i++) {
      View child = getChildAt(i);

      MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
      int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
      int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

      // 换行
      if (lineWidth + childWidth + getPaddingLeft() + getPaddingRight() > getMeasuredWidth()) {
        mAllViews.add(lineViews);
        mLineHeights.add(lineHeight);

        // 重置行VIEW
        lineViews = new ArrayList<>();

        // 重置宽和高
        lineWidth = 0;
        lineHeight = childHeight;
      }
      lineWidth += childWidth;
      lineHeight = Math.max(lineHeight, childHeight);

      lineViews.add(child);
    }
    mAllViews.add(lineViews);
    mLineHeights.add(lineHeight);

    // 开始设置child的位置
    int left = getPaddingLeft();
    int top = getPaddingTop();
    for (int i = 0; i < mAllViews.size(); i++) {
      lineViews = mAllViews.get(i);
      lineHeight = mLineHeights.get(i);

      for (int j = 0; j < lineViews.size(); j++) {
        View child = lineViews.get(j);
        if (child.getVisibility() == View.GONE) {
          continue; // 进行下次循环
        }
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        int cl = left + lp.leftMargin;
        int ct = top + lp.topMargin;
        int cr = cl + child.getMeasuredWidth();
        int cb = ct + child.getMeasuredHeight();
        child.layout(cl, ct, cr, cb);

        left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
      }

      left = getPaddingLeft();
      top += lineHeight;
    }

  }


  /**
   * 用于生成和此容器相匹配的布局参数
   */
  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new MarginLayoutParams(getContext(), attrs);
  }
}
