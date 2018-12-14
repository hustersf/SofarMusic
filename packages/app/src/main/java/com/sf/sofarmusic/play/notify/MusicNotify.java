package com.sf.sofarmusic.play.notify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.db.PlayStatus;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.main.MainActivity;

/**
 * Created by sufan on 17/9/29.
 */

public class MusicNotify {

  private Context mContext;
  private int id;
  private NotificationManager manager;
  private NotificationCompat.Builder builder;
  private RemoteViews remoteViews;

  private PlayItem item;


  // 相关数据
  private int mType;

  public MusicNotify(Context context, int id, PlayItem item) {
    mContext = context;
    this.id = id;
    this.item = item;
    mType = PlayStatus.getInstance(mContext).getType();

    String channelId = "music";
    createNotificationChannel(channelId, "音乐播放", NotificationManager.IMPORTANCE_HIGH);
    manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    builder = new NotificationCompat.Builder(mContext, channelId);

    /**
     * 自定义布局
     * 不支持自定义view
     */
    remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_music_notification);

    remoteViews.setTextViewText(R.id.music_name_tv, item.name);
    remoteViews.setTextViewText(R.id.music_artist_tv, item.artist);


    // 点击XX
    Intent close = new Intent(Constant.NOTIFY_CLOSE);
    PendingIntent pendingIntentClose =
        PendingIntent.getBroadcast(mContext, 0, close, PendingIntent.FLAG_CANCEL_CURRENT);
    remoteViews.setOnClickPendingIntent(R.id.music_close_iv, pendingIntentClose);


    Intent next = new Intent(Constant.NOTIFY_NEXT);
    PendingIntent pendingIntentNext =
        PendingIntent.getBroadcast(mContext, 0, next, PendingIntent.FLAG_CANCEL_CURRENT);
    remoteViews.setOnClickPendingIntent(R.id.music_next_iv, pendingIntentNext);

    Intent play = new Intent(Constant.NOTIFY_PLAY);
    PendingIntent pendingIntentPlay =
        PendingIntent.getBroadcast(mContext, 0, play, PendingIntent.FLAG_CANCEL_CURRENT);
    remoteViews.setOnClickPendingIntent(R.id.music_play_iv, pendingIntentPlay);


    // 跳转
    Intent intent = new Intent(mContext, MainActivity.class);
    PendingIntent pendingIntent =
        PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

    builder.setSmallIcon(R.mipmap.ic_launcher);// 设置小图标，不设置会报错

    // 设置通知栏属性
    builder.setAutoCancel(true)
        // .setDefaults(NotificationCompat.DEFAULT_ALL) ///打开呼吸灯，声音，震动，触发系统默认行为
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setContent(remoteViews)
        .setContentIntent(pendingIntent);
  }

  private void createNotificationChannel(String channelId, String channelName, int importance) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
      NotificationManager notificationManager =
          (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.createNotificationChannel(channel);
    }
  }

  // 展示通知栏
  public NotificationCompat.Builder showNotify() {
    if (mType == PlayStatus.LOCAL) {
      Glide.with(mContext).load(item.imgUri).asBitmap().into(new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
          remoteViews.setImageViewBitmap(R.id.music_iv, bitmap);
          manager.notify(id, builder.build());
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
          super.onLoadFailed(e, errorDrawable);
          Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
              R.drawable.placeholder_disk_210);
          remoteViews.setImageViewBitmap(R.id.music_iv, bitmap);
          manager.notify(id, builder.build());
        }
      });
    } else {
      Glide.with(mContext).load(item.smallUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
          remoteViews.setImageViewBitmap(R.id.music_iv, bitmap);
          manager.notify(id, builder.build());
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
          super.onLoadFailed(e, errorDrawable);
          Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
              R.drawable.placeholder_disk_210);
          remoteViews.setImageViewBitmap(R.id.music_iv, bitmap);
          manager.notify(id, builder.build());
        }
      });
    }
    return builder;
  }

  public void cancelNotify() {
    manager.cancel(id);
  }

  public void setPlayStatus() {
    remoteViews.setImageViewResource(R.id.music_play_iv, R.drawable.notify_pause);
    manager.notify(id, builder.build());

  }

  public void setPauseStatus() {
    remoteViews.setImageViewResource(R.id.music_play_iv, R.drawable.notify_play);
    manager.notify(id, builder.build());
  }

}
