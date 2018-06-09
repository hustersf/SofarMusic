package com.sf.libnet.http;

import android.content.Context;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by sufan on 17/3/28.
 */

public class HttpConfig {

    public static InputStream[] certificates = null;     //证书（自签名）
    public static Context context;   //上下文
    public static Map<String, Object> headerMap;   //报文头

    public static boolean isDebug = false;     //是否是调适模式
    public static boolean isCache = true;     //是否支持缓存

}
