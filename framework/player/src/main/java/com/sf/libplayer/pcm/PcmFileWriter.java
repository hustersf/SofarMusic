package com.sf.libplayer.pcm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.util.Log;

/**
 * Created by sufan on 2018/4/18.
 */

public class PcmFileWriter {

    private String TAG="PcmFileWriter";

    private String mFilePath;
    private FileOutputStream fos;

    public void openFile(String filePath){
        if(fos!=null){
            closeFile();
        }

        mFilePath=filePath;
        File file=new File(mFilePath);
        try {
            fos=new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void write(byte[] buffer,int offsetInBytes, int sizeInBytes){
        if(fos!=null){
            try {
                fos.write(buffer,offsetInBytes,sizeInBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Log.d(TAG,"PcmFile not open");

        }

    }


    public void closeFile(){
        if(fos!=null){
            try {
                fos.close();
                fos=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
