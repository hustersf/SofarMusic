package com.sf.base.network.page;

import java.util.List;

public interface PageList<PAGE, MODE> {

  /**
   * 刷新列表数据
   */
  void refresh();

  /**
   * 列表是否为空
   */
  boolean isEmpty();

  /**
   * 列表数据集合
   */
  List<MODE> getItems();

  /**
   * 注册数据监听者
   */
  void registerObserver(PageListObserver observer);

  /**
   * 注销数据监听者
   */
  void unRegisterObserver(PageListObserver observer);
}
