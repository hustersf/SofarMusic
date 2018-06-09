package com.sf.sofarmusic.enity;

import java.io.Serializable;

/**
 * Created by sufan on 16/11/5.
 */

public class SkinItem implements Serializable {

    public String imgName;     //图片名字
    public String imgUrl;      //图片url
    public String des;         //描述
    public boolean isTitle;    //是否是标题
    public boolean isSelected;     //
    public String  status;    //状态，使用中，已下载等


}
