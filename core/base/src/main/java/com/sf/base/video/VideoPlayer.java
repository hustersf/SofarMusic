package com.sf.base.video;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.Surface;
import com.sf.utility.LogUtil;
import java.util.ArrayList;
import java.util.List;

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

  private List<VideoPlayCallback> playCallbacks = new ArrayList<>();
  private Context mContext;

  private AudioManager audioManager;
  private AudioFocusRequest audioFocusRequest;

  public VideoPlayer(Context context) {
    mContext = context.getApplicationContext();
    initMediaPlayer();
  }

  private void initMediaPlayer() {
    mMediaPlayer = new MediaPlayer();
    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型;
    mMediaPlayer.setOnPreparedListener(preparedListener);
    mMediaPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
    mMediaPlayer.setOnCompletionListener(completionListener);
    mMediaPlayer.setOnErrorListener(errorListener);

    audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
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
      requestAudioFocus();
    } catch (Exception e) {
      LogUtil.d(TAG, "e:" + e.getMessage());
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
    releaseAudioFocus();
  }

  /**
   * 视频文件是否准备好
   */
  public boolean isPrepared() {
    return mMediaPlayer != null && mPrepared;
  }

  /**
   * 移动到指定位置播放
   *
   * @param progress 视频的百分比
   */
  public void seekTo(int progress) {
    long totalDuration = getTotalDuration();
    if (totalDuration > 0) {
      int i = (int) (progress * 1.0f / 100 * totalDuration);
      mMediaPlayer.seekTo(i);
    }
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

  /**
   * 添加视频播放回调
   */
  public void addVideoPlayCallback(VideoPlayCallback callback) {
    playCallbacks.add(callback);
  }

  /**
   * 移除视频播放回调
   */
  public void removeVideoPlayCallback(VideoPlayCallback callback) {
    playCallbacks.remove(callback);
  }

  MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
    @Override
    public void onPrepared(MediaPlayer mp) {
      mPrepared = true;
      mMediaPlayer.start();
      for (VideoPlayCallback callback : playCallbacks) {
        callback.onPlayStart(mp);
      }
    }
  };


  MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener =
      new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
          mCurrentBufferPercentage = percent;
          for (VideoPlayCallback callback : playCallbacks) {
            callback.onBufferingUpdate(mp, percent);
          }
        }
      };


  MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
    @Override
    public void onCompletion(MediaPlayer mp) {
      releaseAudioFocus();
      for (VideoPlayCallback callback : playCallbacks) {
        callback.onCompletion(mp);
      }
    }
  };


  MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
      LogUtil.d(TAG, "error_" + what + ":" + extra);
      return false;
    }
  };

  MediaPlayer.OnInfoListener infoListener = new MediaPlayer.OnInfoListener() {
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
      LogUtil.d(TAG, "info_" + what + ":" + extra);
      return false;
    }
  };

  private void requestAudioFocus() {
    if (Build.VERSION.SDK_INT >= 26) {
      audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
          .setOnAudioFocusChangeListener(listener)
          .setAudioAttributes(new AudioAttributes.Builder()
              .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
              .setLegacyStreamType(AudioManager.STREAM_MUSIC)
              .build())
          .build();
      audioManager.requestAudioFocus(audioFocusRequest);
    } else {
      audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC,
          AudioManager.AUDIOFOCUS_GAIN);
    }
  }

  private void releaseAudioFocus() {
    if (audioManager != null) {
      if (Build.VERSION.SDK_INT >= 26) {
        audioManager.abandonAudioFocusRequest(audioFocusRequest);
      } else {
        audioManager.abandonAudioFocus(listener);
      }
    }
  }


  private AudioManager.OnAudioFocusChangeListener listener =
      new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
          LogUtil.d(TAG, "focusChange:"+focusChange);
          switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:// Pause playback
              break;
            case AudioManager.AUDIOFOCUS_GAIN:// Resume playback
              // 重新获得焦点, 可做恢复播放，恢复后台音量的操作
              LogUtil.d(TAG, "重新拿到焦点，恢复播放");
              play(mCurPath);
              break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
              // 短暂丢失焦点，这种情况是被其他应用申请了短暂的焦点希望其他声音能压低音量（或者关闭声音）凸显这个声音（比如短信提示音），
              break;
            case AudioManager.AUDIOFOCUS_LOSS:// Stop playback
              // 永久丢失焦点除非重新主动获取，这种情况是被其他播放器抢去了焦点， 为避免与其他播放器混音，可将音乐暂停
              pause();
              break;
          }
        }
      };
}
