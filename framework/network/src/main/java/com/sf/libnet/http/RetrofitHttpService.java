package com.sf.libnet.http;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by sufan on 17/3/23.
 */

public interface RetrofitHttpService {


    @GET()
    Call<ResponseBody> executGet(@Url String url);

    @GET()
    Call<ResponseBody> executGet(@Url String url, @QueryMap Map<String, String> maps);


    /**
     * 注意:
     * 1.如果方法的泛型指定的类不是ResonseBody,retrofit会将返回的string成用json转换器自动转换该类的一个对象,转换不成功就报错.
     *  如果不需要gson转换,那么就指定泛型为ResponseBody,
     *  只能是ResponseBody,子类都不行,同理,下载上传时,也必须指定泛型为ResponseBody
     * 2. map不能为null,否则该请求不会执行,但可以size为空.
     * 3.使用@url,而不是@Path注解,后者放到方法体上,会强制先urlencode,然后与baseurl拼接,请求无法成功.
     * @param url
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST()
    Call<ResponseBody> executePost(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 直接post体为一个json格式时,使用这个方法.注意:@Body 不能与@FormUrlEncoded共存
     * @param url
     * @param body
     * @return
     */
    @POST()
    Call<ResponseBody> executeJsonPost(@Url String url, @Body RequestBody body);


    @GET
    Call<ResponseBody>  loadBitmap(@Url String url);


   /**
    * 使用@Streaming，是Retrofit在处理结果之前，会将整个服务器响应结果放入内存，大文件容易oom
    */
    @Streaming
    @GET
    Call<ResponseBody>  downloadFile(@Url String url);




    //rxjava模式
    @GET
    Observable<String> obGetString(@Url String url, @QueryMap Map<String, String> params);

    @POST
    Observable<String> obPostString(@Url String url, @FieldMap Map<String, String> params);

}
