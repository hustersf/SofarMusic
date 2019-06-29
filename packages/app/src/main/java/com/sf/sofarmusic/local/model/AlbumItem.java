package com.sf.sofarmusic.local.model;

import com.sf.sofarmusic.model.Song;
import java.io.Serializable;
import java.util.List;

/**
 * Created by sufan on 16/12/2.
 */
public class AlbumItem implements Serializable {

  public boolean selected;
  public String albumId;
  public String albumName;
  public String authorName;
  public String imgUri;

  public List<Song> songs; // 专辑名下的歌曲列表

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof AlbumItem))
      return false;
    AlbumItem other = (AlbumItem) obj;
    if (!albumId.equals(other.albumId))
      return false;
    return true;
  }
}
