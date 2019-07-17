package com.sf.base.network.retrofit.response;

import java.util.List;

/**
 * 服务端下发一个list数据
 */
public interface ListResponse<MODEL> {

  /**
   * @return 获取是否还有更多.
   */
  boolean hasMore();


  /**
   * @return 获取本次返回的数据集.
   */
  List<MODEL> getItems();

}
