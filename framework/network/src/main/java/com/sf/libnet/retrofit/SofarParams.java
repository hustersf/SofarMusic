package com.sf.libnet.retrofit;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共参数
 */
public class SofarParams implements RetrofitConfig.Params {
  @Override
  public Map<String, String> getHeaderParams() {
    Map<String, String> params = new HashMap<>();
    params.put("OS", "android");
    // ("application/x-www-form-urlencoded"),"multipart/form-data")
    params.put("Content-Type", "application/json");
    params.put("Accept-Encoding", "identity");
    params.put("Connection", "keep-alive");
    // 缓存可能导致无法正常显示文件下载进度（getContentLength=-1）
    params.put("Cache-Control", "no-cache");
    params.put("User-Agent", System.getProperty("http.agent"));
    return params;
  }

  @Override
  public Map<String, String> getUrlParams() {
    Map<String, String> params = new HashMap<>();
    params.put("format", "json");
    params.put("calback", "");
    params.put("from", "webapp_music");
    params.put("method", "baidu.ting.billboard.billList");
    return params;
  }

  @Override
  public Map<String, String> getBodyParams() {
    Map<String, String> params = new HashMap<>();
    return params;
  }
}
