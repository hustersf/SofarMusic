package com.sf.deeplink.interceptor;

import android.content.Context;
import android.text.TextUtils;
import com.sf.utility.LogUtil;

import java.lang.reflect.Field;

public class LogInterceptor implements Interceptor {
  @Override
  public void interceptor(Chain chain) {
    String referrer = getReferrer(chain.context());
    if (!TextUtils.isEmpty(referrer) && !chain.context().getPackageName().equals(referrer)) {
      LogUtil.d("TAG", "能拿到调用者的包名，并且包名来自第三方");
    }
    chain.proceed(chain.uri());
  }

  /**
   * 通过反射获取，放置别人手动修改Referrer的值
   * 缺点5.1以上才能获取
   */
  private String getReferrer(Context context) {
    try {
      Class activityClass = Class.forName("android.app.Activity");
      Field refererField = activityClass.getDeclaredField("mReferrer");
      refererField.setAccessible(true);
      String referrer = (String) refererField.get(context);
      return referrer;
    } catch (Exception e) {
      return null;
    }
  }
}
