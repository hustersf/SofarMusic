package com.sf.sofarmusic.job.jobscheduler;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.sf.sofarmusic.job.JobConstant;
import com.sf.sofarmusic.job.JobManager;
import com.sf.sofarmusic.job.ProcessProtectedService;
import com.sf.utility.AppUtil;
import com.sf.utility.ToastUtil;

/**
 * 需要在xml中申请权限 android:permission="android.permission.BIND_JOB_SERVICE"
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {

  private int jobId = 0;

  /**
   * 当满足任务执行的条件时，将由系统调用此方法
   * 返回false，系统假设这个方法返回时任务已经执行完毕
   * 返回true，系统假设这个任务正要被执行，当任务执行完毕时，需要调用jobFinished来通知系统
   */
  @Override
  public boolean onStartJob(JobParameters params) {
    // Do some work here
    Log.d(JobConstant.TAG,
        "onStartJob-JobScheduler== thread:" + Thread.currentThread().getName() + " process:"
            + AppUtil.getProcessName(getApplicationContext()));
    ToastUtil.startLong(this, "JobScheduler-onStartJob");
    ProcessProtectedService.startService(getApplicationContext());
    return false;
  }

  /**
   * 当任务不可能有执行的机会时，将由系统调用此方法
   */
  @Override
  public boolean onStopJob(JobParameters params) {
    return false;
  }

  /**
   * setPeriodic 与 setMinimumLatency,setOverrideDeadline 不可共用
   */
  public void buildSampleJobInfo() {
    JobInfo.Builder builder =
        new JobInfo.Builder(jobId++, new ComponentName(this, JobSchedulerService.class));
    builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // 设置job执行需要的网络
        .setRequiresCharging(false) // 是否需要充电
        .setRequiresDeviceIdle(false) // 是否需要等设备出于空闲状态的时候
        .setPersisted(true) // 设置设备重启后，这个任务是否还要保留
        .setMinimumLatency(1 * 1000) // job执行的最小时间延迟
        .setOverrideDeadline(2 * 1000) // job执行的最大时间延迟
        .setPeriodic(1000) // job 每1s 执行一次
        .build();
  }

}
