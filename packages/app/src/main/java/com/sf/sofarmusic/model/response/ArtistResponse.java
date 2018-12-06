package com.sf.sofarmusic.model.response;

import com.google.gson.annotations.SerializedName;
import com.sf.sofarmusic.model.Artist;

public class ArtistResponse {

  @SerializedName("artist")
  public Artist mArtist;
}
