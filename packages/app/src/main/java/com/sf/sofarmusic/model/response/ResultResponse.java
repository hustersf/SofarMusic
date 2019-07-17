package com.sf.sofarmusic.model.response;

import com.google.gson.annotations.SerializedName;

public class ResultResponse<T> {

  @SerializedName("result")
  public T result;

}
