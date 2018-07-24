package com.sf.demo.window.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.sf.demo.R;

/**
 * Created by sufan on 17/7/30.
 */

public class NotifyUtil {

    private Context mContext;
    private int id;
    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private RemoteViews remoteViews;


    private int count = 0;

    public NotifyUtil(Context context, int id) {
        mContext = context;
        this.id = id;

        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(mContext);

        /**
         * 自定义布局
         * 不支持自定义view
         */
        remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.title_tv, "我是标题");
        remoteViews.setTextViewText(R.id.time_tv, "我是时间");
        remoteViews.setProgressBar(R.id.music_pb, 100, 0, false);


        //跳转
//        Intent intent = new Intent(mContext, MainActivity.class);
 //       PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setSmallIcon(R.drawable.music);//设置小图标，不设置会报错

        //设置通知栏属性
        builder.setTicker("通知来啦")
                .setAutoCancel(true)
             //   .setDefaults(NotificationCompat.DEFAULT_ALL)   ///打开呼吸灯，声音，震动，触发系统默认行为
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContent(remoteViews);
      //          .setContentIntent(pendingIntent);


    }


    //展示通知栏
    public void showNotify() {
        manager.notify(id, builder.build());
        startTask();
    }


    //刷新通知栏
    private void startTask() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        count+=10;
                        remoteViews.setProgressBar(R.id.music_pb, 100, count, false);
                        manager.notify(id, builder.build());
                        if (count < 100)
                            sendEmptyMessageDelayed(0, 1000);
                        break;
                }

            }
        };
        handler.sendEmptyMessageDelayed(0, 1000);
    }

}
