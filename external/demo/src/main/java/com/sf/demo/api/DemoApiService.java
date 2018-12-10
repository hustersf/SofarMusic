package com.sf.demo.api;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface DemoApiService {

  @POST()
  Observable<ActionResponse> uploadNotifyMessage(@Url String url, @Body RequestBody body);
}
