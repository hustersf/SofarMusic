package com.sf.base.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;

/**
 * Created by sufan on 2017/11/2.
 * <p>
 * 内部存储
 * getCacheDir()    用于获取/data/data/packagename/cache目录
 * getFilesDir()       用于获取/data/data/packagename/files目录
 * getDir(path,Context.MODE_PRIVATE)    /data/data/packagename/app_path/path目录
 * <p>
 * 外部存储
 * getExternalFilesDir()     用于获取/Android/data/packagename/files目录   对应应用详情里面的清除数据
 * getExternalCacheDir()    用于获取/Android/data/packagename/cache目录   对应应用详情里面的清除缓存
 *
 * File 分为目录和文件
 * if(!dir.exists) dir.mkdirs  创建目录，必加
 * if(!file.exists) file.createNewFile()  创建文件，可有可无
 */

public class FileUtil {

    //SD卡是否可用
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    public static String getRootDir(Context context){
       return Environment.getExternalStorageDirectory().getAbsolutePath();
    }


    /**
     * @param context
     * @return 存放crash日志的目录
     */
    public static String getCrashDir(Context context) {
        File crashDir = new File(getCacheDir(context), "crash");
        if (!crashDir.exists()) {
            crashDir.mkdirs();
        }
        return crashDir.getAbsolutePath();
    }

    /**
     * 得到缓存歌词的目录
     *
     * @param context
     * @return 存放歌词的目录
     */
    public static String getLrcDir(Context context) {
        File lrcDir = new File(getCacheDir(context), "lrc");
        if (!lrcDir.exists()) {
            lrcDir.mkdirs();
        }
        return lrcDir.getAbsolutePath();
    }

    public static String getAudioDir(Context context){

        File audioDir = new File(getCacheDir(context), "audio");
        if (!audioDir.exists()) {
            audioDir.mkdirs();
        }
        return audioDir.getAbsolutePath();
    }

    public static String getPictureDir(Context context){

        File pictureDir = new File(getCacheDir(context), "picture");
        if (!pictureDir.exists()) {
            pictureDir.mkdirs();
        }
        return pictureDir.getAbsolutePath();
    }


    /**
     * 得到手机的缓存目录
     *
     * @param context
     * @return
     */
    private static File getCacheDir(Context context) {

        // 获取保存的文件夹路径
        File file;
        if (isSDCardEnable()) {
            // 有SD卡就保存到sd卡
            file = context.getExternalCacheDir();
        } else {
            // 没有就保存到内部储存
            file = context.getCacheDir();
        }
        return file;
    }


    public static void writeToFile(File dstFile, InputStream dataSource) {
        try {
            if (!dstFile.exists()) {
                dstFile.createNewFile();
            }
            FileOutputStream fos=new FileOutputStream(dstFile);
            byte[] buffer=new byte[1024];
            int len;
            while ((len = dataSource.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                fos.flush();
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
