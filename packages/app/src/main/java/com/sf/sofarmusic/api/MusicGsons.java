package com.sf.sofarmusic.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sf.sofarmusic.search.model.SearchAllInfo;

public class MusicGsons {

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(SearchAllInfo.class, new SearchAllInfoDeserializer())
            .create();
}
