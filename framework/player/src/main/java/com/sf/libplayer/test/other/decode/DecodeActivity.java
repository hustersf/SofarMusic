package com.sf.libplayer.test.other.decode;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;

/**
 * MediaCodec SurfaceHolder Example
 * 在SurfaceView上播放mp4文件
 *
 * @author taehwan
 */
public class DecodeActivity extends Activity implements SurfaceHolder.Callback {
    private VideoDecoderThread mVideoDecoder;

    private static String FILE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurfaceView surfaceView = new SurfaceView(this);
        surfaceView.getHolder().addCallback(this);
        setContentView(surfaceView);

   //     FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test1.h264";
            FILE_PATH=getExternalCacheDir().getAbsolutePath()+"/audio//mediarecorder1.mp4";

        mVideoDecoder = new VideoDecoderThread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mVideoDecoder != null) {
            if (mVideoDecoder.init(holder.getSurface(), FILE_PATH)) {
                mVideoDecoder.start();

            } else {
                mVideoDecoder = null;
            }

        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mVideoDecoder != null) {
            mVideoDecoder.close();
        }
    }

}
