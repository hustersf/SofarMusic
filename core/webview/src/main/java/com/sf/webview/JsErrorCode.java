package com.sf.webview;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 定义一些约定的错误码
 */
@Retention(RetentionPolicy.SOURCE)
public @interface JsErrorCode {

  int UNKNOWN_ERROR = -1;

  int SUCCESS = 0;

}
