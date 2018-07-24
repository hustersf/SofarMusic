package com.sf.sofardemo.attr;

import android.view.View;

import com.sf.base.view.LoadView;
import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;


/**
 *  SkinConfig.skinColor=color;
 *  任意选用一个SkinAttr为主题颜色赋值
 */
public class LoadViewAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof LoadView) {
            LoadView loadView = (LoadView) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                if(SkinConfig.isSPColor){
                    color= SkinManager.getInstance().getSPColor(attrValueRefId);
                }
                loadView.setLoadColor(color);

                //重新初始化主题颜色（很重要）
                SkinConfig.skinColor=color;
            }
        }
    }
}
