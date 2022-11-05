package com.sf.sofarmusic.skin.attr;

import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;


/**
 * tabLayout指示器的颜色
 */
public class TabLayoutIndicatorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof TabLayout) {
            TabLayout tabLayout = (TabLayout) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                if(SkinConfig.isSPColor){
                    color=SkinManager.getInstance().getSPColor(attrValueRefId);
                }
                tabLayout.setSelectedTabIndicatorColor(color);
            }
        }
    }
}
