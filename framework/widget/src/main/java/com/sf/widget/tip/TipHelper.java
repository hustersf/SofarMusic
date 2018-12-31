package com.sf.widget.tip;

/**
 * 用于在RecyclerFragment上显示各种Tips, 包括loading, noMore, empty等等.
 */
public interface TipHelper {

  /**
   * 当前列表为空.
   */
  void showEmpty();

  /**
   * 隐藏空tips.
   */
  void hideEmpty();

  /**
   * 列表当前正在加载.
   */
  void showLoading(boolean firstPage, boolean isCache);

  /**
   * 隐藏加载的tip.
   */
  void hideLoading();

  /**
   * 显示没有更多的tip.
   */
  void showNoMoreTips();

  /**
   * 隐藏没有更多的tip.
   */
  void hideNoMoreTips();

  /**
   * 显示加载错误.
   *
   * @param firstPage 是否第一页.
   * @param error 错误内容.
   */
  void showError(boolean firstPage, Throwable error);

  /**
   * 隐藏错误信息.
   */
  void hideError();
}
