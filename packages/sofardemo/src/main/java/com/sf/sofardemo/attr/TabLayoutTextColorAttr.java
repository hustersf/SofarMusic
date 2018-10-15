package com.sf.sofardemo.attr;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;


/**
 * tabLayout字体的颜色
 */
public class TabLayoutTextColorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof TabLayout) {
            TabLayout tabLayout = (TabLayout) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                tabLayout.setTabTextColors(color, SkinConfig.skinColor);
            }
        }
    }
}
