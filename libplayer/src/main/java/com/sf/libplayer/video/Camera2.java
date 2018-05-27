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
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sufan on 2018/4/22.
 */

public class Camera2 extends BaseCamera {

    private static final String TAG = "Camera2";

    private CameraManager mCameraManager;  //摄像机管理器
    private CameraDevice mCameraDevice;    //相机
    private CaptureRequest mCaptureRequest;  //一次请求，如预览，拍照，摄像
    private CameraCaptureSession mPreviewCaptureSession;  //一次会话可以包含多个请求

    private Size mPreviewSize;//预览的Size
    private Size mCaptureSize;//拍照Size
    private Size mVideoSize;//视频Size

    private ImageReader mPreviewReader;   //用于返回原始视频数据
    private ImageReader mPictureReader;   //用于返回拍照数据

    private HandlerThread mCameraThread;
    private Handler mCameraHandler;
    private Handler mMainHandler;


    private void initCamera() {
        try {
            if (mCameraManager == null) {
                mMainHandler = new Handler(Looper.getMainLooper());
                mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);

                //获取合适的尺寸
                parameterSizes();

                //设置数据着陆点
                setImageReader();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Camera2 init failed");
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void openCamera() {
        initCamera();
        try {
            //   startBackgroundThread();
            if (mCameraManager != null) {
                mCameraManager.openCamera(String.valueOf(mCameraId),
                        new CameraStateCallback(), mCameraHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.d(TAG, "Camera2 open failed:" + e.getMessage());
        }

    }


    @Override
    public void destroyCamera() {
        //  stopBackgroundThread();
        closeSession();

        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }

        if (mPictureReader != null) {
            mPictureReader.close();
            mPictureReader = null;
        }

        if (mPreviewReader != null) {
            mPreviewReader.close();
            mPreviewReader = null;
        }

        if(mCameraManager!=null){
            mCameraManager=null;
        }

        Log.d(TAG, "Camera2 has destroy");
    }


    //关闭一次会话
    private void closeSession() {
        if (mPreviewCaptureSession != null) {
            mPreviewCaptureSession.close();
            mPreviewCaptureSession = null;
        }
    }


    /**
     * startBackgroundThread 导致SurfaceView偶尔黑屏
     */
    private void startBackgroundThread() {
        mCameraThread = new HandlerThread("Camera2");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());

    }

    private void stopBackgroundThread() {
        mCameraThread.quitSafely();
        try {
            mCameraThread.join();
            mCameraThread = null;
            mCameraHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void takePicture(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.d(TAG, "Camera2 takePicture path is empty");
            return;
        }
        mPicturePath = path;
        Log.i(TAG, "Camera2 mPicturePath:" + mPicturePath);
        try {
            CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(mPictureReader.getSurface());
            //拍照
            mPreviewCaptureSession.capture(builder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.d(TAG, "Camera2 takePicture failed:" + e.getMessage());
        }
    }

    @Override
    public void startRecord(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.d(TAG, "Camera2 Record path is empty");
            return;
        }
        closeSession();
        mVideoPath = path;
        setUpMediaRecorder();
        try {
            final CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            Surface previewSurface = getSurface();
            builder.addTarget(previewSurface);
            Surface recordSurface = mMediaRecorder.getSurface();
            builder.addTarget(recordSurface);
            mCaptureRequest = builder.build();

            List<Surface> surfaces = Arrays.asList(previewSurface, recordSurface);
            //   List<Surface> surfaces = Arrays.asList(previewSurface);

            //摄像
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mPreviewCaptureSession = session;
                    //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
                    try {
                        mPreviewCaptureSession.setRepeatingRequest(mCaptureRequest, null, mCameraHandler);
                        mMediaRecorder.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, mCameraHandler);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Camera2 startRecord failed:" + e.getMessage());
        }

    }

    @Override
    public void stopRecord() {
        try {
            //解决startPreview failed:Illegal state encountered in camera service
            //https://stackoverflow.com/questions/27907090/android-camera-2-api
            if (mPreviewCaptureSession != null) {
                mPreviewCaptureSession.stopRepeating();
                mPreviewCaptureSession.abortCaptures();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            Log.d(TAG, "Camera2 has stop record");
        }
       // startPreview();
        destroyCamera();
        openCamera();
    }


    //打开相机的回调
    private class CameraStateCallback extends CameraDevice.StateCallback {

        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.d(TAG, "Camera2 onOpened");
            mCameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            Log.d(TAG, "Camera2 onDisconnected");
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            Log.d(TAG, "Camera2 onError：" + error);
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }
    }

    private void startPreview() {
        closeSession();
        try {
            //创建预览的CaptureRequest
            CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface surface = getSurface();
            builder.addTarget(surface);
            builder.addTarget(mPreviewReader.getSurface());
            mCaptureRequest = builder.build();

            //预览+预览数据+拍照数据
            List<Surface> surfaces = Arrays.asList(surface, mPreviewReader.getSurface(), mPictureReader.getSurface());

            //创建相机捕获会话
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mPreviewCaptureSession = session;
                    try {
                        mPreviewCaptureSession.setRepeatingRequest(mCaptureRequest, null, mCameraHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, mCameraHandler);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Camera2 startPreview failed:" + e.getMessage());
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


    //获取合适的尺寸
    private void parameterSizes() {
        try {
            CameraCharacteristics characteristics = mCameraManager.
                    getCameraCharacteristics(String.valueOf(mCameraId));
            //  获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            //获取相机支持的最大拍照尺寸
            mCaptureSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getHeight() * rhs.getWidth());
                }
            });

            //获取预览尺寸
            mPreviewSize = new Size(1280, 720);

            //获取视频录制的尺寸
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setImageReader() {
        try {
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


            //原始视频数据返回  源码NV21 format is not supported
            mPreviewReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(),
                    ImageFormat.YUV_420_888, 2);
            mPreviewReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    //将这帧数据转成字节数组，类似于Camera1的PreviewCallback回调的预览帧数据
                    Image image = reader.acquireLatestImage();
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    onPreviewFrame(data);
                    image.close();
                }
            }, mCameraHandler);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "setImageReader ex:" + e.getMessage());
        }
    }

    private void setUpMediaRecorder() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setOutputFile(mVideoPath);


        mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());

        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        // mMediaRecorder.setVideoSize(1920,1080);
        if (mCameraId == 1) {
            mMediaRecorder.setOrientationHint(270);
        } else {
            mMediaRecorder.setOrientationHint(90);
        }
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Camera2 has set MediaRecorder VideoSize:" + mVideoSize.getWidth() + "*" + mVideoSize.getHeight());
        Log.i(TAG, "Camera2 OutputFilePath:" + mVideoPath);

        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                Log.d(TAG, "MediaRecorder error:" + what + "-" + extra);
            }
        });

    }


    private void savePicture(ImageReader reader) {
        Image image = reader.acquireLatestImage();
        Log.i(TAG, "picture image format: " + image.getFormat());
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        try {
            FileOutputStream fos = new FileOutputStream(mPicturePath);
            fos.write(data);
            fos.close();
            Toast.makeText(mContext, "图片保存至:" + mPicturePath, Toast.LENGTH_SHORT).show();
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

    private void onPreviewFrame(byte[] data) {
        if(mListener!=null){
            mListener.onFrame(data);
        }
    }

    private void print(Image image) {
        Log.i(TAG, "preview image format: " + image.getFormat());
        Image.Plane[] planes = image.getPlanes();
        for (int i = 0; i < planes.length; i++) {
            ByteBuffer iBuffer = planes[i].getBuffer();
            int iSize = iBuffer.remaining();
            Log.i(TAG, "pixelStride  " + planes[i].getPixelStride());
            Log.i(TAG, "rowStride   " + planes[i].getRowStride());
            Log.i(TAG, "width  " + image.getWidth());
            Log.i(TAG, "height  " + image.getHeight());
            Log.i(TAG, "Finished reading data from plane  " + i);

        }
    }

    /**
     * In this sample, we choose a video size with 3x4 aspect ratio. Also, we don't use sizes
     * larger than 1080p, since MediaRecorder cannot handle such a high-resolution video.
     *
     * @param choices The list of available sizes
     * @return The video size
     */
    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }

}
