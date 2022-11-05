package com.sf.utility;


import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import androidx.core.graphics.drawable.DrawableCompat;

public class TintUtil {

    public static void setImageViewColor(ImageView imageView,int color){

        Drawable modeDrawable=imageView.getDrawable().mutate();
        Drawable temp= DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList=ColorStateList.valueOf(color);
        DrawableCompat.setTintList(temp,colorStateList);
        imageView.setImageDrawable(temp);
    }


}
