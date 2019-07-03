package com.sf.demo.system.sensor;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.sf.utility.LogUtil;

/**
 * 简化计步传感器的使用
 */
public class StepCountSensorManagerHelper {

  private static final String TAG = "StepCountSensorManagerHelper";

  private SensorManager mSensorManager;

  private static StepCountSensorManagerHelper instance;
  private Context mContext;
  private float stepCount;

  public float getStepCount() {
    return stepCount;
  }

  private SensorEventListener eventListener = new SensorEventListener() {

    @Override
    public void onSensorChanged(SensorEvent event) {
      LogUtil.d(TAG, "onSensorChanged_type:" + event.sensor.getType());
      if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
        stepCount = event.values[0];
        LogUtil.d(TAG, "今日步数：" + stepCount);
      }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
      LogUtil.d(TAG, "onAccuracyChanged_type:" + sensor.getType());
    }
  };


  public static StepCountSensorManagerHelper getInstance(Context context) {
    if (instance == null) {
      synchronized (StepCountSensorManagerHelper.class) {
        if (instance == null) {
          instance = new StepCountSensorManagerHelper(context);
        }
      }
    }
    return instance;
  }

  private StepCountSensorManagerHelper(Context context) {
    mContext = context.getApplicationContext();
    mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
  }

  public boolean isSupportStepCountSensor() {
    return mContext.getPackageManager()
        .hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
  }

  /**
   * 注册计步传感器
   */
  public void registerStepCountSensor() {
    if (mSensorManager == null) {
      return;
    }

    Sensor stepCountSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
    if (stepCountSensor == null) {
      return;
    }

    mSensorManager.registerListener(eventListener, stepCountSensor,
        SensorManager.SENSOR_DELAY_NORMAL);

    LogUtil.d(TAG, "registerStepCountSensor");
  }

  /**
   * 反注册计步传感器
   */
  public void unRegisterStepCountSensor() {
    if (mSensorManager != null) {
      Sensor stepCountSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
      mSensorManager.unregisterListener(eventListener, stepCountSensor);
      LogUtil.d(TAG, "unRegisterStepCountSensor");
    }
  }


}
