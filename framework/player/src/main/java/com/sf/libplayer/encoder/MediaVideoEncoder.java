package com.sf.libplayer.encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.text.TextUtils;
import android.util.Log;

import com.sf.libplayer.callback.OnVideoEncodedListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by sufan on 2018/4/29.
 */

public class MediaVideoEncoder {

    private static final String TAG = "MediaVideoEncoder";

    //MediaFormat参数
    private static final String MIME_TYPE = "video/avc";
    private static final int COLOR_FORMAT = MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar;
    private static final int FRAME_RATE = 25;
    private static final float BPP = 0.25f;
    private static final int I_FRAME_INTERVAL = 1;
    private static final int TIMEOUT_USEC = 10000;    // 12[msec]

    private MediaCodec mMediaCodec;
    private int mWidth, mHeight;

    private OnVideoEncodedListener mListener;
    private String mH264Path;
    private FileOutputStream mFileOutputStream;

    private boolean isRunning = false;  //线程是否正在运行
    private ArrayBlockingQueue<byte[]> yuvQueue = new ArrayBlockingQueue<byte[]>(10);


    public MediaVideoEncoder(int width, int height) {
        mWidth = width;
        mHeight = height;
        init();
    }

    public void setH264Path(String path){
        mH264Path=path;
        if(!TextUtils.isEmpty(mH264Path)){
            try {
                mFileOutputStream=new FileOutputStream(mH264Path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //初始化参数
    private void init() {
        try {
            mMediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
            final MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, COLOR_FORMAT);
            format.setInteger(MediaFormat.KEY_BIT_RATE, calcBitRate());
            format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, I_FRAME_INTERVAL);
            mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            // get Surface for encoder input
            // this method only can call between #configure and #readyStart
//        mSurface = mMediaCodec.createInputSurface();	// API >= 18
            mMediaCodec.start();
            Log.d(TAG, "MediaCodec init success");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "MediaCodec init failed:" + e.getMessage());
        }

    }

    private int calcBitRate() {
        final int bitrate = (int) (BPP * FRAME_RATE * mWidth * mHeight);
        Log.i(TAG, String.format("bitrate=%5.2f[Mbps]", bitrate / 1024f / 1024f));
        return bitrate;
    }

    public void putYUVData(byte[] buffer) {
        if (yuvQueue.size() >= 10) {
            yuvQueue.poll();
        }
        yuvQueue.add(buffer);
    }


    //开启线程进行编码
    public void startEncoderThread() {
        isRunning = true;
        Thread encoderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    if (yuvQueue.size() > 0) {
                        //从缓冲队列中取出一帧
                        byte[] input = yuvQueue.poll();
                        byte[] yuv420sp = new byte[mWidth * mHeight * 3 / 2];
                        //把待编码的视频帧转换为YUV420格式
                        NV21ToNV12(input, yuv420sp, mWidth, mHeight);
                        input = yuv420sp;
                        //编码
                        encodeData(input, 0, input.length, 0);
                    }
                }
            }
        });
        encoderThread.start();

    }


    //关闭编码的线程
    public void stopThread() {
        isRunning = false;
        stopEncoder();
    }


    private void stopEncoder() {
        if (mMediaCodec != null) {
            mMediaCodec.stop();
            mMediaCodec.release();
        }
        if(mFileOutputStream!=null){
            try {
                mFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeData(byte[] data, int offset, int length, int flag) {
        if(data==null){
            return;
        }
        ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();//拿到输入缓冲区,用于传送数据进行编码
        int inputBufferIndex = mMediaCodec.dequeueInputBuffer(-1);//-1代表无线等待
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(data);
            mMediaCodec.queueInputBuffer(inputBufferIndex, offset, length, System.nanoTime() / 1000, flag);
        }

        ByteBuffer[] outputBuffers = mMediaCodec.getOutputBuffers();
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
        while (outputBufferIndex >= 0) {
            Log.d(TAG, "Get H264 Buffer Success! flag = " + bufferInfo.flags + ",pts=" + bufferInfo.presentationTimeUs);
            ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
            outputBuffer.position(bufferInfo.offset);
            outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
            byte[] frame = new byte[bufferInfo.size];
            outputBuffer.get(frame, 0, bufferInfo.size);
            if (mListener != null) {
                mListener.onFrameEncoded(frame);
            }

            if(!TextUtils.isEmpty(mH264Path)){
                try {
                    mFileOutputStream.write(frame);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);
            outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
        }

    }


    public void setOnVideoEncodedListener(OnVideoEncodedListener listener) {
        mListener = listener;
    }

    private void NV21toI420SemiPlanar(byte[] nv21bytes, byte[] i420bytes, int width, int height) {
        final int iSize = width * height;
        System.arraycopy(nv21bytes, 0, i420bytes, 0, iSize);

        for (int iIndex = 0; iIndex < iSize / 2; iIndex += 2) {
            i420bytes[iSize + iIndex / 2 + iSize / 4] = nv21bytes[iSize + iIndex]; // U
            i420bytes[iSize + iIndex / 2] = nv21bytes[iSize + iIndex + 1]; // V
        }
    }

    private void NV21ToNV12(byte[] nv21, byte[] nv12, int width, int height) {
        if (nv21 == null || nv12 == null) return;
        int framesize = width * height;
        int i = 0, j = 0;
        System.arraycopy(nv21, 0, nv12, 0, framesize);
        for (i = 0; i < framesize; i++) {
            nv12[i] = nv21[i];
        }
        for (j = 0; j < framesize / 2; j += 2) {
            nv12[framesize + j - 1] = nv21[j + framesize];
        }
        for (j = 0; j < framesize / 2; j += 2) {
            nv12[framesize + j] = nv21[j + framesize - 1];
        }
    }


}
