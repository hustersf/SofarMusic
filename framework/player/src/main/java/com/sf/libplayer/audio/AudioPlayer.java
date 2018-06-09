package com.sf.libplayer.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.util.Log;



/**
 * Created by sufan on 2018/4/17.
 */

public class AudioPlayer {

    private static final String TAG = "AudioPlayer";

    private final int DEFAULT_STREAM_TYPE = AudioManager.STREAM_MUSIC;  //流音乐
    private final int DEFAULT_RATE = 44100;    //采样率
    private final int DEFAULT_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;   //双通道(左右声道)
    private final int DEFAULT_FORMAT = AudioFormat.ENCODING_PCM_16BIT;   //数据位宽16位
    private static final int DEFAULT_PLAY_MODE = AudioTrack.MODE_STREAM;


    private AudioTrack mAudioTrack;
    private int mMinBufferSize;


    private boolean isPlaying=false;



    public void startPlay(){
        startPlay(DEFAULT_STREAM_TYPE,DEFAULT_RATE,DEFAULT_CHANNEL,DEFAULT_FORMAT);

    }

    public void startPlay(int streamType, int sampleRateInHz, int channelConfig, int audioFormat){
        if(isPlaying){
            Log.d(TAG,"AudioPlayer has played");
            return;
        }

        mMinBufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        if (mMinBufferSize == AudioRecord.ERROR_BAD_VALUE) {
            Log.d(TAG, "Invalid parameter");
            return;
        }


        mAudioTrack=new AudioTrack(streamType,sampleRateInHz,channelConfig,audioFormat,
                mMinBufferSize,DEFAULT_PLAY_MODE);
        if(mAudioTrack.getState()==AudioTrack.STATE_UNINITIALIZED){
            Log.d(TAG, "AudioTrack initialize fail");
            return;
        }

        isPlaying=true;
    }

    public void stopPlay(){
        if(!isPlaying){
            Log.d(TAG, "AudioTrack is not playing");
            return;
        }

        if(mAudioTrack.getPlayState()==AudioTrack.PLAYSTATE_PLAYING){
            mAudioTrack.stop();
        }

        mAudioTrack.release();
        isPlaying=false;
    }


    public void play(byte[] audioData,int offsetInBytes, int sizeInBytes){
        if(!isPlaying){
            Log.d(TAG, "AudioTrack not start");
            return;
        }

        if(sizeInBytes<mMinBufferSize){
            Log.d(TAG, "audio data not enough");
          //  return;
        }

        if(mAudioTrack.write(audioData,offsetInBytes,sizeInBytes)!=mMinBufferSize){
            Log.d(TAG, "AudioTrack can not write all the data");
        }

        mAudioTrack.play();
        Log.d(TAG, "played  "+sizeInBytes+"  bytes");
    }
}
