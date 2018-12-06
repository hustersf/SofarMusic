package com.sf.libnet.cookie;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/3/10.
 * 永久化cookie
 */

public class DiskCookieJar implements CookieJar {

  private Context mContext;

  public DiskCookieJar(Context context) {
    mContext = context;
  }


  private final PersistentCookieStore cookieStore = new PersistentCookieStore(mContext);


  @Override
  public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
    if (cookies != null && cookies.size() > 0) {
      for (Cookie item : cookies) {
        cookieStore.add(url, item);
      }
    }
  }

  @Override
  public List<Cookie> loadForRequest(HttpUrl url) {
    List<Cookie> cookies = cookieStore.get(url);
    return cookies;
  }
}
