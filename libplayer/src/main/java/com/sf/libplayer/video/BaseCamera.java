package com.sf.libplayer.video;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by sufan on 2018/4/23.
 */

public abstract class BaseCamera {

    private static final String TAG = "BaseCamera";

    protected Context mContext;
    protected int mCameraId = 1;  //1前置  0后置

    protected String mPicturePath;   //拍照后的图片存储路径
    protected String mVideoPath;   //录制视频的存储路径

    protected SurfaceHolder mHolder;  //SurfaceView预览
    protected SurfaceTexture mTexture;  //TextureView预览

    protected MediaRecorder mMediaRecorder;


    public void setContext(Context context) {
        mContext = context;
    }

    //设置前置或后置摄像头
    public void setCameraId(int cameraId) {
        if (cameraId != 1 && cameraId != 0) {
            Log.d(TAG, "error cameraId:" + cameraId + " BaseCamera cameraId only support 0 or 1 ");
            return;
        }
        mCameraId = cameraId;
    }

    //SurfaceView预览
    public void setDisplay(SurfaceHolder holder) {
        mHolder = holder;
    }

    //TextureView预览
    public void setDisplay(SurfaceTexture texture) {
        mTexture = texture;
    }


    public abstract void openCamera();

    public abstract void destroyCamera();

    public abstract void takePicture(String path);

    public abstract void startRecord(String path);

    public abstract void stopRecord();


}
