package com.sf.sofardemo.attr;

import android.view.View;

import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;
import com.sf.widget.flowlayout.FlowTagList;

/**
 * Created by sufan on 17/6/26.
 */

public class FlowLayoutAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof FlowTagList) {
            FlowTagList flowTagList = (FlowTagList) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                if(SkinConfig.isSPColor){
                    color=SkinManager.getInstance().getSPColor(attrValueRefId);
                }
                flowTagList.setColor(color);
            }
        }
    }
}
