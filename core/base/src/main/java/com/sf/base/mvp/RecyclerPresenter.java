package com.sf.base.mvp;

/**
 * 专门用于RecyclerView实现逻辑分离
 */
public class RecyclerPresenter<T> extends Presenter<T> {

  public RecyclerPresenter() {
    // 最多只允许一级继承, Presenter的继承需求应该都拆分成组合方式来实现
  }


}
