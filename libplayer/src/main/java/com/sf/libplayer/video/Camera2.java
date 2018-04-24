package com.sf.libplayer.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by sufan on 2018/4/22.
 */

public class Camera2 extends BaseCamera {

    private static final String TAG = "Camera2";

    private CameraManager mCameraManager;  //摄像管理器
    private CameraDevice mCameraDevice;
    private CaptureRequest mCaptureRequest;
    private CameraCaptureSession mCameraCaptureSession;
    private Handler mCameraHandler;
    private Handler mMainHandler;
    private ImageReader mPictureReader;   //用于拍照
    private ImageReader mRecordReader;    //用于摄像
    private Size mPreviewSize;//预览的Size
    private Size mCaptureSize;//拍照Size

    @SuppressLint("MissingPermission")
    @Override
    public void openCamera() {
        try {
            if (mCameraManager == null) {
                HandlerThread handlerThread = new HandlerThread("Camera2");
                handlerThread.start();
                mCameraHandler = new Handler(handlerThread.getLooper());
                mMainHandler=new Handler(Looper.getMainLooper());

                mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
                setImageReader();
                mCameraManager.openCamera(String.valueOf(mCameraId),
                        new CameraStateCallback(), mCameraHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.d(TAG, "Camera2 open failed");
        }

    }


    @Override
    public void destroyCamera() {
        if (mCameraHandler != null) {
            mCameraHandler.removeCallbacks(null);
            mCameraHandler = null;
        }

        if (mCameraCaptureSession != null) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }

        if (mCaptureRequest != null) {
            mCaptureRequest = null;
        }

        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }

        if (mCameraManager != null) {
            mCameraManager = null;
        }

    }

    @Override
    public void takePicture(String path) {
        mPicturePath = path;
        try {
            CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(mPictureReader.getSurface());
            //拍照
            mCameraCaptureSession.capture(builder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    //打开相机的回调
    private class CameraStateCallback extends CameraDevice.StateCallback {

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }
    }

    //
    private void startPreview() {
        try {
            //创建预览的CaptureRequest
            CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface surface = getSurface();
            builder.addTarget(surface);
            mCaptureRequest = builder.build();

            //创建相机捕获会话
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mPictureReader.getSurface()),
                    new CameraSessionCallback(), mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.d(TAG, "Camera2 startPreview failed");
        }
    }


    //获取预览界面
    private Surface getSurface() {
        Surface surface = null;
        if (mHolder == null && mTexture == null) {
            Log.d(TAG, "Camera2 can not preview");
            return null;
        }
        if (mHolder != null) {
            surface = mHolder.getSurface();
            Log.d(TAG, "Camera2 holder preview");
        }

        if (mTexture != null) {
            surface = new Surface(mTexture);
            Log.d(TAG, "Camera2 texture preview");
        }
        return surface;
    }


    //相机会话
    private class CameraSessionCallback extends CameraCaptureSession.StateCallback {

        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            try {
                mCameraCaptureSession = session;
                //预览
                mCameraCaptureSession.setRepeatingRequest(mCaptureRequest, null, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    }

    private void setImageReader() {
        try {
            CameraCharacteristics characteristics = mCameraManager.
                    getCameraCharacteristics(String.valueOf(mCameraId));
            //  获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            //   获取相机支持的最大拍照尺寸
            mCaptureSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getHeight() * rhs.getWidth());
                }
            });

            //2代表ImageReader中最多可以获取两帧图像流
            mPictureReader = ImageReader.newInstance(mCaptureSize.getWidth(), mCaptureSize.getHeight(),
                    ImageFormat.JPEG, 2);
            mPictureReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    //将这帧数据转成字节数组，类似于Camera1的PreviewCallback回调的预览帧数据
                    savePicture(reader);
                }
            }, mCameraHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void savePicture(ImageReader reader) {
        Image image = reader.acquireLatestImage();
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        try {
            FileOutputStream fos = new FileOutputStream(mPicturePath);
            fos.write(data);
            fos.close();
            Toast.makeText(mContext,"图片保存至:"+mPicturePath,Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "Camera2 FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Camera2 IOException");
        } finally {
            image.close();
        }
    }
}
