package com.sf.sofarmusic.online.rank.model;

import com.google.gson.annotations.SerializedName;
import com.sf.sofarmusic.model.Song;

import java.util.List;


/**
 * 榜单信息
 */
public class Rank {

  @SerializedName("name")
  public String name;

  @SerializedName("type")
  public int type;

  @SerializedName("count")
  public int count;

  @SerializedName("comment")
  public String comment;

  @SerializedName("pic_s260")
  public String squareThumbUrl;

  @SerializedName("pic_s444")
  public String rectangleThumbUrl;

  @SerializedName("content")
  public List<Song> songs;

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (this == obj) {
      return true;
    }

    if (obj instanceof Rank && this.type == ((Rank) obj).type) {
      return true;
    }
    return false;
  }
}
