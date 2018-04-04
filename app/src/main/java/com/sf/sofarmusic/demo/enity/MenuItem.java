package com.sf.sofarmusic.demo.enity;

import java.io.Serializable;

/**
 * Created by sufan on 17/6/17.
 */

public class MenuItem implements Serializable {

    public String name;
    public String imgName;


    public boolean isTitle = false;
    public boolean isAdd = true;   //true显示加号，false显示减号
    public boolean isDashed = false;  //是否是占位符号


//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (!(obj instanceof MenuItem))
//            return false;
//        MenuItem other = (MenuItem) obj;
//        if (name != other.name)
//            return false;
//        return true;
//    }


}
