package com.sf.deeplink.interceptor;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 参考 {@link okhttp3.Interceptor}
 */
public interface Interceptor {

  void interceptor(Chain chain);

  interface Chain {

    @NonNull
    Uri uri();

    @NonNull
    Context context();

    void abort();

    void proceed(@Nullable Uri uri);
  }

}
