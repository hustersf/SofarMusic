package com.sf.sofarmusic.play.core;

import com.sf.sofarmusic.db.song.SongRecordManager;
import com.sf.sofarmusic.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * 播放列表数据
 */
public class PlayDataHolder {

  private static final PlayDataHolder holder = new PlayDataHolder();
  private List<Song> songs = new ArrayList<>();

  public static PlayDataHolder getInstance() {
    return holder;
  }

  public void setSongs(List<Song> data) {
    setSongs(data, true);
  }

  public void setSongs(List<Song> data, boolean diskCache) {
    if (songs == null) {
      songs = new ArrayList<>();
    }
    songs.clear();
    
    // 过滤掉不能播放的歌曲
    for (int i = 0; i < data.size(); i++) {
      if (data.get(i).delStatus != 1) {
        songs.add(data.get(i));
      }
    }

    if (diskCache) {
      SongRecordManager.getInstance().asyncReplaceSongList(songs);
    }
  }

  public List<Song> getSongs() {
    return songs;
  }

  public void clearSongs() {
    songs.clear();
    SongRecordManager.getInstance().asyncClearSongList();
  }
}
