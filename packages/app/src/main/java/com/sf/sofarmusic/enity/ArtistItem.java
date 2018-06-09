package com.sf.sofarmusic.enity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sufan on 16/11/25.
 * 艺术家是无法获取本地图片的，图片都是从后台请求的
 */

public class ArtistItem implements Serializable {
    public boolean isSelected;
    public String tinguid;   //歌手id,接口中的唯一标识
    public String name;   //歌手名字
    public String imgUrl;   //歌手照片
    public String updateTime;   //更新时间
    public boolean isImportant;  //是否标记
    public long artistId;    //本地音乐id
    public List<PlayItem> artistList;

    public String mediumUrl;
    public String extraLargeUrl;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ArtistItem))
            return false;
        ArtistItem other = (ArtistItem) obj;
        if (artistId != other.artistId)
            return false;
        return true;
    }
}
