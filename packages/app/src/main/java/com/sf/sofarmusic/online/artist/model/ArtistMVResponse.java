package com.sf.sofarmusic.online.artist.model;

import com.google.gson.annotations.SerializedName;
import com.sf.base.network.retrofit.response.ListResponse;
import com.sf.sofarmusic.model.MV;
import java.util.List;

public class ArtistMVResponse implements ListResponse<MV> {

    @SerializedName("mvList")
    public List<MV> mvs;

    @SerializedName("has_more")
    public int haveMore;

    @Override
    public boolean hasMore() {
        return haveMore == 1;
    }

    @Override
    public List<MV> getItems() {
        return mvs;
    }
}
