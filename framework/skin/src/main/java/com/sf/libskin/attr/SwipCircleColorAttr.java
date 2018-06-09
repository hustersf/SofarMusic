package com.sf.libskin.attr;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;


/**
 *
 */
public class SwipCircleColorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof SwipeRefreshLayout) {
            SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                if(SkinConfig.isSPColor){
                    color=SkinManager.getInstance().getSPColor(attrValueRefId);
                }
                swipeRefreshLayout.setColorSchemeColors(color);
            }
        }
    }
}
