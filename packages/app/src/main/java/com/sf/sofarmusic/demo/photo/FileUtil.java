package com.sf.sofarmusic.demo.photo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sufan on 17/7/12.
 */

public class FileUtil {

    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }


    public static String getExternalStoragePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getImgFileName() {
        // 用日期作为文件名，确保唯一性
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd_HH-mm-ss");
        String fileName = formatter.format(date) + ".PNG";
        return fileName;
    }

    public static String generateImgePath() {
        String dirPath = getExternalStoragePath() + "/camera";
        File dirFile = new File(dirPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            dirFile.mkdirs();
        }
        return dirPath+"/"+getImgFileName();
    }

    public static String getMainPath(final Context context) {

        String path;
        StringBuffer sb = null;
        if (isSDCardAvailable()) {
            sb = new StringBuffer(Environment.getExternalStorageDirectory()
                    .getAbsolutePath());
        } else {
            sb = new StringBuffer(context.getFilesDir().getAbsolutePath());
        }
        sb.append(File.separator);
        sb.append("SofarMusic");
        sb.append(File.separator);
        path = sb.toString();
        final File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }
}
