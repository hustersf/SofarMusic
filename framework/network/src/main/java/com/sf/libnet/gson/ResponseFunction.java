package com.sf.libnet.gson;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 取出 {@link ResponseDeserializer 中的data数据}
 */
public class ResponseFunction<T> implements Function<Response<T>, T> {

  @Override
  public T apply(@NonNull Response<T> response) throws Exception {
    return response.body();
  }
}
