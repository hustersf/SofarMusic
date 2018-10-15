package com.sf.demo.picker.city;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/6/20.
 * 城市
 * 成员变量的名字必须和city.json中的名字一致
 */

public class City {

    public String areaName;

    public List<County> counties = new ArrayList<>();

}
