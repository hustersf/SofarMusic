package com.sf.sofarmusic.skin.attr;

import android.view.View;

import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;
import com.sf.sofarmusic.play.lrc.LrcView;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:22:53
 */
public class LrcViewAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof LrcView) {
            LrcView lrcView = (LrcView) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                if(SkinConfig.isSPColor){
                    color= SkinManager.getInstance().getSPColor(attrValueRefId);
                }
                lrcView.setCurrnetColor(color);
            }
        }
    }
}
