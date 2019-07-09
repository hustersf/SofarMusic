package com.sf.base.callback;

import android.content.Intent;

/**
 * 包装ActivityCallback
 */
public abstract class ActivityCallbackAdapter implements ActivityCallback {

  @Override
  public void onCancel(Intent data) {}
}
