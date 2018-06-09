package com.sf.sofarmusic.demo.window.keyboard.verifycode;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 定时器 控制短信验证码发送
 * */
public class SmsTimer extends CountDownTimer {

	private Button button;
	private long countDownInterval = 1000;
	private Context mContext;

	/**
	 * @param millisInFuture
	 *            定时总时间
	 * @param countDownInterval
	 *            执行时间间隔
	 * @param button
	 *            获取验证码按钮
	 * */
	public SmsTimer(long millisInFuture, long countDownInterval, Button button,
			Context context) {
		super(millisInFuture - 1, countDownInterval);
		// TODO Auto-generated constructor stub
		this.button = button;
		this.countDownInterval = countDownInterval;
		mContext = context;
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		button.setBackground(null);
		button.setClickable(true);
		button.setText("重新发送");
		button.setTextColor(Color.parseColor("#4F8BE8"));
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
		button.setBackground(null);
		button.setClickable(false);
		button.setText(millisUntilFinished / countDownInterval + "S");
		button.setTextColor(Color.parseColor("#4F8BE8"));
	}

	public void stop() {
		this.cancel();
		this.onFinish();
	}

}
