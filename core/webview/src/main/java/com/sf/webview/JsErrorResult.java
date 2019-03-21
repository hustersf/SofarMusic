package com.sf.webview;

import com.google.gson.annotations.SerializedName;

public class JsErrorResult {

  @JsErrorCode
  @SerializedName("result")
  public final int mResult;

  @SerializedName("error_msg")
  public final String mErrorMsg;

  public JsErrorResult(int errorCode, String errorMsg) {
    mResult = errorCode;
    mErrorMsg = errorMsg;
  }
}
