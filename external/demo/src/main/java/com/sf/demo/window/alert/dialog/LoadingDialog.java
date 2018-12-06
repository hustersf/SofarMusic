package com.sf.demo.window.alert.dialog;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.sf.demo.R;

/**
 * Created by sufan on 17/6/28.
 */

public class LoadingDialog extends Dialog {

    private Context mContext;
    private Timer mTimer = null;// 定时器
    private long mTimeOut = 0;// 默认timeOut为0即无限大
    private OnTimeOutListener mTimeOutListener = null;// timeOut后的处理器

    private Animation animation;
    private ImageView circle_iv;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (isShowing()) {
                if (mTimeOutListener != null) {
                    mTimeOutListener.onTimeOut(LoadingDialog.this);
                    dismiss();
                } else {
                    dismiss();
                }
            }
        }
    };

    public LoadingDialog(Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_loading);
        circle_iv = (ImageView) findViewById(R.id.circle_iv);
        animation = getRotateAnimation();
        getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);// 去掉背景框
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mTimeOut != 0) {
            mTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Message msg = mHandler.obtainMessage();
                    mHandler.sendMessage(msg);
                }
            };
            mTimer.schedule(timerTask, mTimeOut);
        }
        circle_iv.startAnimation(animation);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        circle_iv.clearAnimation();
    }

    @Override
    public void show() {
        if (!this.isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    public RotateAnimation getRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 359,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setStartOffset(-1);
        return rotateAnimation;
    }

    public void setTimeOut(long t, OnTimeOutListener timeOutListener) {
        mTimeOut = t;
        if (timeOutListener != null) {
            this.mTimeOutListener = timeOutListener;
        }
    }

    public interface OnTimeOutListener {
        void onTimeOut(Dialog dialog);
    }

    public static LoadingDialog createLoadingDialog(Context context,long time, OnTimeOutListener listener){

        LoadingDialog dialog=new LoadingDialog(context);
        if (time != 0) {
            dialog.setTimeOut(time, listener);
        }
        dialog.setCancelable(false);
        return dialog;
    }
}
