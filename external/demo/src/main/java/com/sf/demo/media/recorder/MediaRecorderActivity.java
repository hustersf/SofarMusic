package com.sf.demo.media.recorder;

import java.io.IOException;

import android.Manifest;
import android.content.Intent;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.sf.base.UIRootActivity;
import com.sf.base.permission.PermissionUtil;
import com.sf.base.util.FileUtil;
import com.sf.demo.R;
import com.sf.utility.ToastUtil;

/**
 * Created by sufan on 2018/4/26.
 */

public class MediaRecorderActivity extends UIRootActivity {

  private Button btn_audio_record, btn_video_record1, btn_video_record2;
  private MediaRecorder mRecorder;
  private String path;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_media_recorder;
  }

  @Override
  protected void initTitle() {
    head_title.setText("MediaRecorder音视频录制");
  }

  @Override
  protected void initView() {
    btn_audio_record = findViewById(R.id.btn_audio_record);
    btn_video_record1 = findViewById(R.id.btn_video_record1);
    btn_video_record2 = findViewById(R.id.btn_video_record2);
  }

  @Override
  protected void initData() {
    path = FileUtil.getAudioDir(this) + "/mediarecorder.3gp";

    String des = "录音或摄像权限被禁止，我们需要打开权限";
    String[] permissions =
        new String[] {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    PermissionUtil.requestPermissions(this, permissions, des, "").subscribe(aBoolean -> {
      if (!aBoolean) {
        finish();
      }
    });
  }

  @Override
  protected void initEvent() {
    btn_audio_record.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          Log.d("TAG", "按住");
          start();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
          Log.d("TAG", "松开");
          stop();
        }
        return false;
      }
    });

    btn_video_record1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(baseAt, CameraRecorderActivity.class);
        intent.putExtra("api", 1);
        startActivity(intent);
      }
    });

    btn_video_record2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(baseAt, CameraRecorderActivity.class);
        intent.putExtra("api", 2);
        startActivity(intent);

        // Intent intent=new Intent(baseAt, CameraActivity.class);
        // intent.putExtra("api",2);
        // startActivity(intent);
      }
    });
  }

  private void start() {
    btn_audio_record.setText("松开 结束");
    try {
      mRecorder = new MediaRecorder();
      mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mRecorder.setAudioSamplingRate(44100);
      mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
      mRecorder.setOutputFile(path);
      mRecorder.prepare();
      mRecorder.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void stop() {
    btn_audio_record.setText("按住 录音");
    ToastUtil.startShort(this, "录音文件已保存至：" + path);
    mRecorder.stop();
    mRecorder.release();
    mRecorder = null;
  }
}
