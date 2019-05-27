package com.sf.widget.bitmap.combine.layout;

import android.graphics.Bitmap;

/**
 * 决定头像的排列方式
 */
public interface ILayoutManager {

  /**
   * 
   * @param size 组合头像图片的总大小
   * @param subSize 每一个头像的大小
   * @param gap 头像之间的间隔的距离
   * @param gapColor 间隔的颜色
   * @param bitmaps 头像数组
   * @return 返回组合头像
   */
  Bitmap combineBitmap(int size, int subSize, int gap, int gapColor, Bitmap[] bitmaps);
}
