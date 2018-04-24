package com.sf.libplayer.video;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by sufan on 2018/4/22.
 */

public class Camera1 extends BaseCamera implements Camera.PreviewCallback {
    private static final String TAG = "Camera1";

    private Camera mCamera;
    private  Camera.Parameters parameters;

    private Camera.Size mSize;  //previewSize
    private Camera.Size nSize;  //pictureSize

    @Override
    public void openCamera() {
        //打开摄像头
        if (mCamera == null) {
            mCamera = Camera.open(mCameraId);
        }

        try {
            //设置预览
            if (mHolder == null && mTexture == null) {
                Log.d(TAG, "Camera1 can not preview");
                return;
            }

            if (mHolder != null) {
                mCamera.setPreviewDisplay(mHolder);
                Log.d(TAG, "Camera1 holder preview");
            }

            if (mTexture != null) {
                mCamera.setPreviewTexture(mTexture);
                Log.d(TAG, "Camera1 texture preview");
            }

            parameters(mCamera);

            //设置参数
            parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO); //聚焦,否则模糊
            parameters.setPreviewFormat(ImageFormat.NV21); //预览数据格式
            parameters.setPictureFormat(ImageFormat.JPEG);  //设置拍照图片格式
            parameters.setPreviewSize(mSize.width, mSize.height);   //预览视频尺寸
            parameters.setPictureSize(nSize.width, nSize.height); //拍照图片尺寸，解决图片模糊问题,不设置，默认取的是低分辨率
            //设置拍照后图片的方向，否则方向不对
            if (mCameraId == 0) {
                parameters.setRotation(90);  //后置
            } else {
                parameters.setRotation(270);  //前置
            }
            mCamera.setParameters(parameters);
            mCamera.setDisplayOrientation(90);   //预览角度默认0度，手机左侧为0

            //开启预览
            mCamera.setPreviewCallback(this);  //接收每一帧的数据
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Camera1 open failed");
        }
    }

    @Override
    public void destroyCamera() {
        if (mCamera == null) {
            return;
        }

        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;

        Log.d(TAG, "Camera1 has destroy");
    }


    @Override
    public void takePicture(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.d(TAG, "Camera1 takePicture path is empty");
            return;
        }
        //FOCUS_MODE_CONTINUOUS_VIDEO导致autoFocus回调失效
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);

        mPicturePath = path;

        if (mCamera != null) {
            //拍照出来模糊，聚焦之后在拍
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    mCamera.takePicture(new Camera.ShutterCallback() {
                        @Override
                        public void onShutter() {
                            //可以播放按下拍照的声音
                        }
                    }, new RawDataPicture(), new JEPGPicture());
                }
            });
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }


    //previewSize 1280*720  pictureSize 1280 x 720
    private void parameters(Camera camera) {
        List<Camera.Size> previewSizes = camera.getParameters().getSupportedPreviewSizes();
        List<Camera.Size> pictureSizes = camera.getParameters().getSupportedPictureSizes();

        mSize = previewSizes.get(0);
        nSize = pictureSizes.get(0);

        Camera.Size psize;
        for (int i = 0; i < pictureSizes.size(); i++) {
            psize = pictureSizes.get(i);
            Log.d("pictureSize", psize.width + " x " + psize.height);
        }
        for (int i = 0; i < previewSizes.size(); i++) {
            psize = previewSizes.get(i);
            Log.i("previewSize", psize.width + " x " + psize.height);
        }
    }

    //原始数据
    private class RawDataPicture implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    }


    //JEPG格式数据
    private class JEPGPicture implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                FileOutputStream fos = new FileOutputStream(mPicturePath);
                fos.write(data);
                fos.close();
                Toast.makeText(mContext, "图片保存至:" + mPicturePath, Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //拍照之后就停留在原位置了
                mCamera.startPreview();
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                mCamera.setParameters(parameters);
            }

        }
    }
}
