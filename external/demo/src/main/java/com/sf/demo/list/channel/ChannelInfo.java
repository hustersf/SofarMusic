package com.sf.demo.list.channel;

import com.google.gson.annotations.SerializedName;

/**
 * 频道信息
 */
public class ChannelInfo {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("isFixed")
    public boolean isFixed;

}
