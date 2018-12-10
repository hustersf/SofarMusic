package com.sf.demo.system.notification;

import com.google.gson.annotations.SerializedName;

public class NotifyContent {

  // 包名
  @SerializedName("packageName")
  public String mPackageName;

  // app名字
  @SerializedName("appName")
  public String mAppName;

  // 来源
  @SerializedName("crawlType")
  public @NotifyService.NotifyObtainSource String mSoruce;

  // 标题
  @SerializedName("pushTitle")
  public String mTitle;

  // 内容
  @SerializedName("pushContent")
  public String mContent;
}
