package com.sf.sofarmusic.play;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sf.libnet.callback.StringCallback;
import com.sf.libnet.control.NetWorkUtil;
import com.sf.sofarmusic.PlayServiceAIDL;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.db.PlayList;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.play.notify.MusicNotify;
import com.sf.sofarmusic.util.LogUtil;
import com.sf.sofarmusic.util.ToastUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sufan on 17/4/11.
 */

public class PlayService extends Service implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {

    private MediaPlayer mediaPlayer;
    private static final String TAG = "PlayService";

    private List<PlayItem> playList;

    private int currentType;    //当前歌曲是本地还是在线的
    private int currentPosition; //当前歌曲在列表中的位置
    private int currentMode;     //当前歌曲的播放模式

    private String preSongId;   //上一首歌曲id
    private String currentSongId;  //当前歌曲id

    //定时通知UI更新
    private Timer timer;
    private int secondProgress;
    private int total;

    //通知栏广播通知
    private BroadcastReceiver receiver;
    private MusicNotify musicNotify;

    //音频焦点管理
    private AudioManager audioManager;

    //锁屏封面
    private MediaSessionCompat mSession;
    private Bitmap albumBt;
    private Handler mHandler;



    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate:service");
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mHandler = new Handler(Looper.getMainLooper());

        setUpNotifyReceiver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpMediaSession();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy:service");
        audioManager.abandonAudioFocus(this);   //一定要释放焦点
        audioManager = null;

        if(mSession!=null) {
            mSession.setActive(false);
            mSession.release();
        }

        unregisterReceiver(receiver);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }


    /**
     * 播放当前歌曲
     */
    private void play() {
        playList = PlayList.getInstance(PlayService.this).getPlayList();
        if (playList.size() == 0) {
            LogUtil.d(TAG, "列表数据为空");
            return;
        }

        currentType = PlayStatus.getInstance(PlayService.this).getType();
        currentPosition = PlayStatus.getInstance(PlayService.this).getPosition();
        currentMode = PlayStatus.getInstance(PlayService.this).getMode();
        currentSongId = playList.get(currentPosition).songId;

        if (musicNotify != null) {
            musicNotify.setPlayStatus();
        }

        LogUtil.d(TAG, "play");
        PlayStatus.getInstance(PlayService.this).setStatus(PlayStatus.PLAY);
        //主动获取焦点
        audioManager.requestAudioFocus(PlayService.this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        LogUtil.d(TAG, "preId:" + preSongId + "  curId" + currentSongId);
        if (currentSongId.equals(preSongId)) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateMediaSession(true);
                }
            });
            return;
        }

        if (currentType == PlayStatus.LOCAL) {
            playUrl();
        } else {
            getSongUrl();
        }
    }


    /**
     * 播放下一首
     */
    private void playNext() {
        if (playList == null) {
            playList = PlayList.getInstance(PlayService.this).getPlayList();
            currentPosition = PlayStatus.getInstance(PlayService.this).getPosition();
        }
        currentMode = PlayStatus.getInstance(PlayService.this).getMode();
        mediaPlayer.setLooping(false);
        if (currentMode == PlayStatus.List_Cycle) {
            if (currentPosition == playList.size() - 1) {
                currentPosition = 0;
            } else {
                currentPosition = currentPosition + 1;
            }
        } else if (currentMode == PlayStatus.Single_Cycle) {
            mediaPlayer.setLooping(true);
        } else if (currentMode == PlayStatus.Random_Cycle) {
            int temp = currentPosition;
            do {
                currentPosition = new Random().nextInt(playList.size());
            } while (currentPosition == temp);
        }

        if (musicNotify != null) {
            musicNotify.setPlayStatus();
        }
        PlayStatus.getInstance(PlayService.this).setStatus(PlayStatus.PLAY);
        sendPositionBroadcast();
        if (currentType == PlayStatus.LOCAL) {
            playUrl();
        } else {
            getSongUrl();
        }
    }

    /**
     * 播放上一首
     */
    private void playPre() {
        if (playList == null) {
            playList = PlayList.getInstance(PlayService.this).getPlayList();
            currentPosition = PlayStatus.getInstance(PlayService.this).getPosition();
        }
        currentMode = PlayStatus.getInstance(PlayService.this).getMode();
        mediaPlayer.setLooping(false);
        if (currentMode == PlayStatus.List_Cycle) {
            if (currentPosition == 0) {
                currentPosition = playList.size() - 1;
            } else {
                currentPosition = currentPosition - 1;
            }
        } else if (currentMode == PlayStatus.Single_Cycle) {
            mediaPlayer.setLooping(true);
        } else if (currentMode == PlayStatus.Random_Cycle) {
            int temp = currentPosition;
            do {
                currentPosition = new Random().nextInt(playList.size());
            } while (currentPosition == temp);
        }

        if (musicNotify != null) {
            musicNotify.setPlayStatus();
        }
        PlayStatus.getInstance(PlayService.this).setStatus(PlayStatus.PLAY);
        sendPositionBroadcast();
        if (currentType == PlayStatus.LOCAL) {
            playUrl();
        } else {
            getSongUrl();
        }
    }


    /**
     * 暂停播放
     */
    private void pause() {
        LogUtil.d(TAG, "pause");
        audioManager.abandonAudioFocus(PlayService.this);   //主动放弃焦点
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                updateMediaSession(true);
            }
        });

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            PlayStatus.getInstance(PlayService.this).setStatus(PlayStatus.PAUSE);
        }
        if (musicNotify != null) {
            musicNotify.setPauseStatus();
        }
    }


    /**
     * 移动到指定位置
     *
     * @param progress 百分比
     */
    private void seekTo(int progress) {
        int i = (int) (progress * 1.0f / 100 * total);
        mediaPlayer.seekTo(i);
    }


    /**
     * 清空播放列表
     */
    private void clear(){
        PlayList.getInstance(this).clearList();
        playList = PlayList.getInstance(PlayService.this).getPlayList();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        if (timer != null) {
            timer.cancel();
        }

        mSession.setActive(false);
        if (musicNotify != null) {
            musicNotify.cancelNotify();
            stopForeground(true);
        }
    }


    /**
     * 释放资源
     */
    private void destroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.release();
        }

        if (timer != null) {
            timer.cancel();
        }

        if (musicNotify != null) {
            musicNotify.cancelNotify();
            stopForeground(true);
        }
    }


    PlayServiceAIDL.Stub iBinder = new PlayServiceAIDL.Stub() {
        @Override
        public void play() throws RemoteException {
            PlayService.this.play();
        }

        @Override
        public void playNext() throws RemoteException {
            PlayService.this.playNext();
        }

        @Override
        public void playPre() throws RemoteException {
            PlayService.this.playPre();
        }

        @Override
        public void pause() throws RemoteException {
            PlayService.this.pause();
        }

        @Override
        public void seekTo(int progress) throws RemoteException {
            PlayService.this.seekTo(progress);
        }

        @Override
        public void clear() throws RemoteException {
            PlayService.this.clear();
        }

        @Override
        public void destroy() throws RemoteException {
            PlayService.this.destroy();
        }
    };


    private void playUrl() {
        String url = playList.get(currentPosition).showUrl;
        preSongId = playList.get(currentPosition).songId;
        LogUtil.d(TAG, "type:" + currentType + " name:" + playList.get(currentPosition).name + " mode:" + currentMode);
        LogUtil.d(TAG, "歌曲播放地址：" + url);
        try {
            if (timer != null) {
                timer.cancel();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getSongUrl() {
        LogUtil.d(TAG, "获取在线歌曲地址中...");
        String songId = playList.get(currentPosition).songId;
        String url = Constant.Ip + "method=baidu.ting.song.play&songid=" + songId;
        NetWorkUtil.getInstance().requestGetAsyn(url, new HashMap<String, String>(), new StringCallback() {
            @Override
            public void OnSuccess(String str) {
                JSONObject jsonObject = JSONObject.parseObject(str);
                JSONObject songInfo = jsonObject.getJSONObject("songinfo");
                JSONObject bitrate = jsonObject.getJSONObject("bitrate");

                PlayItem item = playList.get(currentPosition);
                item.showUrl = bitrate.getString("show_link");
                item.fileUrl = bitrate.getString("file_link");

                LogUtil.d(TAG, "获取在线歌曲地址获取成功");

                playUrl();
            }

            @Override
            public void OnError(Object obj) {
                LogUtil.d(TAG, "获取在线歌曲地址获取失败:" + obj.toString());
                ToastUtil.startShort(PlayService.this,"网络连接不可用，请稍后再试");
                /**
                 * 小米手机--设置--其他高级设置--电源和性能--神隐模式

                 标准(限制后台应用的网络和定位功能)

                 关闭(不限制后台应用的功能)

                 默认是标准,在屏保后4分钟左右会限制后台应用的网络功能
                 */
            }
        });

    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtil.d(TAG, "onBufferingUpdate");
        //已经缓冲的百分比
        secondProgress = percent;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        LogUtil.d(TAG, "onCompletion");
        audioManager.abandonAudioFocus(this);
        playNext();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        LogUtil.d(TAG, "onPrepared");
        mediaPlayer.start();
        startTimer();
        startNotify();
        updateMediaSession(true);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogUtil.d(TAG, "onError-Error code:" + what + "  Error extra" + extra);
        return false;
    }


    //开启音乐通知栏
    private void startNotify() {
        PlayItem item = playList.get(currentPosition);
        musicNotify = new MusicNotify(this, 1, item);
        NotificationCompat.Builder builder = musicNotify.showNotify();
        startForeground(1, builder.build());    //将service放置前台，减少被杀死的几率
    }


    //定时通知UI更新
    private void startTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendBroadcast();
            }
        };
        timer.schedule(task, 0, 1000);  //每一秒通知
    }


    //设置通知栏的广播
    private void setUpNotifyReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.NOTIFY_CLOSE);
        intentFilter.addAction(Constant.NOTIFY_NEXT);
        intentFilter.addAction(Constant.NOTIFY_PLAY);
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);    //监听耳机拔出
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);  //锁屏
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);   //开屏
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.NOTIFY_CLOSE)) {
                    destroy();
                } else if (intent.getAction().equals(Constant.NOTIFY_NEXT)) {
                    playNext();
                } else if (intent.getAction().equals(Constant.NOTIFY_PLAY)) {
                    if (mediaPlayer.isPlaying()) {
                        pause();
                        sendPauseBroadcast();
                    } else {
                        play();
                        sendPlayBroadcast();
                    }
                } else if (intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                    pause();
                    sendPauseBroadcast();
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    LogUtil.d(TAG, "锁屏");
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    LogUtil.d(TAG, "开屏");
                }
            }
        };
        registerReceiver(receiver, intentFilter);
    }


    //发送UI更新广播
    private void sendBroadcast() {
        total = mediaPlayer.getDuration();
        int current = mediaPlayer.getCurrentPosition();
        int progress = (int) ((current * 1.0f) / total * 100);

        Intent intent = new Intent();
        intent.setAction(Constant.RefreshProgress);
        Bundle bundle = new Bundle();
        bundle.putInt("secondProgress", secondProgress);
        bundle.putInt("progress", progress);
        bundle.putInt("current", current);
        bundle.putInt("total", total);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }


    //发送列表正在播放的歌曲的位置
    private void sendPositionBroadcast() {
        PlayStatus.getInstance(PlayService.this).setPosition(currentPosition);

        Intent intent = new Intent();
        intent.setAction(Constant.RefreshPosition);
        Bundle bundle = new Bundle();
        bundle.putInt("position", currentPosition);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }


    //告诉页面音乐处于停止状态
    private void sendPauseBroadcast() {
        PlayStatus.getInstance(this).setStatus(PlayStatus.PAUSE);
        Intent intent = new Intent();
        intent.setAction(Constant.NOTIFY_SERVICE_PAUSE);
        sendBroadcast(intent);

    }


    //告诉页面音乐处于播放状态
    private void sendPlayBroadcast() {
        PlayStatus.getInstance(this).setStatus(PlayStatus.PLAY);
        Intent intent = new Intent();
        intent.setAction(Constant.NOTIFY_SERVICE_PLAY);
        sendBroadcast(intent);
    }


    //处理音频焦点问题
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT://Pause playback
                // 暂时丢失焦点，这种情况是被其他应用申请了短暂的焦点，可压低后台音量
                LogUtil.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                break;
            case AudioManager.AUDIOFOCUS_GAIN://Resume playback
                // 重新获得焦点,  可做恢复播放，恢复后台音量的操作
                LogUtil.d(TAG, "音频重新拿到焦点，恢复播放");
                play();
                sendPlayBroadcast();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // 短暂丢失焦点，这种情况是被其他应用申请了短暂的焦点希望其他声音能压低音量（或者关闭声音）凸显这个声音（比如短信提示音），
                LogUtil.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                break;
            case AudioManager.AUDIOFOCUS_LOSS://Stop playback
                // 永久丢失焦点除非重新主动获取，这种情况是被其他播放器抢去了焦点，  为避免与其他播放器混音，可将音乐暂停
                LogUtil.d(TAG, "音频失去焦点，暂停播放");
                pause();
                sendPauseBroadcast();
                break;
        }
    }


    private void setUpMediaSession() {
        mSession = new MediaSessionCompat(this, "SofarMusic");
        mSession.setCallback(new MediaSessionCompat.Callback() {

            @Override
            public void onPlay() {
                super.onPlay();
                play();
            }

            @Override
            public void onPause() {
                super.onPause();
                pause();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                playPre();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                playNext();
            }
        });
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
    }

    private void updateMediaSession(final boolean isActive) {
        LogUtil.d(TAG, "updateMediaSession");
        final PlayItem item = playList.get(currentPosition);
        if (currentType == PlayStatus.LOCAL) {
            Glide.with(this).load(item.imgUri).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    albumBt = bitmap;
                    setScreenInfo(item, isActive);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
//                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_disk_210);
                    albumBt = null;
                    setScreenInfo(item, isActive);
                }
            });
        } else {
            Glide.with(this).load(item.bigUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                    albumBt = bitmap;
                    setScreenInfo(item, isActive);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
//                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_disk_210);
                    albumBt = null;
                    setScreenInfo(item, isActive);
                }
            });
        }

    }


    private void setScreenInfo(PlayItem item, boolean isActive) {
        int playState = mediaPlayer.isPlaying()
                ? PlaybackStateCompat.STATE_PLAYING
                : PlaybackStateCompat.STATE_PAUSED;
        mSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, item.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, item.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, item.album)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, item.name)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration())
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, currentPosition + 1)
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, playList.size())
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, "test")
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumBt)
                .build());

        mSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setState(playState, mediaPlayer.getCurrentPosition(), 1.0f)
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .build());
        mSession.setActive(isActive);

    }

}
