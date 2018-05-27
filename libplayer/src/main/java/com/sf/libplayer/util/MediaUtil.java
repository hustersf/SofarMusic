package com.sf.libplayer.util;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.test.AndroidTestCase;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by sufan on 2018/4/28.
 *
 * 小米5 6.0.1  WVMExtractor: Failed to open libwvm.so: dlopen failed: library "libwvm.so" not found
 *
 * MPEG4Writer: Unsupported mime 'audio/mpeg'
 * 当音频文件是mp3时会报这个错误  stackoverflow
 * https://stackoverflow.com/questions/20416265/impossible-to-mix-audio-file-and-video-file-using-mediamuxer
 */

public class MediaUtil {


    private static final String TAG = "MediaUtil";
    /**
     * @param audioPath 音频文件路劲
     * @param videoPath 视频文件路径
     * @param outPath   合成之后的保存路径
     */
    public static void combineVideo(String audioPath, String videoPath, String outPath) {
        MediaMuxer muxer = null;
        MediaExtractor audioExtractor = null;
        MediaExtractor videoExtractor = null;
        try {
            muxer = new MediaMuxer(outPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

            //找到音频文件的音频轨
            audioExtractor = new MediaExtractor();
            audioExtractor.setDataSource(audioPath);
            int srcATrackIndex = -1;    //音频源的音频轨
            int audioTrackIndex = -1; //音频轨添加到muxer后返回的新的轨道
            //在此循环，目的是找到我们需要的音频轨
            for (int i = 0; i < audioExtractor.getTrackCount(); i++) {
                MediaFormat format = audioExtractor.getTrackFormat(i);
                if (format.getString(MediaFormat.KEY_MIME).startsWith("audio/")) {
                    srcATrackIndex = i;
                    audioTrackIndex = muxer.addTrack(format);
                    break;
                }
            }

            Log.d(TAG,"音频轨源索引："+srcATrackIndex+" 音频轨新索引:"+audioTrackIndex);

            //找到视频文件的视频轨
            videoExtractor = new MediaExtractor();
            videoExtractor.setDataSource(videoPath);
            int srcVTrackIndex = -1;  //视频源的视频轨
            int videoTrackIndex = -1;  //视频轨添加到muxer后返回的新的轨道
            for (int i = 0; i < videoExtractor.getTrackCount(); i++) {
                MediaFormat format = videoExtractor.getTrackFormat(i);
                if (format.getString(MediaFormat.KEY_MIME).startsWith("video/")) {
                    srcVTrackIndex = i;
                    videoTrackIndex = muxer.addTrack(format);
                    break;
                }
            }

            Log.d(TAG,"视频轨源索引："+srcVTrackIndex+" 视频频轨新索引:"+videoTrackIndex);


            //添加完所有轨道后start
            muxer.start();
            Log.d(TAG,"开始合成视频...");

            //封装音频track
            audioExtractor.selectTrack(srcATrackIndex);   //移动到音频轨上
            if (audioTrackIndex != -1) {
                MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                info.presentationTimeUs = 0;
                ByteBuffer buffer = ByteBuffer.allocate(100 * 1024);
                while (true) {
                    int sampleSize = audioExtractor.readSampleData(buffer, 0);
                    if (sampleSize < 0) {
                        //没有可获取的样本，退出循环
                        break;
                    }

                    info.offset = 0;
                    info.size = sampleSize;
                    info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                    info.presentationTimeUs = audioExtractor.getSampleTime();

                    muxer.writeSampleData(audioTrackIndex, buffer, info);//将样本写入新的轨道
                    audioExtractor.advance(); //进入下一个样本
                }
            }
            Log.d(TAG,"音频轨样本采集完成");

            //封装视频track
            videoExtractor.selectTrack(srcVTrackIndex);   //移动到视频轨上
            if (videoTrackIndex != -1) {
                MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
                info.presentationTimeUs = 0;
                ByteBuffer buffer = ByteBuffer.allocate(100 * 1024);
                while (true) {
                    int sampleSize = videoExtractor.readSampleData(buffer, 0);
                    if (sampleSize < 0) {
                        //没有可获取的样本，退出循环
                        break;
                    }

                    info.offset = 0;
                    info.size = sampleSize;
                    info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
                    info.presentationTimeUs = videoExtractor.getSampleTime();

                    muxer.writeSampleData(videoTrackIndex, buffer, info);//将样本写入新的轨道
                    videoExtractor.advance(); //进入下一个样本
                }
            }
            muxer.stop();
            Log.d(TAG,"视频轨样本采集完成");
            Log.d(TAG,"视频合成完毕："+outPath);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"合成出错:"+e.getMessage());
        } finally {
            //释放资源
            if(audioExtractor!=null){
                audioExtractor.release();
            }
            if(videoExtractor!=null){
                videoExtractor.release();
            }
            if(muxer!=null){
                muxer.release();
            }
        }
    }
}
