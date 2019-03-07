package com.sf.base.network.retrofit.response;

/**
 * 如果存在多页，则需要下发一个cursor来指向当前页的下一页
 */
public interface CursorResponse<MODEL> extends ListResponse<MODEL> {

  String getCursor();

}
