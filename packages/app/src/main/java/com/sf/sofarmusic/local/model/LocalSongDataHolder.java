package com.sf.sofarmusic.local.model;

import com.sf.sofarmusic.model.Song;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地歌曲数据
 */
public class LocalSongDataHolder {

  private static final LocalSongDataHolder holder = new LocalSongDataHolder();

  // 本地所有歌曲列表
  private List<Song> allSongs = new ArrayList<>();

  // 用户手动选择的歌曲列表，如选中某个歌手/专辑/文件
  private List<Song> selectSongs = new ArrayList<>();

  public static LocalSongDataHolder getInstance() {
    return holder;
  }

  public void setAllSongs(List<Song> data) {
    if (allSongs == null) {
      allSongs = new ArrayList<>();
    }
    allSongs.clear();
    allSongs.addAll(data);
  }

  public List<Song> getAllSongs() {
    return allSongs;
  }

  public void setSelectSongs(List<Song> data) {
    if (selectSongs == null) {
      selectSongs = new ArrayList<>();
    }
    selectSongs.clear();
    selectSongs.addAll(data);
  }

  public List<Song> getSelectSongs() {
    return selectSongs;
  }


  public void clear() {
    allSongs.clear();
    selectSongs.clear();
  }
}
