package com.sf.sofarmusic.enity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sufan on 16/12/9.
 */

public class FileItem implements Serializable {

    public boolean isSelected;
    public String path;          //完整路径
    public String parent;          //分割的父路径
    public String name;          //文件夹名(父目录的名字)
    public List<PlayItem> fileList;



}
