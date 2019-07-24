package com.sf.sofarmusic.online.artist.model;

import com.google.gson.annotations.SerializedName;
import com.sf.sofarmusic.model.MV;
import com.sf.sofarmusic.model.Video;
import com.sf.sofarmusic.model.VideoFile;
import java.util.Map;

public class MVDetailResponse {

  @SerializedName("share_url")
  public String shareUrl;

  @SerializedName("share_pic")
  public String shareImg;

  @SerializedName("mv_info")
  public MV mv;

  @SerializedName("video_info")
  public Video video;

  @SerializedName("min_definition")
  public String minDefinition;

  @SerializedName("max_definition")
  public String maxDefinition;

  @SerializedName("files")
  public Map<String, VideoFile> videoFileMap;

}
