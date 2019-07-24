package com.sf.base.video;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;
import com.sf.utility.LogUtil;

/**
 * 封装视频播放器的操作
 */
public class VideoPlayer {

  private static final String TAG = "VideoPlayer";

  private MediaPlayer mMediaPlayer;
  private Surface mSurface;

  private boolean mPrepared;
  private String mCurPath;
  private int mCurrentBufferPercentage;

  private MediaPlayer.OnPreparedListener mOnPreparedListener;
  private MediaPlayer.OnCompletionListener mOnCompletionListener;
  private MediaPlayer.OnErrorListener mOnErrorListener;
  private MediaPlayer.OnInfoListener mOnInfoListener;

  public VideoPlayer() {
    initMediaPlayer();
  }

  private void initMediaPlayer() {
    mMediaPlayer = new MediaPlayer();
    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型;
    mMediaPlayer.setOnPreparedListener(preparedListener);
    mMediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
    mMediaPlayer.setOnCompletionListener(completionListener);
    mMediaPlayer.setOnErrorListener(errorListener);
  }

  /**
   * SurfaceView 可通过SurfaceHolder.getSurface
   * TextureView 可通过new Surface(SurfaceTexture)
   * 
   * @param surface 承载文件流数据
   */
  public void setSurface(Surface surface) {
    mSurface = surface;
    if (mSurface != null) {
      mMediaPlayer.setSurface(mSurface);
    }
  }

  public boolean surfaceAvailable() {
    return mSurface != null;
  }

  /**
   * 播放视频
   *
   * @param path file path or http path
   */
  public void play(String path) {
    try {
      if (!path.equals(mCurPath)) {
        mPrepared = false;
        mCurPath = path;
      }

      if (mPrepared) {
        mMediaPlayer.start();
      } else {
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(path);
        mMediaPlayer.prepareAsync();
      }
    } catch (Exception e) {

    }
  }

  /**
   * 暂停视频
   */
  public void pause() {
    mMediaPlayer.pause();
  }


  /**
   * 停止播放视频
   */
  public void stop() {
    mMediaPlayer.reset();
    mMediaPlayer.release();
    mMediaPlayer = null;
  }

  /**
   * 视频文件是否准备好
   */
  public boolean isPrepared() {
    return mMediaPlayer != null && mPrepared;
  }

  /**
   *
   * @return 当前的播放位置
   */
  public int getCurrentPosition() {
    if (isPrepared()) {
      return mMediaPlayer.getCurrentPosition();
    }
    return 0;
  }

  /**
   *
   * @return 播放总时长
   */
  public int getTotalDuration() {
    if (isPrepared()) {
      return mMediaPlayer.getDuration();
    }
    return -1;
  }

  /**
   *
   * @return 已缓冲的百分比
   */
  public int getBufferPercentage() {
    return mCurrentBufferPercentage;
  }


  public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
    mOnPreparedListener = l;
  }

  public void setOnErrorListener(MediaPlayer.OnErrorListener l) {
    mOnErrorListener = l;
  }

  public void setOnCompletionListener(MediaPlayer.OnCompletionListener l) {
    mOnCompletionListener = l;
  }

  public void setOnInfoListener(MediaPlayer.OnInfoListener l) {
    mOnInfoListener = l;
  }

  MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
    @Override
    public void onPrepared(MediaPlayer mp) {
      mPrepared = true;
      mMediaPlayer.start();
      if (mOnPreparedListener != null) {
        mOnPreparedListener.onPrepared(mp);
      }
    }
  };


  MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener =
      new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
          mCurrentBufferPercentage = percent;
        }
      };


  MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
    @Override
    public void onCompletion(MediaPlayer mp) {
      if (mOnCompletionListener != null) {
        mOnCompletionListener.onCompletion(mp);
      }
    }
  };


  MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
      LogUtil.d(TAG, "error_" + what + ":" + extra);
      if (mOnErrorListener != null) {
        mOnErrorListener.onError(mp, what, extra);
      }
      return false;
    }
  };

  MediaPlayer.OnInfoListener infoListener = new MediaPlayer.OnInfoListener() {
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
      LogUtil.d(TAG, "info_" + what + ":" + extra);
      if (mOnInfoListener != null) {
        mOnInfoListener.onInfo(mp, what, extra);
      }
      return false;
    }
  };
}
