package com.sf.deeplink.matcher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;

public interface Matcher {
  boolean match(@NonNull Uri routerUri);
  Intent result(@NonNull Context context, @NonNull Uri routerUri);
}
