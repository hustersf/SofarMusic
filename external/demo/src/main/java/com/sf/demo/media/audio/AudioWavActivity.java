package com.sf.demo.media.audio;

import android.Manifest;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.sf.base.UIRootActivity;
import com.sf.base.callback.PermissionsResultListener;
import com.sf.libplayer.audio.AudioCapture;
import com.sf.libplayer.audio.AudioPlayer;
import com.sf.libplayer.wav.WavFileReader;
import com.sf.libplayer.wav.WavFileWriter;
import com.sf.demo.R;
import com.sf.base.util.FileUtil;

/**
 * Created by sufan on 2018/4/16.
 * wav格式的音频文件的存储和解析
 */

public class AudioWavActivity extends UIRootActivity {

    private Button btn_audio_record;
    private Button btn_audio_record_play;


    private AudioCapture audioCapture;
    private AudioPlayer audioPlayer;

    private WavFileWriter wavFileWriter;
    private WavFileReader wavFileReader;
    private boolean isReading;

    private String path="";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_media_audio;
    }

    @Override
    protected void initTitle() {
        head_title.setText("wav音频文件的存储和解析");
    }

    @Override
    public void initView() {
        btn_audio_record=findViewById(R.id.btn_audio_record);
        btn_audio_record_play=findViewById(R.id.btn_audio_record_play);
    }

    @Override
    public void initData() {
        path=FileUtil.getAudioDir(this)+"/audioTest.wav";
        audioCapture=new AudioCapture();
        audioPlayer=new AudioPlayer();
        wavFileReader=new WavFileReader();
        wavFileWriter=new WavFileWriter();

        String des = "录音权限被禁止，我们需要打开录音权限";
        String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
        baseAt.requestPermissions(des, permissions, 100, new PermissionsResultListener() {
            @Override
            public void onPermissionGranted() {

            }
            @Override
            public void onPermissionDenied() {
                finish();

            }
        });

    }

    @Override
    public void initEvent() {
        btn_audio_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    Log.d("TAG","按住");
                    start();
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    Log.d("TAG","松开");
                    stop();
                }
                return false;
            }
        });

        btn_audio_record_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();

            }
        });

    }

    //播放录音
    private void play(){
        isReading=true;
        wavFileReader.openFile(path);
        audioPlayer.startPlay();
        new AudioTrackThread().start();
    }

    private class AudioTrackThread extends Thread{
        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            while (isReading && wavFileReader.readData(buffer,0,buffer.length)>0){
                audioPlayer.play(buffer,0,buffer.length);
            }
            audioPlayer.stopPlay();
            wavFileReader.closeFile();
        }
    }


    //开始录音
    private void start(){
        wavFileWriter.openFile(path,44100,2,16);
        btn_audio_record.setText("松开 结束");
        audioCapture.startRecord();
        audioCapture.setOnAudioFrameCaptureListener(new AudioCapture.onAudioFrameCaptureListener() {
            @Override
            public void onAudioFrameCapture(byte[] audioData) {
                wavFileWriter.writeData(audioData,0,audioData.length);
            }
        });
    }

    //结束录音
    private void stop(){
        btn_audio_record.setText("按住 录音");
        audioCapture.stopRecord();
        wavFileWriter.closeFile();
    }
}
