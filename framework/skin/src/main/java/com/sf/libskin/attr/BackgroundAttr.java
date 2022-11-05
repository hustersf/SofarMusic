package com.sf.libskin.attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.cardview.widget.CardView;

import com.sf.libskin.attr.base.SkinAttr;
import com.sf.libskin.config.SkinConfig;
import com.sf.libskin.loader.SkinManager;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:46
 */
public class BackgroundAttr extends SkinAttr {

  @Override
  public void apply(View view) {

    if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
      int color = SkinManager.getInstance().getColor(attrValueRefId);
      if (SkinConfig.isSPColor) {
        color = SkinManager.getInstance().getSPColor(attrValueRefId);
      }
      if (view instanceof CardView) {//这里对CardView特殊处理下
        CardView cardView = (CardView) view;
        //给CardView设置背景色应该使用cardBackgroundColor，直接使用background就会没有圆角效果
        cardView.setCardBackgroundColor(color);
      } else {
        view.setBackgroundColor(color);
      }
    } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
      Drawable bg = SkinManager.getInstance().getDrawable(attrValueRefId);
      view.setBackgroundDrawable(bg);
    }
  }
}
