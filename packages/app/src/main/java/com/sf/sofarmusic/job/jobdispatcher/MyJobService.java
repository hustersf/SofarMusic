package com.sf.sofarmusic.job.jobdispatcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.sf.sofarmusic.job.JobConstant;
import com.sf.sofarmusic.job.JobManager;
import com.sf.sofarmusic.job.ProcessProtectedService;
import com.sf.sofarmusic.menu.profile.ProfileActivity;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.utility.AppUtil;
import com.sf.utility.ToastUtil;

/**
 * 用于保活MyJobService所在的进程
 */
public class MyJobService extends JobService {

  /**
   * 返回值，表示该任务是否执行完毕了
   */
  @Override
  public boolean onStartJob(JobParameters job) {
    // Do some work here
    Log.d(JobConstant.TAG, "onStartJob-JobDispatcher== thread:" + Thread.currentThread().getName() + " process:"
            + AppUtil.getProcessName(getApplicationContext()));

    ToastUtil.startLong(this,"obdispatcher-onStartJob");
    ProcessProtectedService.startService(getApplicationContext());
    return false;
  }

  /**
   * 返回值，表示该任务是否需要重试
   */
  @Override
  public boolean onStopJob(JobParameters job) {
    return false;
  }


  /**
   *
   * 构造Job的例子,供参考用
   */
  public void buildSampleJob() {
    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

    // 构造一些额外参数
    Bundle myExtrasBundle = new Bundle();
    myExtrasBundle.putString("some_key", "some_value");

    dispatcher.newJobBuilder()
        .setService(MyJobService.class)
        .setTag("sample-job") // 给job添加tag,便于后面通过tag取消job
        .setRecurring(false) // 是否周期性的执行该job
        .setLifetime(Lifetime.FOREVER) // job的生命周期
        .setTrigger(Trigger.executionWindow(0, 60)) // start between 0 and 60 seconds from now
        .setReplaceCurrent(false) // 当有相同tag的任务时，之前的任务是否被覆盖
        .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL) // Job启动失败时的重试策略
        .setConstraints(Constraint.ON_ANY_NETWORK, Constraint.DEVICE_IDLE) // 构造一些job执行的约束条件
        .setExtras(myExtrasBundle) // 传递一些额外的参数
        .build();
  }
}
