package com.sf.sofarmusic.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sf.libnet.gson.Gsons;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.model.PlayInfo;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.search.model.SearchAllInfo;
import java.lang.reflect.Type;

public class SearchAllInfoDeserializer implements JsonDeserializer<SearchAllInfo> {
  @Override
  public SearchAllInfo deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    SearchAllInfo searchAllInfo = Gsons.SOFAR_GSON.fromJson(json, typeOfT);
    if (searchAllInfo.searchContentType == SearchAllInfo.TYPE_PLAY) {
      searchAllInfo.playInfo = Gsons.SOFAR_GSON.fromJson(json, PlayInfo.class);
    } else if (searchAllInfo.searchContentType == SearchAllInfo.TYPE_SONG) {
      searchAllInfo.song = Gsons.SOFAR_GSON.fromJson(json, Song.class);
    }
    if (searchAllInfo.searchContentType == SearchAllInfo.TYPE_ALBUM) {
      searchAllInfo.album = Gsons.SOFAR_GSON.fromJson(json, Album.class);
    }
    if (searchAllInfo.searchContentType == SearchAllInfo.TYPE_ARTIST) {
      searchAllInfo.artist = Gsons.SOFAR_GSON.fromJson(json, Artist.class);
    }
    return searchAllInfo;
  }
}
