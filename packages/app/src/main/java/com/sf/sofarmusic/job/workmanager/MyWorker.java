package com.sf.sofarmusic.job.workmanager;

import java.util.UUID;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sf.sofarmusic.job.JobConstant;
import com.sf.sofarmusic.job.ProcessProtectedService;
import com.sf.utility.AppUtil;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

  private Handler mHandler = new Handler(Looper.getMainLooper());

  public MyWorker(@NonNull Context context,
    @NonNull WorkerParameters workerParams) {
    super(context, workerParams);
  }

  @NonNull
  @Override
  public Result doWork() {
    // Do some work here，在子线程
    Log.d(JobConstant.TAG,
        "doWork-WorkManager== thread:" + Thread.currentThread().getName() + " process:"
            + AppUtil.getProcessName(getApplicationContext()));

//    ToastUtil.startLong(getApplicationContext(), "WorkManager-doWork");
    ProcessProtectedService.startService(getApplicationContext());
    return Result.success();
  }


  /**
   * 取消任务，有多个方法
   */
  public void cancelTask(UUID id) {
    WorkManager.getInstance().cancelWorkById(id);
    WorkManager.getInstance().cancelAllWork();
    WorkManager.getInstance().cancelAllWorkByTag("group");
  }

  /**
   * 一个样本Task
   */
  public void buildSampleRequest() {
    // 传递参数
    Data myData = new Data.Builder()
        .putInt("key", 1)
        .build();

    Constraints constraints = new Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(false)
        .build();

    OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyWorker.class)
        .setConstraints(constraints) // 添加约束条件
        .addTag("group") // 对任务添加分组，便于后续取消job
        .setInputData(myData) // 传递参数，可以在doWork中调用getIntData获取
        .build();
    WorkManager.getInstance().enqueue(request);
  }
}
