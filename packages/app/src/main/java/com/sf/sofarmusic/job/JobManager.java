package com.sf.sofarmusic.job;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sf.sofarmusic.job.jobdispatcher.MyJobService;
import com.sf.sofarmusic.job.jobscheduler.JobSchedulerService;
import com.sf.sofarmusic.job.workmanager.MyWorker;
import com.sf.utility.LogUtil;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class JobManager {

  private int jobId = 0; // JobSchedluer需要传递任务id

  private static JobManager sInstance = null;
  private Context mContext;

  private JobManager(Context context) {
    mContext = context.getApplicationContext();
  }

  public static JobManager getInstance(Context context) {
    if (sInstance == null) {
      synchronized (JobManager.class) {
        if (sInstance == null) {
          sInstance = new JobManager(context);
        }
      }
    }
    return sInstance;
  }

  /**
   * jobType {@link JobType}
   * 进程杀死和关机重启，均能被再次调起
   */
  public void startJob(@JobType int jobType) {
    if (jobType == JobType.JOB_SCHEDULER) {
      excuteJobSchedluer();
    } else if (jobType == JobType.JOB_DISPATCHER) {
      excuteJobDispatcher();
    } else if (jobType == JobType.WORK_MANAGER) {
      excuteWorkManager();
    }
  }

  /**
   * {@link JobSchedulerService#buildSampleJobInfo()}
   * 实现周期执行有两种方式
   * 1. setMinimumLatency和setOverrideDeadline，然后在onStartJob中，调用excuteJobSchedluer
   * 2、setPeriodic,但时间间隔有阈值 intervalMillis>=15min，flexMillis>=5min
   */
  private void excuteJobSchedluer() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      JobInfo.Builder builder =
          new JobInfo.Builder(jobId++, new ComponentName(mContext, JobSchedulerService.class));
      builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
          .setPersisted(true)
          .setRequiresCharging(false)
          .setRequiresDeviceIdle(false)
          .setPeriodic(JobConstant.PERIOD); // job执行周期，至少为15min
      // .setMinimumLatency(10 * 1000) // job执行的最小时间延迟
      // .setOverrideDeadline(20 * 1000); // job执行的最大时间延迟

      JobScheduler tm = (JobScheduler) mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
      tm.schedule(builder.build());
      LogUtil.d(JobConstant.TAG, "excuteJobSchedluer");
    }
  }

  /**
   * {@link MyJobService#buildSampleJob()}
   * 周期执行,但除过第一次执行外，其它执行可能时间偏长一点
   * 需要设置安装了谷歌服务
   */
  private void excuteJobDispatcher() {
    int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mContext);
    if (resultCode == ConnectionResult.SUCCESS) {
      FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(mContext));
      Job job = dispatcher.newJobBuilder()
          .setService(MyJobService.class)
          .setTag("my-unique-tag")
          .setRecurring(true)
          .setReplaceCurrent(true)
          .setTrigger(Trigger.executionWindow(10, 20))
          .setLifetime(Lifetime.FOREVER)
          .setConstraints(Constraint.ON_ANY_NETWORK)
          .build();
      dispatcher.schedule(job);
      LogUtil.d(JobConstant.TAG, "excuteJobDispatcher");
    } else {
      LogUtil.d(JobConstant.TAG, "此设备不支持谷歌服务");
      excuteWorkManager();
    }
  }

  /**
   * {@link MyWorker#buildSampleRequest()}
   * 周期性执行
   * 第一次立马执行，第二次执行至少间隔15min(源码会判断时间)
   */
  private void excuteWorkManager() {
    Constraints constraints = new Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresCharging(false)
        .build();

    PeriodicWorkRequest request =
        new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build();

    WorkManager.getInstance().enqueue(request);

    LogUtil.d(JobConstant.TAG, "excuteWorkManager");
  }



  @Retention(RetentionPolicy.SOURCE)
  public @interface JobType {
    int JOB_SCHEDULER = 0; // 安卓5.0自带API JobScheduler
    int JOB_DISPATCHER = 1; // 第三方库 https://github.com/firebase/firebase-jobdispatcher-android
    int WORK_MANAGER = 2; // 安卓扩展包 androidx.work.
  }

}
