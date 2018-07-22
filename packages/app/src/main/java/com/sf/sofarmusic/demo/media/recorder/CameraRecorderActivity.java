package com.sf.sofarmusic.demo.media.recorder;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sf.base.BaseActivity;
import com.sf.libplayer.video.SofarCamera;
import com.sf.sofarmusic.R;
import com.sf.base.UIRootActivity;
import com.sf.sofarmusic.util.FileUtil;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.utility.LogUtil;

/**
 * Created by sufan on 2018/4/26.
 */

public class CameraRecorderActivity extends BaseActivity implements TextureView.SurfaceTextureListener {

    private TextView tv_camera, tv_des;
    private Button btn_video_record;

    private TextureView texture_camera;
    private SurfaceTexture texture;

    private SofarCamera mSofarCamera;
    private String mPath;

    private int api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_camera_texture);
        initView();
        initData();
        initEvent();
    }

    public void initView() {
        tv_camera = findViewById(R.id.tv_camera);
        tv_des = findViewById(R.id.tv_des);
        btn_video_record = findViewById(R.id.btn_take_picture);
        texture_camera = findViewById(R.id.texture_camera);
    }

    public void initData() {
        tv_des.setText("长按录制");
        tv_camera.setTypeface(FontUtil.setFont(this));

        api = getIntent().getIntExtra("api", 0);


        texture = texture_camera.getSurfaceTexture();
        LogUtil.d((texture == null) + "   texture");
    }

    public void initEvent() {
        texture_camera.setSurfaceTextureListener(this);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSofarCamera.switchCamera();
            }
        });
        btn_video_record.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv_des.setText("正在录制中...");
                    mSofarCamera.startRecord(mPath);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    tv_des.setText("录制完毕，可再次录制");
                    mSofarCamera.stopRecord();
                }
                return false;
            }
        });
    }

    private void initCamera() {
        if (api == 1) {
            mSofarCamera = new SofarCamera.Builder()
                    .context(this)
                    .cameraApi(SofarCamera.CAMERA1)
                    .cameraId(SofarCamera.CAMERA_FRONT)
                    .texture(texture)
                    .build();
            mPath = FileUtil.getAudioDir(this) + "/mediarecorder1.mp4";
        } else {
            mSofarCamera = new SofarCamera.Builder()
                    .context(this)
                    .cameraApi(SofarCamera.CAMERA2)
                    .cameraId(SofarCamera.CAMERA_FRONT)
                    .texture(texture)
                    .build();
            mPath = FileUtil.getAudioDir(this) + "/mediarecorder2.mp4";
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.texture = surface;
        initCamera();
        new CameraThread().start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSofarCamera.destroyCamera();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private class CameraThread extends Thread{
        @Override
        public void run() {
            Looper.prepare();
            mSofarCamera.openCamera();
            Looper.loop();
        }
    }
}
