package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;

public class VideoFile {

  @SerializedName("video_file_id")
  public String videoFileId;

  @SerializedName("video_id")
  public String videoId;

  @SerializedName("source_path")
  public String sourcePath;

  @SerializedName("file_link")
  public String fileLink;

  @SerializedName("aspect_ratio")
  public String aspectRatio;

  @SerializedName("file_size")
  public String fileSize;

  @SerializedName("definition")
  public String definition; // 分辨率，31流畅/41高清

  @SerializedName("definition_name")
  public String definitionName; // 分辨率名字，流畅/高清

  @SerializedName("file_format")
  public String fileFormat;

}
