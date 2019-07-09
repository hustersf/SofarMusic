package com.sf.deeplink.matcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class UriMatcher implements Matcher {

  private final List<UriActivityMapping> mUriActivityMappings = new ArrayList<>();
  private @Nullable UriActivityMapping mResult;

  public UriMatcher() {
    mUriActivityMappings
        .add(new UriActivityMapping("sofar://login", "com.sf.sofarnote.login.LoginActivity"));

    mUriActivityMappings
        .add(new UriActivityMapping("sofar://email/login",
            "com.sf.sofarnote.login.email.EmailLoginActivity"));

    mUriActivityMappings.add(new UriActivityMapping("sofar://email/register",
        "com.sf.sofarnote.login.email.EmailRegisterActivity"));
  }


  /**
   * uri构成
   * [scheme:][//authority][path][?query][#fragment]
   * [scheme:][//host:port][path][?query][#fragment]
   */
  @Override
  public boolean match(@NonNull Uri routerUri) {
    // 保证uri有明确的scheme
    if (routerUri == null || routerUri.isRelative()) {
      return false;
    }

    for (UriActivityMapping uriActivityMapping : mUriActivityMappings) {
      Uri uri = uriActivityMapping.mUri;

      // 保证uri有明确的scheme
      if (uri.isRelative()) {
        continue;
      }

      // 匹配scheme
      if (!routerUri.getScheme().equals(uri.getScheme())) {
        continue;
      }

      // 匹配authority
      if (!TextUtils.isEmpty(routerUri.getAuthority())
          && !routerUri.getAuthority().equals(uri.getAuthority())) {
        continue;
      }

      // 匹配path
      if (!TextUtils.isEmpty(routerUri.getPath())
          && !routerUri.getPath().equals(uri.getPath())) {
        continue;
      }

      // 匹配query
      if (!TextUtils.isEmpty(uri.getQuery())
          && !uri.getQuery().equals(routerUri.getQuery())) {
        continue;
      }

      mResult = uriActivityMapping;
      return true;
    }

    return false;
  }

  @Nullable
  @Override
  public Intent result(@NonNull Context context, @NonNull Uri routerUri) {
    if (mResult != null) {
      Intent intent = new Intent();
      intent.setClassName(context.getPackageName(), mResult.mClassName);
      intent.setData(routerUri);
      return intent;
    }
    return null;
  }

  public static final class UriActivityMapping {

    public Uri mUri;
    public String mClassName;

    public UriActivityMapping(String uriString, String className) {
      mUri = Uri.parse(uriString);
      mClassName = className;
    }

  }
}
