package com.sf.widget.refresh;

/**
 * 记录下拉刷新过程中的一些状态
 * RefreshView通常需要继承它，来实现在不同状态下的View动画
 */
public interface RefreshStatus {

  /**
   * 松手时调用
   */
  void reset();

  /**
   * 下拉的一瞬间调用
   */
  void pull();


  /**
   * 下拉过程中的变化
   */
  void pullProgress(float pullDistance, float pullProgress);


  /**
   * 松开刷新
   */
  void release();

  /**
   * 正在刷新
   */
  void refreshing();


  /**
   * 刷新完成
   */
  void complete();

}
