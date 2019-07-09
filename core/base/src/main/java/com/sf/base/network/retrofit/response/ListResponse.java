package com.sf.base.network.retrofit.response;
import java.util.List;

/**
 * 服务端下发一个list数据
 */
public interface ListResponse<MODEL> {

    List<MODEL> getItems();

}
