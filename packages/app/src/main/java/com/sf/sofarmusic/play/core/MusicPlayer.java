package com.sf.sofarmusic.play.core;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import com.sf.sofarmusic.base.SofarApp;
import com.sf.utility.LogUtil;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer
    implements
      MediaPlayer.OnBufferingUpdateListener,
      MediaPlayer.OnCompletionListener,
      MediaPlayer.OnPreparedListener,
      MediaPlayer.OnErrorListener {

  private static final String TAG = "MusicPlayer";

  private MediaPlayer mediaPlayer;
  private int totalDuration; // 总时长
  private int secondProgress; // 缓冲的百分比

  private String curPath;
  private boolean prepared;

  private List<MusicPlayCallback> playCallbacks = new ArrayList<>();

  private Context context;
  private AudioManager audioManager;
  private AudioFocusRequest audioFocusRequest;

  static class InstanceHolder {
    static final MusicPlayer instance = new MusicPlayer();
  }

  public static MusicPlayer getInstance() {
    return InstanceHolder.instance;
  }

  private MusicPlayer() {
    this.context = SofarApp.getAppContext();
    init();
  }

  private void init() {
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
    mediaPlayer.setOnBufferingUpdateListener(this);
    mediaPlayer.setOnCompletionListener(this);
    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.setOnErrorListener(this);

    audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
  }

  /**
   * 播放音乐
   * 
   * @param path file path or http path
   */
  public void play(String path) {
    try {
      if (!path.equals(curPath)) {
        prepared = false;
        curPath = path;
      }

      if (prepared) {
        if (!mediaPlayer.isPlaying()) {
          mediaPlayer.start();
        }
      } else {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(path);
        mediaPlayer.prepareAsync();
      }
      requestAudioFocus();
    } catch (Exception e) {

    }
  }

  /**
   * 是否正在播放
   */
  public boolean isPlaying() {
    return mediaPlayer.isPlaying();
  }

  public boolean isPrepared() {
    return prepared;
  }

  /**
   * 暂停播放
   */
  public void pause() {
    mediaPlayer.pause();
  }

  /**
   * 停止播放
   */
  public void stop() {
    mediaPlayer.stop();
    mediaPlayer.release();
    mediaPlayer = null;
    releaseAudioFocus();
  }

  /**
   * 移动到指定位置播放
   * 
   * @param progress 音频的百分比
   */
  public void seekTo(int progress) {
    int i = (int) (progress * 1.0f / 100 * totalDuration);
    mediaPlayer.seekTo(i);
  }

  /**
   *
   * @return 当前的播放位置
   */
  public int getCurrentPosition() {
    return mediaPlayer.getCurrentPosition();
  }

  /**
   *
   * @return 播放总时长
   */
  public int getTotalDuration() {
    return totalDuration;
  }

  /**
   *
   * @return 已缓冲好的百分比
   */
  public int getSecondProgress() {
    return secondProgress;
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {
    LogUtil.d(TAG, "onBufferingUpdate:" + percent);
    secondProgress = percent;

    for (MusicPlayCallback callback : playCallbacks) {
      callback.onBufferingUpdate(mp, percent);
    }
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    LogUtil.d(TAG, "onCompletion");
    releaseAudioFocus();
    for (MusicPlayCallback callback : playCallbacks) {
      callback.onCompletion(mp);
    }
  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    LogUtil.d(TAG, "onError:" + what + "-" + extra);
    return true;
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    LogUtil.d(TAG, "onPrepared");
    prepared = true;
    totalDuration = mp.getDuration();
    mediaPlayer.start();
    for (MusicPlayCallback callback : playCallbacks) {
      callback.onPlayStart(mp);
    }
  }

  /**
   * 添加音乐播放回调
   */
  public void addMusicPlayCallback(MusicPlayCallback callback) {
    playCallbacks.add(callback);
  }

  /**
   * 移除音乐播放回调
   */
  public void removeMusicPlayCallback(MusicPlayCallback callback) {
    playCallbacks.remove(callback);
  }

  private void requestAudioFocus() {
    if (Build.VERSION.SDK_INT >= 26) {
      audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
          .setOnAudioFocusChangeListener(listener)
          .setAudioAttributes(new AudioAttributes.Builder()
              .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
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
          LogUtil.d(TAG, "focusChange:" + focusChange);
          switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:// Pause playback
              break;
            case AudioManager.AUDIOFOCUS_GAIN:// Resume playback
              // 重新获得焦点, 可做恢复播放，恢复后台音量的操作
              LogUtil.d(TAG, "音频重新拿到焦点，恢复播放");
              if (PlayControlHolder.getInstance().isPlaying()) {
                play(curPath);
              }
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
