package com.sf.libplayer.decoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

/**
 * Created by sufan on 2018/4/29.
 */

public class MediaVideoDecoder {

    private static final String TAG = "MediaVideoDecoder";

    private static final String MIME_TYPE = "video/avc";
    private static final int COLOR_FORMAT = MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface; // API >= 18
    private static final int FRAME_RATE = 25;
    private static final float BPP = 0.25f;
    private static final int I_FRAME_INTERVAL = 10;
    protected static final int TIMEOUT_USEC = 10000;    // 1[msec]

    private MediaCodec mMediaCodec;
    private int mWidth, mHeight;
    private boolean mIsFirstFrame=true;
    private Surface mSurface;

    private boolean isRunning = false;  //线程是否正在运行
    private ArrayBlockingQueue<byte[]> encodeQueue = new ArrayBlockingQueue<byte[]>(10);//编码后的数据


    public MediaVideoDecoder(int width, int height,Surface surface) {
        mWidth = width;
        mHeight = height;
        mSurface=surface;
        init();
    }

    //初始化参数
    private void init() {
        try {
            mMediaCodec = MediaCodec.createDecoderByType(MIME_TYPE);
            final MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, COLOR_FORMAT);
            format.setInteger(MediaFormat.KEY_BIT_RATE, calcBitRate());
            format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, I_FRAME_INTERVAL);
            mMediaCodec.configure(format, mSurface, null, 0);
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

    public void putEncodeData(byte[] buffer) {
        if (encodeQueue.size() >= 10) {
            encodeQueue.poll();
        }
        encodeQueue.add(buffer);
    }


    public void startDecoderThread(){
        isRunning = true;
        Thread decodeThread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning){
                    if (encodeQueue.size() > 0) {
                        //从缓冲队列中取出一帧
                        byte[] input = encodeQueue.poll();
                        //编码
                        decodeData(input, 0, input.length, 0);
                    }
                }
            }
        });
        decodeThread.start();

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
    }


    private void decodeData(byte[] data, int offset, int length, int flag) {
        ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();//拿到输入缓冲区,用于传送数据进行编码
        int inputBufferIndex = mMediaCodec.dequeueInputBuffer(TIMEOUT_USEC);
        Log.d(TAG,"deocode inputBufferIndex:"+inputBufferIndex);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(data);
            if (mIsFirstFrame) {
                /**
                 * Some formats, notably AAC audio and MPEG4, H.264 and H.265 video formats
                 * require the actual data to be prefixed by a number of buffers containing
                 * setup data, or codec specific data. When processing such compressed formats,
                 * this data must be submitted to the codec after start() and before any frame data.
                 * Such data must be marked using the flag BUFFER_FLAG_CODEC_CONFIG in a call to queueInputBuffer.
                 */
                mMediaCodec.queueInputBuffer(inputBufferIndex, offset, length, System.nanoTime()/1000, MediaCodec.BUFFER_FLAG_CODEC_CONFIG);
                mIsFirstFrame = false;
            } else {
                mMediaCodec.queueInputBuffer(inputBufferIndex, offset, length, System.nanoTime()/1000, flag);
            }
        }

        ByteBuffer[] outputBuffers = mMediaCodec.getOutputBuffers();
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
        while (outputBufferIndex >= 0) {
            Log.d(TAG, "decode drain frame  " + bufferInfo.size);
            ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
            outputBuffer.position(bufferInfo.offset);
            outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
            byte[] frame = new byte[bufferInfo.size];
            outputBuffer.get(frame, 0, bufferInfo.size);
            mMediaCodec.releaseOutputBuffer(outputBufferIndex, false);
            outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
        }

    }


}
