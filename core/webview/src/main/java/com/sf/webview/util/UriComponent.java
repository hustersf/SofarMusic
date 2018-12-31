package com.sf.webview.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.sf.deeplink.matcher.UriMatcher;

import io.reactivex.annotations.NonNull;

public class UriComponent {

  @Nullable
  public static Intent createIntentWithUri(Context context, @NonNull Uri uri) {
    UriMatcher uriMatcher = new UriMatcher();
    if (uriMatcher.match(uri)) {
      return uriMatcher.result(context, uri);
    }
    return null;
  }
}
