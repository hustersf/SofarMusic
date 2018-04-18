package com.sf.libplayer.pcm;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sufan on 2018/4/18.
 */

public class PcmFileReader {

    private String TAG="PcmFileReader";

    private String mFilePath;
    private FileInputStream fis;

    public void openFile(String filePath){
        if(fis!=null){
            closeFile();
        }

        mFilePath=filePath;
        File file=new File(mFilePath);
        try {
            fis=new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public int read(byte[] buffer,int offsetInBytes, int sizeInBytes){
        int res=-1;
        if(fis!=null){
            try {
                res=fis.read(buffer,offsetInBytes,sizeInBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Log.d(TAG,"PcmFile not open");
        }
        return res;
    }


    public void closeFile(){
        if(fis!=null){
            try {
                fis.close();
                fis=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




}
