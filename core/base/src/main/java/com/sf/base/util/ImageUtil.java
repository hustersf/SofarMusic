package com.sf.base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.sf.base.R;
import com.sf.utility.ResUtil;

/**
 * Created by sufan on 17/6/17.
 */

public class ImageUtil {


    /**
     * 获取view截图
     *
     * @param view 需要截图的view
     * @return Drawable
     */
    public Drawable getViewScreenShot(View view) {
        Bitmap obmp = null;
        BitmapDrawable transitionsDrawable = null;
        view.setDrawingCacheEnabled(true);// 设置图片缓冲区
        if (view.getDrawingCache() != null) {
            obmp = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);// 清空图片缓冲区
            transitionsDrawable = new BitmapDrawable(obmp);
            obmp.recycle();
        }
        return transitionsDrawable;
    }

    public static void setDrawableByName(Context context, String name, ImageView imageView) {
        int imgId = ResUtil.getDrawableId(context, name);
        Drawable drawable = null;
        try {
            drawable = context.getResources().getDrawable(imgId);
        } catch (Exception e) {
        }

        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setImageResource(R.drawable.music);
        }

    }

    public static Bitmap getReverseBitmapById(int resId, Context context){
        Bitmap sourceBitmap= BitmapFactory.decodeResource(context.getResources(),resId);
        Matrix matrix=new Matrix();
        matrix.setScale(1,-1);
        Bitmap inverseBitmap=Bitmap.createBitmap(sourceBitmap,0,sourceBitmap.getHeight()/2,sourceBitmap.getWidth(),sourceBitmap.getHeight()/3,matrix,false);
        Bitmap groupbBitmap=Bitmap.createBitmap(sourceBitmap.getWidth(),sourceBitmap.getHeight()+sourceBitmap.getHeight()/3+60,sourceBitmap.getConfig());
        Canvas gCanvas=new Canvas(groupbBitmap);
        gCanvas.drawBitmap(sourceBitmap,0,0,null);
        gCanvas.drawBitmap(inverseBitmap,0,sourceBitmap.getHeight()+50,null);
        Paint paint=new Paint();
        Shader.TileMode tileMode= Shader.TileMode.CLAMP;
        LinearGradient shader=new LinearGradient(0,sourceBitmap.getHeight()+50,0,
                groupbBitmap.getHeight(), Color.BLACK,Color.TRANSPARENT,tileMode);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        gCanvas.drawRect(0,sourceBitmap.getHeight()+50,sourceBitmap.getWidth(),groupbBitmap.getHeight(),paint);
        return groupbBitmap;
    }


}

