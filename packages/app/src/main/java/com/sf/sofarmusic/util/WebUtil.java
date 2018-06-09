package com.sf.sofarmusic.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.os.Build;
import android.webkit.WebView;
import android.widget.Toast;

import com.sf.sofarmusic.demo.photo.FileUtil;

import java.io.FileOutputStream;

import static com.sf.libnet.http.HttpConfig.context;

/**
 * Created by sufan on 17/7/28
 * 对webview进行快照
 */

public class WebUtil {

    private Context mContext;
    private WebView wv_web;
    public  WebUtil(WebView wv_web,Context context){
        this.wv_web=wv_web;
        mContext=context;

    }

    private void saveBitmap() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            saveBitmap1();
        } else {
            saveBitmap2();
        }
    }

    private void saveBitmap1() {
        Picture pic = wv_web.capturePicture();
        int width = pic.getWidth();
        int height = pic.getHeight();
        if (width > 0 && height > 0) {
            int dx = 15;// 左右边距15px
            int dy = 220;// 下边距220px
            int dh = 944;// 全长944px
            int dw = 360;// 全宽360px
            float screenshot_x = width * dx / dw;
            float screenshot_y = height * dx / dh;
            float screenshot_width = width - 2 * screenshot_x;
            double screenshot_height = height - screenshot_y - height * dy / dh;

            Bitmap bmp = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            pic.draw(canvas);
            try {
                String fileName = FileUtil.getMainPath(context)
                        + System.currentTimeMillis() + ".png";
                FileOutputStream fos = new FileOutputStream(fileName);
                if (fos != null) {
                    bmp = CreatMatrixBitmap(bmp, (int) screenshot_x,
                            (int) screenshot_y, (int) screenshot_width,
                            (int) screenshot_height);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.close();
                }
                Toast.makeText(mContext,
                        "截图成功，文件名是：" + fileName, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }
    }

    private void saveBitmap2() {
        Picture pic = wv_web.capturePicture();
        int width = pic.getWidth();
        int height = pic.getHeight();
        if (width > 0 && height > 0) {
            int dx = 15;// 左右边距15px
            int dy = 220;// 下边距220px
            int dh = 944;// 全长944px
            int dw = 360;// 全宽360px
            float screenshot_x = width * dx / dw;
            float screenshot_y = height * dx / dh;
            float screenshot_width = width - 2 * screenshot_x;
            double screenshot_height = height - screenshot_y - height * dy / dh;

            wv_web.setDrawingCacheEnabled(true);
            wv_web.buildDrawingCache();
            Bitmap bmp = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            Paint paint = new Paint();
            int bmHight = bmp.getHeight();
            canvas.drawBitmap(bmp, 0, bmHight, paint);
            wv_web.draw(canvas);
            try {
                String fileName = FileUtil.getMainPath(context)
                        + System.currentTimeMillis() + ".png";
                FileOutputStream fos = new FileOutputStream(fileName);
                if (fos != null) {
                    bmp = CreatMatrixBitmap(bmp, (int) screenshot_x,
                            (int) screenshot_y, (int) screenshot_width,
                            (int) screenshot_height);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.close();
                }
                Toast.makeText(mContext,
                        "截图成功，文件名是：" + fileName, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }
    }

    /**
     * 创建一个缩小或放大的新图片
     *
     */
    private Bitmap CreatMatrixBitmap(Bitmap bitMap, int x, int y, int bitWidth,
                                     int bitHeight) {

        bitMap = Bitmap.createBitmap(bitMap, x, y, bitWidth, bitHeight);
        return bitMap;
    }



}
