package com.sf.sofarmusic.api;

import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.model.response.ArtistResponse;
import com.sf.sofarmusic.model.response.LrcResponse;
import com.sf.sofarmusic.online.rank.model.RankDetailResponse;
import com.sf.sofarmusic.online.rank.model.RankResponse;
import com.sf.sofarmusic.online.recommend.model.RecommendResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MusicApiService {

  /**
   * 获取榜单列表
   */
  @GET("v1/restserver/ting?method=baidu.ting.billboard.billCategory")
  Observable<RankResponse> rankList();

  /**
   * 获取榜单歌曲
   */
  @GET("v1/restserver/ting?method=baidu.ting.billboard.billList")
  Observable<RankDetailResponse> rankSongs(@Query("type") int type, @Query("offset") int offset,
      @Query("size") int size);

  /**
   * 获取歌词的地址列表
   */
  @GET("v1/restserver/ting?method=baidu.ting.search.lrcys")
  Observable<LrcResponse> getLrcLink(@Query("query") String name);

  /**
   * 获取歌词内容
   */
  @GET()
  Observable<String> getLrc(@Url String url);

  /**
   * 获取在线歌曲播放地址
   */
  @GET("v1/restserver/ting?method=baidu.ting.song.play")
  Observable<Song> getSongInfo(@Query("songid") String songId);


  /**
   * 获取歌手的信息
   */
  @GET()
  Observable<ArtistResponse> getArtistInfo(@Url String url, @Query("artist") String name);


  /**
   * 获取推荐列表 //&cuid=C41D777FF9FC42A766C5E5958A9C9A41
   */
  @GET("v1/restserver/ting?method=baidu.ting.plaza.newEditionIndex")
  Observable<RecommendResponse> recommendList();

  /**
   * 获取歌手列表
   */
  @GET("v1/restserver/ting?method=baidu.ting.artist.getList")
  Observable<RecommendResponse> artistList(@Query("area") int area, @Query("sex") int sex);
}
