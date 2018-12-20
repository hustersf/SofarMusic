package com.sf.demo.api;

import com.sf.demo.system.notification.NotifyContent;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface DemoApiService {

  @POST()
  Observable<ActionResponse> uploadNotifyMessage(@Url String url, @Body NotifyContent notifyContent);

  @FormUrlEncoded
  @POST()
  Observable<ActionResponse> uploadNotifyMessage(@Url String url, @Field("title") String title,
      @Field("content") String content);
}
