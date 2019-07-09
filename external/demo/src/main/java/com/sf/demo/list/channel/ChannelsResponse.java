package com.sf.demo.list.channel;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ChannelsResponse {

  // 已添加的频道
  @SerializedName("channelSub")
  public List<ChannelInfo> subChannels;

  // 未添加的频道
  @SerializedName("channelNoSub")
  public List<ChannelInfo> noSubChannels;
}
