package com.sf.sofarmusic.demo.media.track;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sf.libplayer.util.MediaUtil;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.UIRootActivity;
import com.sf.sofarmusic.util.FileUtil;
import com.sf.sofarmusic.util.ToastUtil;

import java.io.File;

/**
 * Created by sufan on 2018/4/28.
 */

public class MediaTrackActivity extends UIRootActivity {

    private TextView tv_audio,tv_video;
    private Button btn_combine;

    private String mAudioPath,mVideoPath;
    private String mOutPath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media_track;
    }

    @Override
    protected void initTitle() {
        head_title.setText("音视频的合成");
    }

    @Override
    protected void initView() {
        tv_audio=findViewById(R.id.tv_audio);
        tv_video=findViewById(R.id.tv_video);
        btn_combine=findViewById(R.id.btn_combine);
    }

    @Override
    protected void initData() {
     //   mAudioPath= FileUtil.getRootDir(this)+"/downloads|sf的Music/赵雷-成都.mp3";
        mVideoPath=FileUtil.getAudioDir(this)+"/mediarecorder2.mp4";
        mAudioPath=FileUtil.getAudioDir(this)+"/mediarecorder1.mp4";
        mOutPath=FileUtil.getAudioDir(this)+"track.mp4";


        if(!TextUtils.isEmpty(mAudioPath)) {
            int index=mAudioPath.lastIndexOf('/');
            if(index!=-1) {
                tv_audio.setText("音乐："+mAudioPath.substring(index+1));
            }
        }

        if(!TextUtils.isEmpty(mVideoPath)) {
            int index=mVideoPath.lastIndexOf('/');
            if(index!=-1) {
                tv_video.setText("视频："+mVideoPath.substring(index+1));
            }
        }

    }

    @Override
    protected void initEvent() {
        btn_combine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file1=new File(mAudioPath);
                File file2=new File(mVideoPath);
                if(!file1.exists()|| !file2.exists()){
                    ToastUtil.startShort(baseAt,"音乐或者视频文件不存在");
                }else {
                    MediaUtil.combineVideo(mAudioPath,mVideoPath,mOutPath);
                }
            }
        });
    }
}
