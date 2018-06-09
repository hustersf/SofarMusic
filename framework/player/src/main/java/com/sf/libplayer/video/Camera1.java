package com.sf.libplayer.video;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import com.sf.libplayer.encoder.MediaEncoder;
import com.sf.libplayer.encoder.MediaVideoEncoder;

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
    private Camera.Parameters parameters;

    private Camera.Size previewSize;  //previewSize
    private Camera.Size pictureSize;  //pictureSize
    private Camera.Size videoSize;  //pictureSize

    @Override
    public void openCamera() {
        try {
            //打开摄像头
            if (mCamera == null) {
                mCamera = Camera.open(mCameraId);
            }

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

            //获取适合的尺寸
            parameterSizes(mCamera);

            //设置参数
            parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO); //聚焦,否则模糊
            parameters.setPreviewFormat(ImageFormat.NV21); //预览数据格式
            parameters.setPictureFormat(ImageFormat.JPEG);  //设置拍照图片格式
            parameters.setPreviewSize(previewSize.width, previewSize.height);   //预览视频尺寸
            parameters.setPictureSize(pictureSize.width, pictureSize.height); //拍照图片尺寸，解决图片模糊问题,不设置，默认取的是低分辨率
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
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        Log.d(TAG, "Camera1 has destroy");
    }


    @Override
    public void takePicture(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.d(TAG, "Camera1 takePicture path is empty");
            return;
        }
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(parameters);

        mPicturePath = path;
        Log.i(TAG, "Camera1 mPicturePath:" + mPicturePath);

        if (mCamera != null) {
            //拍照出来模糊，聚焦之后在拍
//            mCamera.autoFocus(new Camera.AutoFocusCallback() {
//                @Override
//                public void onAutoFocus(boolean success, Camera camera) {
//                    mCamera.takePicture(new Camera.ShutterCallback() {
//                        @Override
//                        public void onShutter() {
//                            //可以播放按下拍照的声音
//                        }
//                    }, new RawDataPicture(), new JEPGPicture());
//                }
//            });

            //有些情况autoFocus不回调
            mCamera.takePicture(new Camera.ShutterCallback() {
                @Override
                public void onShutter() {
                    //可以播放按下拍照的声音
                }
            }, new RawDataPicture(), new JEPGPicture());
        }
    }

    @Override
    public void startRecord(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.d(TAG, "Camera1 Record path is empty");
            return;
        }

        mVideoPath = path;
        try {
            setUpMediaRecorder();
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            Log.d(TAG, "Camera1 has start record");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Camera1 start failed:" + e.getMessage());
        }


    }

    @Override
    public void stopRecord() {
        if (mMediaRecorder != null) {
            mCamera.lock();
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            Log.d(TAG, "Camera1 has stop record");
        }
    }


    private void setUpMediaRecorder() {
        mCamera.unlock();

        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setOutputFile(mVideoPath);


        mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(videoSize.width, videoSize.height);

        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        if (mCameraId == 1) {
            mMediaRecorder.setOrientationHint(270);
        } else {
            mMediaRecorder.setOrientationHint(90);
        }

        Log.i(TAG, "Camera1 has set MediaRecorder VideoSize:" + videoSize.width + "*" + videoSize.height);
        Log.i(TAG, "Camera1 OutputFilePath:" + mVideoPath);

        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                Log.d(TAG, "MediaRecorder error:" + what + "-" + extra);
            }
        });

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(mListener!=null){
            mListener.onFrame(data);
        }
    }


    //获取各种尺寸
    private void parameterSizes(Camera camera) {
        List<Camera.Size> previewSizes = camera.getParameters().getSupportedPreviewSizes();
        List<Camera.Size> pictureSizes = camera.getParameters().getSupportedPictureSizes();
        List<Camera.Size> videoSizes = camera.getParameters().getSupportedVideoSizes();

        previewSize = previewSizes.get(0);
        pictureSize = pictureSizes.get(0);
        videoSize = videoSizes.get(0);

        Camera.Size psize;
        for (int i = 0; i < pictureSizes.size(); i++) {
            psize = pictureSizes.get(i);
            Log.i("pictureSize", psize.width + " x " + psize.height);
        }
        for (int i = 0; i < previewSizes.size(); i++) {
            psize = previewSizes.get(i);
            Log.i("previewSize", psize.width + " x " + psize.height);
        }

        for (int i = 0; i < videoSizes.size(); i++) {
            psize = previewSizes.get(i);
            Log.i("videoSize", psize.width + " x " + psize.height);
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
