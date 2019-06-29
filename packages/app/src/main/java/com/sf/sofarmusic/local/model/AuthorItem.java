package com.sf.sofarmusic.local.model;

import com.sf.sofarmusic.model.Song;
import java.io.Serializable;
import java.util.List;

/**
 * Created by sufan on 16/11/25.
 * 艺术家是无法获取本地图片的，图片都是从后台请求的
 */
public class AuthorItem implements Serializable {
  public boolean selected;
  public String name; // 歌手名字
  public String authorId; // 本地歌手id
  public String mediumUrl; // 歌手中图
  public String extraLargeUrl;// 歌手大图

  public List<Song> songs; // 歌手名下的歌曲列表

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof AuthorItem))
      return false;
    AuthorItem other = (AuthorItem) obj;
    if (!authorId.equals(other.authorId))
      return false;
    return true;
  }
}
