package com.sf.demo.list.page;

import java.io.Serializable;
import java.util.List;


public class DividePartBean implements Serializable {

    public int column;                           //列数
    public List<DividePartSubBean> list;             //子菜单
    public int row;                               //行数
    public String background;                        //背景色

    public String title;

}
