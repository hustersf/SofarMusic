package com.sf.sofarmusic.online.recommend.model;

import com.google.gson.annotations.SerializedName;
import com.sf.base.network.retrofit.response.ListResponse;
import java.util.List;

public class RecommendResponse implements ListResponse<FeedGroup> {

  @SerializedName("modules")
  public List<FeedGroup> feedGroups;

  @Override
  public List<FeedGroup> getItems() {
    return feedGroups;
  }
}
