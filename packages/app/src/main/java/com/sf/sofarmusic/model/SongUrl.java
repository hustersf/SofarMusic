package com.sf.sofarmusic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SongUrl {

    @SerializedName("url")
    public List<SongLink> songLinks;

}
