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
import com.sf.libplayer.pcm.PcmFileReader;
import com.sf.libplayer.pcm.PcmFileWriter;
import com.sf.demo.R;
import com.sf.base.util.FileUtil;

/**
 * Created by sufan on 2018/4/16.
 * 原始音频数据的录入和播放
 */

public class AudioPCMActivity extends UIRootActivity {

    private Button btn_audio_record;
    private Button btn_audio_record_play;


    private AudioCapture audioCapture;
    private AudioPlayer audioPlayer;

    private PcmFileWriter pcmFileWriter;
    private PcmFileReader pcmFileReader;
    private boolean isReading;

    private String path="";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_media_audio;
    }

    @Override
    protected void initTitle() {
        head_title.setText("原始音频的录制和播放");
    }

    @Override
    public void initView() {
        btn_audio_record=findViewById(R.id.btn_audio_record);
        btn_audio_record_play=findViewById(R.id.btn_audio_record_play);
    }

    @Override
    public void initData() {
        path=FileUtil.getAudioDir(this)+"/audioTest.pcm";
        audioCapture=new AudioCapture();
        audioPlayer=new AudioPlayer();
        pcmFileReader=new PcmFileReader();
        pcmFileWriter=new PcmFileWriter();

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
        pcmFileReader.openFile(path);
        audioPlayer.startPlay();
        new AudioTrackThread().start();
    }

    private class AudioTrackThread extends Thread{
        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            while (isReading && pcmFileReader.read(buffer,0,buffer.length)>0){
                audioPlayer.play(buffer,0,buffer.length);
            }
            audioPlayer.stopPlay();
            pcmFileReader.closeFile();
        }
    }


    //开始录音
    private void start(){
        pcmFileWriter.openFile(path);
        btn_audio_record.setText("松开 结束");
        audioCapture.startRecord();
        audioCapture.setOnAudioFrameCaptureListener(new AudioCapture.onAudioFrameCaptureListener() {
            @Override
            public void onAudioFrameCapture(byte[] audioData) {
                pcmFileWriter.write(audioData,0,audioData.length);

            }
        });
    }

    //结束录音
    private void stop(){
        btn_audio_record.setText("按住 录音");
        audioCapture.stopRecord();
        pcmFileWriter.closeFile();
    }
}
