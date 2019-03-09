package com.sf.utility;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.function.Function;

public class ScheduleHandler extends Handler {

  private final int mInterval;
  private final Runnable mRunnable;
  private boolean mStopped = true;
  private Function<Integer, Integer> mEvaluator;

  public ScheduleHandler(Looper looper, int interval, Runnable runnable) {
    super(looper);
    mInterval = interval;
    mRunnable = runnable;
  }

  public ScheduleHandler(int interval, Runnable runnable) {
    mInterval = interval;
    mRunnable = runnable;
  }

  public void setIntervalEvaluator(Function<Integer, Integer> evaluator) {
    mEvaluator = evaluator;
  }

  @Override
  public void handleMessage(Message msg) {
    if (mStopped) {
      return;
    }
    mRunnable.run();
    sendEmptyMessageDelayed(0, getInterval());
  }

  public void start() {
    if (!mStopped) {
      return;
    }
    mStopped = false;
    sendEmptyMessage(0);
  }

  public void delayStart() {
    if (!mStopped) {
      return;
    }
    mStopped = false;
    sendEmptyMessageDelayed(0, getInterval());
  }

  public void stop() {
    mStopped = true;
    removeMessages(0);
  }

  public boolean isRunning() {
    return !mStopped;
  }

  private long getInterval() {
    if (mEvaluator == null) {
      return mInterval;
    }
    return mEvaluator.apply(mInterval);
  }
}
