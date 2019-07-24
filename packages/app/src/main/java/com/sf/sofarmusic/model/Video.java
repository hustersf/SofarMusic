package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;

public class Video {

  @SerializedName("video_id")
  public String videoId;

  @SerializedName("mv_id")
  public String mvId;

  @SerializedName("sourcepath")
  public String sourcePath;

  @SerializedName("thumbnail")
  public String thumbnail;
}
