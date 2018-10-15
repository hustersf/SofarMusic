package com.sf.libnet.http;

import android.os.Environment;

import com.sf.libnet.cookie.SimpleCookieJar;
import com.sf.libnet.https.HttpsUtil;
import com.sf.libnet.interceptor.CacheInterceptor;
import com.sf.libnet.interceptor.OkHttpRequestInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sufan on 17/3/1.
 * 可以在这里配置超时时间，增加拦截器，cookie等等
 */


//初始化Retrofit
public class RetrofitManager {

    //https://api.douban.com/v2/movie/top250?start=0&count=10
    private static String baseUrl = "https:www.baidu.com/";
    private static final int DEFAULT_TIME_OUT = 30;//超时时间 30s
    private static final int DEFAULT_READ_TIME_OUT = 30;//超时时间 30s
    private static final int DEFAULT_WRITE_OUT = 30;//超时时间 30s
    private Retrofit mRetrofit;

    private static RetrofitManager instance = null;

    private OkHttpClient mClient;

    private RetrofitManager() {

        //创建OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(DEFAULT_WRITE_OUT, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间


        //添加证书
        if (HttpConfig.certificates != null) {
            HttpsUtil https1Utils=new HttpsUtil(HttpConfig.certificates, null, null);
            builder.sslSocketFactory(https1Utils.sslSocketFactory,https1Utils.trustManager);
        }

        //请求头拦截器
        OkHttpRequestInterceptor requestInterceptor=new OkHttpRequestInterceptor(HttpConfig.headerMap);
        builder.addInterceptor(requestInterceptor);

        //日志拦截器
        if(HttpConfig.isDebug) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        //缓存配置,cache取决于服务端的响应头部是否有Cache-Control
        //缓存拦截器的作用在于若服务端没有设置Cache-Control 1
        //官方方法调用request的CacheControl方法 2
        //1和2效果一致
        if(HttpConfig.isCache){
            CacheInterceptor cacheInterceptor=new CacheInterceptor();
            builder.addNetworkInterceptor(cacheInterceptor);
            builder.cache(new Cache(new File(Environment.getExternalStorageDirectory().getPath() + "/SofarCache/Net"), 1024 * 1024 * 100));
        }

        //添加cookie
        builder.cookieJar(new SimpleCookieJar());
    //    builder.cookieJar(new MemoryCookieJar());
     //   builder.cookieJar(new DiskCookieJar());   //永久化cookie

        mClient = builder.build();
        mClient.newBuilder().build();

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(mClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }


    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }


    /**
     * 关闭验证DNS和certificate匹配
     */
    public void closeHostnameVerifier(){
       mClient.newBuilder().hostnameVerifier(new HostnameVerifier() {
           @Override
           public boolean verify(String hostname, SSLSession session) {
               //在这里可以校验域名
               return true;
           }
       }).build();

    }



    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

}
