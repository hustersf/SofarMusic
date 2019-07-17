package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;

/**
 * 存放歌曲信息
 */
public class Song {

  // 当前歌曲是否正在播放
  public boolean play;

  @SerializedName("song_id")
  public String songId;

  @SerializedName("title")
  public String name;

  // 歌手id,本地音乐会用到
  public String authorId;

  @SerializedName("author")
  public String author;

  @SerializedName("ting_uid")
  public String tingUid;

  @SerializedName("album_id")
  public String albumId;

  @SerializedName("album_title")
  public String albumTitle;

  @SerializedName("pic_small")
  public String smallThumbUrl;

  @SerializedName("pic_big")
  public String bigThumbUrl;

  @SerializedName("lrclink")
  public String lrcLink;

  @SerializedName("songurl")
  public SongUrl songUrl;

  @SerializedName("bitrate")
  public SongLink songLink;

  // 歌曲本地播放地址
  public String songUri;

  //歌曲播放时长 秒
  @SerializedName("file_duration")
  public long length;

  // 歌曲播放时长
  public long duration;

  // 本地歌曲缩略图地址
  public String albumImgUri;

}
