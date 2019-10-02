package com.sf.sofarmusic.model;


import com.google.gson.annotations.SerializedName;

public class UserInfo {

  @SerializedName("userid")
  public String userId;

  @SerializedName("username")
  public String name;

  @SerializedName("userpic_small")
  public String thumbSmallUrl;

  @SerializedName("userpic")
  public String thumbUrl;

}
