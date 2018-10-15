package com.sf.sofardemo.attr;

import android.view.View;

import com.sf.base.view.LoadView;
import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.loader.SkinManager;


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
