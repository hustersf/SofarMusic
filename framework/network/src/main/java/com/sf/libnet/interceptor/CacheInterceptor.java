package com.sf.libnet.interceptor;

import java.io.IOException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by allen on 2017/1/3.
 * <p>
 * 网络缓存
 */

public class CacheInterceptor implements Interceptor {
  private Context mContext;

  public CacheInterceptor(Context context) {
    mContext = context;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    // 如果没有网络，则启用 FORCE_CACHE
    if (!isNetworkConnected()) {
      request = request.newBuilder()
          .cacheControl(CacheControl.FORCE_CACHE)
          .build();
    }

    Response originalResponse = chain.proceed(request);
    if (isNetworkConnected()) {
      // 有网的时候读接口上的@Headers里的配置
      String cacheControl = request.cacheControl().toString();
      if (TextUtils.isEmpty(cacheControl)) {
        cacheControl = "public, max-age=60";
      }
      return originalResponse.newBuilder()
          .header("Cache-Control", cacheControl)
          .removeHeader("Pragma")
          .build();
    } else {
      return originalResponse.newBuilder()
          .header("Cache-Control", "public, only-if-cached, max-stale=3600")
          .removeHeader("Pragma")
          .build();
    }
  }

  /**
   * 判断是否有网络
   *
   * @return 返回值
   */
  public boolean isNetworkConnected() {
    if (mContext != null) {
      ConnectivityManager mConnectivityManager =
          (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
      if (mNetworkInfo != null) {
        return mNetworkInfo.isAvailable();
      }
    }
    return false;
  }
}
