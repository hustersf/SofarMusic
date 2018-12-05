package com.sf.sofarmusic.api;

import com.sf.sofarmusic.model.response.RankSongsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicApiService {

  @GET("ting")
  Observable<RankSongsResponse> getRankSongs(@Query("type") String type,
      @Query("size") int size, @Query("offset") int offset);

}
