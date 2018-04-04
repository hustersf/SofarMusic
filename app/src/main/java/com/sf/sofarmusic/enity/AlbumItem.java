package com.sf.sofarmusic.enity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sufan on 16/12/2.
 */

public class AlbumItem implements Serializable {

    public boolean isSelected;
    public long albumId;
    public String albumName;
    public String artistName;
    public String imgUri;

    public List<PlayItem> albumList;



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AlbumItem))
            return false;
        AlbumItem other = (AlbumItem) obj;
        if (albumId != other.albumId)
            return false;
        return true;
    }
}
