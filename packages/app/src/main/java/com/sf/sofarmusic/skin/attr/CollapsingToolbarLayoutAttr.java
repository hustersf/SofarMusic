package com.sf.sofarmusic.skin.attr;

import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;

import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;

/**
 * Created by sufan on 17/7/5.
 */

public class CollapsingToolbarLayoutAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof CollapsingToolbarLayout) {
            CollapsingToolbarLayout collapsingLayout = (CollapsingToolbarLayout) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                if (SkinConfig.isSPColor) {
                    color = SkinManager.getInstance().getSPColor(attrValueRefId);
                }
                collapsingLayout.setContentScrimColor(color);

            }
        }
    }
}
