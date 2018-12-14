package com.sf.sofarmusic.skin.attr;

import android.view.View;

import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;
import com.sf.widget.progress.MusicProgress;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:22:53
 */
public class MusicProgressAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof MusicProgress) {
            MusicProgress musicProgress = (MusicProgress) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                if(SkinConfig.isSPColor){
                    color= SkinManager.getInstance().getSPColor(attrValueRefId);
                }
                musicProgress.setReachColor(color);
            }
        }
    }
}
