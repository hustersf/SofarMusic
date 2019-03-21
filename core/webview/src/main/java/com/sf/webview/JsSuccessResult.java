package com.sf.webview;

import com.google.gson.annotations.SerializedName;

public class JsSuccessResult {

  @JsErrorCode
  @SerializedName("result")
  public final int mResult = JsErrorCode.SUCCESS;

  @SerializedName("data")
  public Object mData;

  public JsSuccessResult() {}

  public JsSuccessResult(Object data) {
    mData = data;
  }
}
