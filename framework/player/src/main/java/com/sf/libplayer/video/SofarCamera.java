package com.sf.libplayer.video;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.SurfaceHolder;

import androidx.annotation.IntDef;

import com.sf.libplayer.callback.OnCameraFrameListener;

/**
 * Created by sufan on 2018/4/22.
 */

public class SofarCamera {
    public static final int CAMERA_FRONT=1;  //前置摄像头
    public static final int CAMERA_BACK=0;   //后置摄像头
    public static final int CAMERA1=1;   //旧api
    public static final int CAMERA2=2;   //新api

    private Context context;
    private BaseCamera baseCamera;
    private int cameraId;
    private int cameraApi;


    private SurfaceHolder holder;
    private SurfaceTexture texture;

    private SofarCamera(Builder builder){
        this.context=builder.context;
        this.cameraId=builder.cameraId;
        this.cameraApi=builder.cameraApi;
        this.holder=builder.holder;
        this.texture=builder.texture;
        initCamera();
    }

    private void initCamera(){
        if(cameraApi==CAMERA1){
            baseCamera=new Camera1();
        }else if(cameraApi==CAMERA2){
            baseCamera=new Camera2();
        }
        baseCamera.setContext(context);
        baseCamera.setCameraId(cameraId);
        baseCamera.setDisplay(holder);
        baseCamera.setDisplay(texture);
    }

    public void openCamera(){
        baseCamera.openCamera();
    }

    public void destroyCamera(){
        baseCamera.destroyCamera();
    }

    public void startRecord(String path){
        baseCamera.startRecord(path);
    }

    public void stopRecord(){
        baseCamera.stopRecord();
    }

    public void setOnFrameListener(OnCameraFrameListener listener){
        baseCamera.setListener(listener);
    }

    public void switchCamera(){
        if(cameraId==CAMERA_FRONT){
            cameraId=CAMERA_BACK;
        }else {
            cameraId=CAMERA_FRONT;
        }
        destroyCamera();
        initCamera();
        openCamera();
    }

    public void takePicture(String path){
       baseCamera.takePicture(path);
    }


    public Builder newBuilder() {
        return new Builder(this);
    }


    public static final class Builder{
        private Context context;
        private int cameraId;
        private int cameraApi;
        private SurfaceHolder holder;
        private SurfaceTexture texture;

        public Builder(){
            this.cameraId=1;
            this.cameraApi=1;
        }

        public Builder(SofarCamera camera){
            this.cameraId=camera.cameraId;
            this.cameraApi=camera.cameraApi;
            this.holder=camera.holder;
            this.texture=camera.texture;
        }

        public Builder context(Context context){
            this.context=context;
            return this;
        }

        public Builder cameraId(int cameraId){
            this.cameraId=cameraId;
            return this;
        }

        public Builder cameraApi(@CameraApi int cameraApi){
            this.cameraApi=cameraApi;
            return this;
        }

        public Builder holder(SurfaceHolder holder){
            this.holder=holder;
            return this;
        }

        public Builder texture(SurfaceTexture texture){
            this.texture=texture;
            return this;
        }


        public SofarCamera build() {
            return new SofarCamera(this);
        }
    }


    @IntDef({CAMERA1,CAMERA2})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraApi{

    }

}
