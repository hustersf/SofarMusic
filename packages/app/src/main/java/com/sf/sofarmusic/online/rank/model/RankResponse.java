package com.sf.sofarmusic.online.rank.model;

import com.google.gson.annotations.SerializedName;
import com.sf.base.network.retrofit.response.ListResponse;

import java.util.List;

/**
 * 榜单报文
 */
public class RankResponse implements ListResponse<Rank> {

  @SerializedName("content")
  public List<Rank> ranks;

  @Override
  public boolean hasMore() {
    return false;
  }

  @Override
  public List<Rank> getItems() {
    return ranks;
  }

}
