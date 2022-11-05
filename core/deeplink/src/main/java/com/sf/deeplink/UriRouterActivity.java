package com.sf.deeplink;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * 用于外部浏览器中的web页面至app原生页面
 * 所有的解析都会经过这里
 * adb shell am start -a android.intent.action.VIEW -d sofar://login
 *
 * 如发现没有触发此Activity，请检查AndroidManifest.xml的inintent-filter配置是佛普正确
 */
public class UriRouterActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    new UriRouterParser(this).parse();
  }
}
