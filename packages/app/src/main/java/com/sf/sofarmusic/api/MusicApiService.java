package com.sf.sofarmusic.api;

import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.model.response.ArtistResponse;
import com.sf.sofarmusic.model.response.LrcResponse;
import com.sf.sofarmusic.model.response.RankSongsResponse;
import com.sf.sofarmusic.online.rank.model.RankResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MusicApiService {

  /**
   * 获取榜单列表
   */
  @GET("ting?method=baidu.ting.billboard.billCategory")
  Observable<RankResponse> rankList();


  /**
   * 获取榜单歌曲
   */
  @GET("ting?method=baidu.ting.billboard.billList")
  Observable<RankSongsResponse> getRankSongs(@Query("type") int type,
      @Query("size") int size, @Query("offset") int offset);

  /**
   * 获取歌词的地址列表
   */
  @GET("ting?method=baidu.ting.search.lrcys")
  Observable<LrcResponse> getLrcLink(@Query("query") String name);

  /**
   * 获取歌词内容
   */
  @GET()
  Observable<String> getLrc(@Url String url);

  /**
   * 获取在线歌曲播放地址
   */
  @GET("ting?method=baidu.ting.song.play")
  Observable<Song> getSongInfo(@Query("songid") String songId);


  /**
   * 获取歌手的信息
   */
  @GET()
  Observable<ArtistResponse> getArtistInfo(@Url String url, @Query("artist") String name);
}
