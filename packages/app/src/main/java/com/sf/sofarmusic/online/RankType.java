package com.sf.sofarmusic.online;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 榜单类型
 * 26,是歌手列表，但好像失效了
 */
@Retention(RetentionPolicy.SOURCE)
public @interface RankType {

  int NEW_SONG = 1; // 新歌榜
  int HOT_SONG = 2; // 热歌榜
  int SELF_SONG = 200; // 原创榜

  int CHINA_SONG = 20; // 华语榜
  int EA_SONG = 21; // 欧美榜
  int FILM_SONG = 24; // 影视榜
  int DOUBLE_SONG = 23; // 情歌榜
  int NET_SONG = 25; // 网路歌曲榜
  int OLD_SONG = 22; // 老歌榜
  int ROCK_SONG = 11; // 摇滚榜

  int BILLBOARD_SONG = 8; // 媒体榜单

  // 其它榜单
  int KTV_SONG = 6;
  int SPRITE_SONG = 9;
  int KING_SONG = 10;
}
