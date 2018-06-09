package com.sf.sofarmusic.demo.media.video;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sf.libplayer.video.SofarCamera;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.BaseActivity;
import com.sf.sofarmusic.util.FileUtil;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.sofarmusic.util.LogUtil;

/**
 * Created by sufan on 2018/4/23.
 */

public class CameraTextureActivity extends BaseActivity implements TextureView.SurfaceTextureListener {

    private TextView tv_camera;
    private Button btn_take_picture;

    private TextureView texture_camera;
    private SurfaceTexture texture;

    private SofarCamera mSofarCamera;
    private String picturePath;

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
        btn_take_picture = findViewById(R.id.btn_take_picture);
        texture_camera = findViewById(R.id.texture_camera);
    }

    public void initData() {
        picturePath = FileUtil.getPictureDir(this) + "/take_picture.jpg";
        tv_camera.setTypeface(FontUtil.setFont(this));

        api = getIntent().getIntExtra("api", 0);


        texture = texture_camera.getSurfaceTexture();

        LogUtil.d((texture == null) + "   texture");
//        mSofarCamera = new SofarCamera.Builder()
//                .cameraApi(SofarCamera.CAMERA1)
//                .cameraId(SofarCamera.CAMERA_FRONT)
//                .texture(texture)
//                .build();
    }

    public void initEvent() {
        texture_camera.setSurfaceTextureListener(this);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSofarCamera.switchCamera();
            }
        });
        btn_take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSofarCamera.takePicture(picturePath);
            }
        });

    }

    private void initCamera(){
        if(api==1) {
            mSofarCamera = new SofarCamera.Builder()
                    .context(this)
                    .cameraApi(SofarCamera.CAMERA1)
                    .cameraId(SofarCamera.CAMERA_FRONT)
                    .texture(texture)
                    .build();
        }else {
            mSofarCamera = new SofarCamera.Builder()
                    .context(this)
                    .cameraApi(SofarCamera.CAMERA2)
                    .cameraId(SofarCamera.CAMERA_FRONT)
                    .texture(texture)
                    .build();
        }
    }


    //TextureView
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        LogUtil.d("onSurfaceTextureAvailable:SurfaceTexture==null:" + (surface == null));
        texture=surface;
        initCamera();
        mSofarCamera.openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtil.d("onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        LogUtil.d("onSurfaceTextureDestroyed");
        mSofarCamera.destroyCamera();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        LogUtil.d("onSurfaceTextureUpdated");
    }
}
