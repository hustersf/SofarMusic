package com.sf.sofarmusic.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 存放歌手信息
 */
public class Artist implements Serializable {

  public static final int AREA_ALL = 0;// 所有歌手，排在前面的是热门歌手
  public static final int AREA_CHINA = 6;// 华语歌手
  public static final int AREA_WEST = 3;// 欧美歌手
  public static final int AREA_KOREA = 7;// 韩国歌手
  public static final int AREA_JAPAN = 60;// 日本歌手
  public static final int AREA_OTHER = 5;// 其它地区歌手

  public static final int SEX_ALL = 0; // 不区分男女或者组合
  public static final int SEX_MAN = 1; // 男歌手
  public static final int SEX_WOMAN = 2; // 女歌手
  public static final int SEX_GROUP = 3; // 组合


  @SerializedName(value = "artist_id", alternate = "artistid")
  public String artistId;

  @SerializedName("area")
  public String area;

  @SerializedName(value = "name", alternate = {"artist_name", "artistname"})
  public String name;

  @SerializedName("gender")
  public String gender;

  @SerializedName("country")
  public String country;

  @SerializedName("songs_total")
  public int songCount;

  @SerializedName("albums_total")
  public int albumCount;

  @SerializedName("ting_uid")
  public String tingUid;

  @SerializedName("avatar_mini")
  public String avatarMiniUrl;

  @SerializedName("avatar_small")
  public String avatarSmallUrl;

  @SerializedName("avatar_middle")
  public String avatarMiddleUrl;

  @SerializedName(value = "avatar_big", alternate = "avatar_s180")
  public String avatarBigUrl;

}
