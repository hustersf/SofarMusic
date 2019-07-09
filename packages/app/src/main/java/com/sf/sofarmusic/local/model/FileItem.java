package com.sf.sofarmusic.local.model;

import com.sf.sofarmusic.model.Song;
import java.io.Serializable;
import java.util.List;

/**
 * Created by sufan on 16/12/9.
 */

public class FileItem implements Serializable {

  public boolean selected;  //此目录是否被选中
  public String path; // 此目录的路径
  public String parent; // 此目录的父目录
  public String name; // 此目录的名字
  public List<Song> songs;  //此目录下的歌曲列表

}
