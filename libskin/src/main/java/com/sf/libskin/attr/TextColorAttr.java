package com.sf.libskin.attr;

import android.view.View;
import android.widget.TextView;

import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:22:53
 */
public class TextColorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                //  tv.setTextColor(SkinManager.getInstance().getColorStateList(attrValueRefId));
               // int color = SkinManager.getInstance().getColor(attrValueRefId);
                if (SkinConfig.isSPColor) {
                    int color = SkinManager.getInstance().getSPColor(attrValueRefId);
                    tv.setTextColor(color);
                }else {
                    tv.setTextColor(SkinManager.getInstance().getColorStateList(attrValueRefId));
                }

            }
        }
    }
}
