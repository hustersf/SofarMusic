package com.sf.libnet.retrofit;

import java.util.Map;

import com.google.gson.Gson;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Call;

public interface RetrofitConfig {

  Params buildParams();

  String buildBaseUrl();

  Gson buildGson();

  OkHttpClient buildClient();

  Call<Object> buildCall(Call<Object> call);

  Observable<?> buildObservable(Observable<?> o, Call<Object> call);


  interface Params {

    Map<String, String> getHeaderParams();

    Map<String, String> getUrlParams();

    Map<String, String> getBodyParams();
  }

}
