package com.sf.libplayer.decoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;

/**
 * Created by sufan on 2018/4/29.
 */

public class MediaAudioDecoder {

    private static final String TAG = "MediaAudioDecoder";

    private static final String MIME_TYPE = "audio/mp4a-latm";
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_NUM = 1;
//    private static final int BITRATE = 128 * 1000; //AAC-LC, 64 *1024 for AAC-HE
    private static final int PROFILE_LEVEL = MediaCodecInfo.CodecProfileLevel.AACObjectLC;
    private static final int MAX_BUFFER_SIZE = 16384;

    private MediaCodec mMediaCodec;


    public MediaAudioDecoder(){

    }

    //初始化参数
    private void init(){
        try {
            mMediaCodec = MediaCodec.createDecoderByType(MIME_TYPE);
            MediaFormat format = new MediaFormat();
            format.setString(MediaFormat.KEY_MIME, MIME_TYPE);
            format.setInteger(MediaFormat.KEY_SAMPLE_RATE, SAMPLE_RATE);
            format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, CHANNEL_NUM);
            format.setInteger(MediaFormat.KEY_AAC_PROFILE, PROFILE_LEVEL);
            format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, MAX_BUFFER_SIZE);
            mMediaCodec.configure(format, null, null, 0);
            mMediaCodec.start();
            Log.d(TAG,"MediaCodec init success");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"MediaCodec init failed:"+e.getMessage());
        }
    }


    public void decodeData(byte[] data){

    }

}
