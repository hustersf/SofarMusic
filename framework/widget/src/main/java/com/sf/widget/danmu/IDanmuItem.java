package com.sf.widget.danmu;

import android.graphics.Canvas;

public interface IDanmuItem {

  /**
   * 绘制item
   */
  void doDraw(Canvas canvas);

  /**
   * item在Canvas的偏移量
   */
  void setStartPosition(int x, int y);

  /**
   * item是否滚出弹幕View的范围
   */
  boolean isOut();

  /**
   * 弹幕的高度
   */
  int getHeight();

  /**
   * 弹幕的宽度
   */
  int getWidth();

  /**
   * x轴的偏移量
   */
  int getCurrX();

  /**
   * y轴偏移量
   */
  int getCurrY();

  /**
   * 设置速度因子
   */
  void setSpeedFactor(float factor);

  /**
   * 获取速度因子
   */
  float getSpeedFactor();

  /**
   * 当前弹幕是否 与其他弹幕发生碰撞
   */
  boolean willHit(IDanmuItem runningItem);

}
