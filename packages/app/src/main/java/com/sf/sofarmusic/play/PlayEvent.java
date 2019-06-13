package com.sf.sofarmusic.play;

import com.sf.sofarmusic.model.Song;

public class PlayEvent {

  /**
   * 切换歌曲
   */
  public static class ChangeSongEvent {
    public int position;

    public ChangeSongEvent(int position) {
      this.position = position;
    }
  }

  /**
   * 选中歌曲
   */
  public static class SelectSongEvent {
    public Song song;

    public SelectSongEvent(Song song) {
      this.song = song;
    }
  }

  /**
   * 歌曲播放
   */
  public static class PlaySongEvent {}

  /**
   * 歌曲暂停
   */
  public static class PauseSongEvent {}


}
