package com.sf.widget.chat.layoutmanager;

import android.graphics.Point;

/**
 * 决定头像的排列方式
 */
public interface ILayoutManager {

  /**
   * @param i view所在的位置
   * @param size 组合头像图片的总大小
   * @param subSize 每一个头像的大小
   * @param gap 头像之间的间隔的距离
   * @param count 子view的数量
   * @return 每一个头像view的坐标（x代表left,y代表top)
   */
  Point getPoint(int i, int size, int subSize, int gap, int count);

  /**
   *
   * @param size
   * @param gap
   * @param count
   * @return
   */
  int getSubSize(int size, int gap, int count);
}
