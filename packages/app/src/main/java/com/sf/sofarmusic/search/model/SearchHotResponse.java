package com.sf.sofarmusic.search.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchHotResponse {

  @SerializedName("result")
  public List<SearchInfo> hotSearchInfos;

}
