package com.sf.sofarmusic.skin.attr;

import android.view.View;

import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.loader.SkinManager;
import com.sf.base.view.LoadView;


/**
 *
 */
public class LoadViewTextColorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof LoadView) {
            LoadView loadView = (LoadView) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
               loadView.setTextColor(color);
            }
        }
    }
}
