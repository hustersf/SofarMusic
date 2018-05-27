package com.sf.sofarmusic.demo.media.video;

import android.graphics.SurfaceTexture;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.sf.libplayer.callback.OnCameraFrameListener;
import com.sf.libplayer.callback.OnVideoEncodedListener;
import com.sf.libplayer.decoder.MediaVideoDecoder;
import com.sf.libplayer.encoder.MediaVideoEncoder;
import com.sf.libplayer.video.SofarCamera;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.UIRootActivity;
import com.sf.sofarmusic.util.FileUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by sufan on 2018/4/29.
 * 视频的编解码
 */

public class MediaCodecActivity extends UIRootActivity  implements SurfaceHolder.Callback,TextureView.SurfaceTextureListener{
    private SurfaceView surface_record;
    private TextureView texture_play;
    private SurfaceHolder mHolder;
    private TextureView mTexture;

    private Button button;
    private boolean isRecording;

    private SofarCamera mSofarCamera;

    private MediaVideoEncoder mEncoder;
    private MediaVideoDecoder mDecoder;


    private String filePath = Environment.getExternalStorageDirectory() + "//test1.h264";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media_codec;
    }

    @Override
    protected void initTitle() {
        head_title.setText("视频的编解码实例");
    }

    @Override
    protected void initView() {
        surface_record=findViewById(R.id.surface_record);
        texture_play=findViewById(R.id.texture_play);
        button=findViewById(R.id.button);
    }

    @Override
    protected void initData() {
        mHolder = surface_record.getHolder();
        mHolder.addCallback(this);
        texture_play.setSurfaceTextureListener(this);

        mEncoder=new MediaVideoEncoder(1080,600);
        mEncoder.setH264Path(FileUtil.getAudioDir(this)+"/videotest.h264");
    }

    private void readFile(){
        try {
            FileInputStream fis=new FileInputStream(filePath);
            byte[] bytes = new byte[512];
            int rs = -1;
            while ((rs = fis.read(bytes)) > 0) {
                mDecoder.putEncodeData(bytes);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording=!isRecording;
                if(isRecording) {
                    button.setText("停止录制");
                    mEncoder.startEncoderThread();
                    mDecoder.startDecoderThread();
                    mSofarCamera.setOnFrameListener(new OnCameraFrameListener() {
                        @Override
                        public void onFrame(byte[] data) {
                            mEncoder.putYUVData(data);
                        }
                    });
                }else {
                    mDecoder.stopThread();
                    mEncoder.stopThread();
                    button.setText("开始录制");
                    mSofarCamera.setOnFrameListener(null);
                }
            }
        });

        mEncoder.setOnVideoEncodedListener(new OnVideoEncodedListener() {
            @Override
            public void onFrameEncoded(byte[] encoded) {
//                Log.d("TAG","encoded");
                mDecoder.putEncodeData(encoded);
            }
        });

    }



    private void initCamera(){
        mSofarCamera=new SofarCamera.Builder()
                .context(this)
                .cameraId(SofarCamera.CAMERA_BACK)
                .cameraApi(SofarCamera.CAMERA1)
                .holder(mHolder)
                .build();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCamera();
        mSofarCamera.openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("TAG","surface:width*height="+width+"*"+height);


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSofarCamera.destroyCamera();
    }




    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d("TAG","texture:width*height="+width+"*"+height);
        mDecoder=new MediaVideoDecoder(1080,600,new Surface(surface));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
