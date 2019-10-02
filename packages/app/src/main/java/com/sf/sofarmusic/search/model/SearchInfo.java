package com.sf.sofarmusic.search.model;

import com.google.gson.annotations.SerializedName;

public class SearchInfo {

  public static final int ALL_LINK_TYPE = 0; // 跳转至搜索分类界面
  public static final int SONG_LINK_TYPE = 1; // 跳转至播放界面
  public static final int ALBUM_LINK_TYPE = 5; // 跳转至专辑详情页
  public static final int ARTIST_LINK_TYPE = 8; // 跳转至歌手详情页

  public static final String ARTIST = "artist"; // 歌手
  public static final String SONG = "song"; // 歌曲
  public static final String ALBUM = "album"; // 专辑

  @SerializedName("word")
  public String word;

  @SerializedName("linktype")
  public int linkType;

  @SerializedName("linkurl")
  public String linkUrl;

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SearchInfo) {
      return word != null && word.equals(((SearchInfo) obj).word);
    }
    return false;
  }
}
