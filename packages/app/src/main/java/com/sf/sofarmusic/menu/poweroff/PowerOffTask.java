package com.sf.sofarmusic.menu.poweroff;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sufan on 17/10/5.
 */

public class PowerOffTask {

    private Timer timer;
    private int minute;
    private Handler mHandler;


    public PowerOffTask(int minute) {
        this.minute = minute;
        mHandler=new Handler(Looper.getMainLooper());
    }


    public void openTask(final TimeCallback callback) {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onTime(minute--);
                    }
                });
            }
        };
        timer.schedule(task, 0, 1 * 60 * 1000);
    }

    public void closeTask() {
        timer.cancel();
        timer = null;
        mHandler=null;
    }


    public interface TimeCallback {
        void onTime(int minute);
    }
}
