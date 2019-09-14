package com.sf.sofarmusic.search.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.model.PlayInfo;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.model.UserInfo;

/**
 * 搜索汇总信息
 */
public class SearchAllInfo {

  public static final int TYPE_NONE = -1; // 全部信息
  public static final int TYPE_SONG = 0; // 单曲
  public static final int TYPE_ARTIST = 1; // 歌手
  public static final int TYPE_ALBUM = 2; // 专辑
  public static final int TYPE_PLAY = 10; // 歌单
  public static final int TYPE5 = 15;

  @SerializedName("search_content_type")
  public int searchContentType;

  @Nullable
  public PlayInfo playInfo;

  @Nullable
  public Song song;

  @Nullable
  public Album album;

  @Nullable
  public Artist artist;

}
