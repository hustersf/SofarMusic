package com.sf.libskin.attr.base;

import java.util.HashMap;

import com.sf.libskin.attr.BackgroundAttr;
import com.sf.libskin.attr.SwipCircleColorAttr;
import com.sf.libskin.attr.TextColorAttr;
import com.sf.libskin.utils.SkinL;



/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:9:47
 */
public class AttrFactory {

    private static String TAG = "AttrFactory";

    public static HashMap<String, SkinAttr> mSupportAttr = new HashMap<>();

    static {
        mSupportAttr.put("background", new BackgroundAttr());
        mSupportAttr.put("textColor", new TextColorAttr());
        mSupportAttr.put("swipCircleColor", new SwipCircleColorAttr());
    }


    public static SkinAttr get(String attrName, int attrValueRefId, String attrValueRefName, String typeName) {
        SkinL.i(TAG, "attrName:" + attrName);
        SkinAttr mSkinAttr = mSupportAttr.get(attrName).clone();
        if (mSkinAttr == null) return null;
        mSkinAttr.attrName = attrName;
        mSkinAttr.attrValueRefId = attrValueRefId;
        mSkinAttr.attrValueRefName = attrValueRefName;
        mSkinAttr.attrValueTypeName = typeName;
        return mSkinAttr;
    }

    /**
     * 检测属性是否被支持
     *
     * @param attrName
     * @return true : supported <br>
     * false: not supported
     */
    public static boolean isSupportedAttr(String attrName) {
        return mSupportAttr.containsKey(attrName);
    }

    /**
     * 增加对换肤属性的支持
     *
     * @param attrName 属性名
     * @param skinAttr 自定义的属性
     */
    public static void addSupportAttr(String attrName, SkinAttr skinAttr) {
        mSupportAttr.put(attrName, skinAttr);
    }
}
