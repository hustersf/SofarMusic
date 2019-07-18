package com.sf.sofarmusic.api;

import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.model.response.ActionResponse;
import com.sf.sofarmusic.model.response.ResultResponse;
import com.sf.sofarmusic.online.artist.model.AlbumDetailResponse;
import com.sf.sofarmusic.online.artist.model.ArtistAlbumResponse;
import com.sf.sofarmusic.online.artist.model.ArtistMVResponse;
import com.sf.sofarmusic.online.artist.model.ArtistResponse;
import com.sf.sofarmusic.model.response.LrcResponse;
import com.sf.sofarmusic.online.artist.model.ArtistSongResponse;
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
   * 获取推荐列表 //&cuid=C41D777FF9FC42A766C5E5958A9C9A41
   */
  @GET("v1/restserver/ting?method=baidu.ting.plaza.newEditionIndex")
  Observable<RecommendResponse> recommendList();

  /**
   * 获取歌手列表
   */
  @GET("v1/restserver/ting?method=baidu.ting.artist.getList&order=1")
  Observable<ArtistResponse> artistList(@Query("area") int area, @Query("sex") int sex,
      @Query("offset") int offset, @Query("limit") int limit);


  /**
   * 歌手歌曲列表
   */
  @GET("v1/restserver/ting?method=baidu.ting.artist.getSongList&order=2")
  Observable<ArtistSongResponse> artistSongList(@Query("tinguid") String tingUid,
      @Query("offset") int offset, @Query("limit") int limit);

  /**
   * 歌手专辑列表
   */
  @GET("v1/restserver/ting?method=baidu.ting.artist.getAlbumList&order=1")
  Observable<ArtistAlbumResponse> artistAlbumList(@Query("tinguid") String tingUid,
      @Query("offset") int offset, @Query("limit") int limit);

  /**
   * 歌手MV列表
   */
  @GET("v1/restserver/ting?method=baidu.ting.artist.getArtistMVList&usetinguid=1")
  Observable<ResultResponse<ArtistMVResponse>> artistMVList(@Query("id") String tingUid,
      @Query("page") int page,
      @Query("size") int limit);

  /**
   * 专辑下的歌曲列表
   */
  @GET("v1/restserver/ting?method=baidu.ting.album.getAlbumInfo")
  Observable<AlbumDetailResponse> albumSongList(@Query("album_id") String albumId);

}
