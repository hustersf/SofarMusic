package com.sf.libplayer.encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;

/**
 * Created by sufan on 2018/4/29.
 */

public class MediaAudioEncoder {

    private static final String TAG = "MediaAudioEncoder";

    private static final String MIME_TYPE = "audio/mp4a-latm";
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_NUM = 1;
    private static final int BITRATE = 128 * 1000; //AAC-LC, 64 *1024 for AAC-HE
    private static final int PROFILE_LEVEL = MediaCodecInfo.CodecProfileLevel.AACObjectLC;
    private static final int MAX_BUFFER_SIZE = 16384;

    private MediaCodec mMediaCodec;


    public MediaAudioEncoder(){

    }

    //初始化参数
    private void init(){
        try {
            mMediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
            MediaFormat audioFormat = new MediaFormat();
            audioFormat.setString(MediaFormat.KEY_MIME, MIME_TYPE);
            audioFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, SAMPLE_RATE);
            audioFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, CHANNEL_NUM);
            audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, BITRATE);
            audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, PROFILE_LEVEL);
            audioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, MAX_BUFFER_SIZE);
            mMediaCodec.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.start();
            Log.d(TAG,"MediaCodec init success");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"MediaCodec init failed:"+e.getMessage());
        }
    }


    public void encodeData(byte[] data){

    }

}
