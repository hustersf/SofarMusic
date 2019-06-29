package com.sf.sofarmusic.local.model;

import com.sf.sofarmusic.model.Song;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地歌曲数据
 */
public class LocalSongDataHolder {

  private static final LocalSongDataHolder holder = new LocalSongDataHolder();
  private List<Song> songs = new ArrayList<>();

  public static LocalSongDataHolder getInstance() {
    return holder;
  }

  public void setSongs(List<Song> data) {
    if (songs == null) {
      songs = new ArrayList<>();
    }
    songs.clear();
    songs.addAll(data);
  }

  public List<Song> getSongs() {
    return songs;
  }

  public void clearSongs() {
    songs.clear();
  }
}
