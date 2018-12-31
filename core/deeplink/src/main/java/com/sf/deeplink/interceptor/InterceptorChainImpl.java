package com.sf.deeplink.interceptor;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.sf.utility.LogUtil;

import java.util.List;

/**
 * 参考 {@link okhttp3.internal.http.RealInterceptorChain}
 */
public class InterceptorChainImpl implements Interceptor.Chain {

  private static final String TAG = "InterceptorChainImpl";

  private List<Interceptor> mInterceptors;
  private final Uri mUri;
  private final int mIndex;
  private final Context mContext;
  private int mCalls;

  public InterceptorChainImpl(List<Interceptor> interceptors, Context context, @NonNull Uri uri,
      int index) {
    mInterceptors = interceptors;
    mContext = context;
    mUri = uri;
    mIndex = index;
  }


  @NonNull
  @Override
  public Uri uri() {
    return mUri;
  }

  @NonNull
  @Override
  public Context context() {
    return mContext;
  }

  @Override
  public void abort() {
    mInterceptors.clear();
    if (mContext != null && mContext instanceof Activity) {
      Activity activity = (Activity) mContext;
      if (!activity.isFinishing()) {
        activity.finish();
      }
    }
  }

  /**
   * @param uri 在这里允许对uri，做一些处理，在传给下一个Interceptor
   */
  @Override
  public void proceed(@NonNull Uri uri) {
    if (mIndex >= mInterceptors.size()) {
      throw new AssertionError();
    }

    mCalls++;

    if (mCalls > 1) {
      throw new IllegalStateException("uri interceptor " + mInterceptors.get(mIndex - 1)
          + " must call proceed() exactly once");
    }

    InterceptorChainImpl next = new InterceptorChainImpl(mInterceptors, mContext, uri, mIndex + 1);
    Interceptor interceptor = mInterceptors.get(mIndex);
    interceptor.interceptor(next);
    LogUtil.d(TAG, "interceptor:" + interceptor.getClass().getSimpleName());
  }
}
