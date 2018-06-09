package com.sf.sofarmusic.enity;

import java.io.Serializable;

/**
 * Created by sufan on 17/3/23.
 */

public class MovieItem implements Serializable {

    private String imgUrl;
    private String chName;
    private String enName;
    private String showTime;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }
}
