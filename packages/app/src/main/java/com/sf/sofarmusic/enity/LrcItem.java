package com.sf.sofarmusic.enity;

import java.io.Serializable;

/**
 * Created by sufan on 16/11/30.
 */

public class LrcItem implements Serializable ,Comparable<LrcItem>{

    private String content;
    private int time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public int compareTo(LrcItem lrcItem) {
        if (this.getTime() > lrcItem.getTime()) {
            return 1;
        } else {
            return -1;
        }
    }
}
