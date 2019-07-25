package com.sf.sofarmusic.play.core;

import com.sf.sofarmusic.model.Song;

public class PlayEvent {

  /**
   * 切换歌曲 by song
   */
  public static class SelectSongEvent {
    public Song song;

    public SelectSongEvent(Song song) {
      this.song = song;
    }
  }

  /**
   * 删除歌曲
   */
  public static class DeleteSongEvent {
    public Song song;

    public DeleteSongEvent(Song song) {
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

  /**
   * 清空歌曲列表
   */
  public static class ClearSongEvent {}

  /**
   * 播放服务绑定成功
   */
  public static class PlayServiceConnected {
    public MusicPlayer playerHelper;

    public PlayServiceConnected(MusicPlayer playerHelper) {
      this.playerHelper = playerHelper;
    }
  }

  /**
   * 进度条拖拽
   * 
   * 0拖拽中，1拖拽完成
   */
  public static class PlayProgressDragEvent {}

}
