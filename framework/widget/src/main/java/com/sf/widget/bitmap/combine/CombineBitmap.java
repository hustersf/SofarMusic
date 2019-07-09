package com.sf.widget.bitmap.combine;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sf.widget.bitmap.combine.layout.DingLayoutManager;
import com.sf.widget.bitmap.combine.layout.ILayoutManager;
import com.sf.widget.bitmap.combine.layout.WechatLayoutManager;

public class CombineBitmap {

  Bitmap[] bitmaps;
  int size;
  int gap;
  int gapColor;

  ILayoutManager layoutManager;

  public static CombineBitmap with() {
    return new CombineBitmap();
  }

  public CombineBitmap layoutManager(ILayoutManager layoutManager) {
    this.layoutManager = layoutManager;
    return this;
  }

  /**
   * size 最终生成的图片的大小
   */
  public CombineBitmap size(int size) {
    this.size = size;
    return this;
  }


  /**
   * gap 每个头像之间的间距
   */
  public CombineBitmap gap(int gap) {
    this.gap = gap;
    return this;
  }

  /**
   * gapColor 间距的颜色
   */
  public CombineBitmap gapColor(int gapColor) {
    this.gapColor = gapColor;
    return this;
  }

  /**
   * bitmaps 头像集合
   */
  public CombineBitmap load(Bitmap[] bitmaps) {
    this.bitmaps = bitmaps;
    return this;
  }

  public void into(ImageView imageView) {
    int subSize = layoutManager.getSubSize(size, gap, layoutManager, bitmaps.length);
    Bitmap result = layoutManager.combineBitmap(size, subSize, gap, gapColor, bitmaps);
    imageView.setImageBitmap(result);
  }
}
