package com.sf.sofarmusic.exception;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.sf.base.util.FileUtil;
import com.sf.utility.IOUtil;


/**
 * 用于捕获异常信息
 */
public class CrashHandler implements UncaughtExceptionHandler {
  public static final String TAG = "CrashHandler";
  // 系统默认的UncaughtException处理类
  private UncaughtExceptionHandler mDefaultHandler;
  // CrashHandler实例
  private static CrashHandler instance;
  // 程序的Context对象
  private Context mContext;

  // 异常信息
  public static final String EXCEPETION_INFOS_STRING = "EXCEPETION_INFOS_STRING";
  // 应用包信息
  public static final String PACKAGE_INFOS_MAP = "PACKAGE_INFOS_MAP";
  // 设备数据信息
  public static final String BUILD_INFOS_MAP = "BUILD_INFOS_MAP";
  // 系统常规配置信息
  public static final String SYSTEM_INFOS_MAP = "SYSTEM_INFOS_MAP";
  // 手机安全配置信息
  public static final String SECURE_INFOS_MAP = "SECURE_INFOS_MAP";
  // 内存情况信息
  public static final String MEMORY_INFOS_STRING = "MEMORY_INFOS_STRING";
  public static final String VERSION_NAME = "versionName";
  public static final String VERSION_CODE = "versionCode";

  // 用来存储设备信息和异常信息
  private Map<String, Object> infos = new HashMap<>();
  private Map<String, String> mPackageInfos = new HashMap<>();
  private Map<String, String> mDeviceInfos = new HashMap<>();
  private Map<String, String> mSystemInfos = new HashMap<>();
  private Map<String, String> mSecureInfos = new HashMap<>();
  private String mExceptionInfos;
  private String mMemInfos;

  // 用于格式化日期,作为日志文件名的一部分
  private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

  /**
   * 保证只有一个CrashHandler实例
   */
  private CrashHandler() {}

  /**
   * 获取CrashHandler实例 ,单例模式
   */
  public static CrashHandler getInstance() {
    if (instance == null)
      instance = new CrashHandler();
    return instance;
  }

  /**
   * 初始化
   */
  public void init(Context context) {
    mContext = context;
    // 获取系统默认的UncaughtException处理器
    mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    // 设置该CrashHandler为程序的默认处理器
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  /**
   * 当UncaughtException发生时会转入该函数来处理
   */
  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    Log.i(TAG, "uncaughtException");
    handleException(ex);
    if (mDefaultHandler != null) {
      mDefaultHandler.uncaughtException(thread, ex);
    }
  }

  /**
   * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
   *
   * @param ex
   * @return true:如果处理了该异常信息;否则返回false.
   */
  private boolean handleException(Throwable ex) {
    Log.i(TAG, "handleException");
    if (ex == null) {
      return false;
    }
    // 使用Toast来显示异常信息
    new Thread() {
      @Override
      public void run() {
        Looper.prepare();
        Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
        Looper.loop();
      }
    }.start();
    // 收集信息
    collectInfos(ex);

    // 保存日志文件
    saveCatchInfo2File();

    // 上传至服务端
    sendCrash2Service();
    return true;
  }

  /**
   * 收集相关信息
   */
  private void collectInfos(Throwable ex) {
    mExceptionInfos = collectExceptionInfos(ex);
    collectPackageInfos();
    collectDeviceInfos();
    collectSystemInfos();
    collectSecureInfos();
    mMemInfos = collectMemInfos();

    // 将信息储存到一个总的Map中提供给上传动作回调
    infos.put(EXCEPETION_INFOS_STRING, mExceptionInfos);
    infos.put(PACKAGE_INFOS_MAP, mPackageInfos);
    infos.put(BUILD_INFOS_MAP, mDeviceInfos);
    infos.put(SYSTEM_INFOS_MAP, mSystemInfos);
    infos.put(SECURE_INFOS_MAP, mSecureInfos);
    infos.put(MEMORY_INFOS_STRING, mMemInfos);
  }

  /**
   * 获取异常信息
   */
  private String collectExceptionInfos(Throwable ex) {
    Writer writer = new StringWriter();
    PrintWriter printWriter = new PrintWriter(writer);
    ex.printStackTrace(printWriter);
    Throwable cause = ex.getCause();
    while (cause != null) {
      cause.printStackTrace(printWriter);
      // 换行 每个异常栈之间换行
      printWriter.append("\r\n");
      cause = cause.getCause();
    }
    printWriter.close();
    return writer.toString();
  }



  /**
   * 获取应用包参数信息
   */
  private void collectPackageInfos() {
    try {
      PackageManager pm = mContext.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
      if (pi != null) {
        String versionName = pi.versionName == null ? "null" : pi.versionName;
        String versionCode = pi.versionCode + "";
        mPackageInfos.put(VERSION_NAME, versionName);
        mPackageInfos.put(VERSION_CODE, versionCode);
      }
    } catch (NameNotFoundException e) {
      Log.e(TAG, "an error occured when collect package info", e);
    }
  }



  /**
   * 获取设备信息
   */
  private void collectDeviceInfos() {
    Field[] fields = Build.class.getDeclaredFields();
    for (Field field : fields) {
      try {
        field.setAccessible(true);
        mDeviceInfos.put(field.getName(), field.get(null).toString());
        Log.d(TAG, field.getName() + " : " + field.get(null));
      } catch (Exception e) {
        Log.e(TAG, "an error occured when collect crash info", e);
      }
    }
  }


  /**
   * 获取系统常规设定属性
   */
  private void collectSystemInfos() {
    Field[] fields = Settings.System.class.getFields();
    for (Field field : fields) {
      if (!field.isAnnotationPresent(Deprecated.class)
          && field.getType() == String.class) {
        try {
          String value =
              Settings.System.getString(mContext.getContentResolver(), (String) field.get(null));
          if (value != null) {
            mSystemInfos.put(field.getName(), value);
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }


  /**
   * 获取系统安全设置信息
   */
  private void collectSecureInfos() {
    Field[] fields = Settings.Secure.class.getFields();
    for (Field field : fields) {
      if (!field.isAnnotationPresent(Deprecated.class)
          && field.getType() == String.class
          && field.getName().startsWith("WIFI_AP")) {
        try {
          String value =
              Settings.Secure.getString(mContext.getContentResolver(), (String) field.get(null));
          if (value != null) {
            mSecureInfos.put(field.getName(), value);
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }


  /**
   * 获取内存信息
   */
  private String collectMemInfos() {
    BufferedReader br = null;
    StringBuffer sb = new StringBuffer();

    ArrayList<String> commandLine = new ArrayList<>();
    commandLine.add("dumpsys");
    commandLine.add("meminfo");
    commandLine.add(Integer.toString(Process.myPid()));
    try {
      java.lang.Process process = Runtime.getRuntime()
          .exec(commandLine.toArray(new String[commandLine.size()]));
      br = new BufferedReader(new InputStreamReader(process.getInputStream()), 8192);

      while (true) {
        String line = br.readLine();
        if (line == null) {
          break;
        }
        sb.append(line);
        sb.append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      IOUtil.closeQuietly(br);
    }
    return sb.toString();
  }



  /**
   * 保存错误信息到文件中
   */
  private String saveCatchInfo2File() {
    StringBuffer sb = new StringBuffer();
    sb.append(getInfosStr(mPackageInfos));
    sb.append(getInfosStr(mDeviceInfos));
    sb.append(mExceptionInfos);
    try {
      long timestamp = System.currentTimeMillis();
      String time = formatter.format(new Date());
      String fileName = "crash-" + time + "-" + timestamp + ".txt";

      String dir = FileUtil.getCrashDir(mContext);
      File file = new File(dir, fileName);
      if (!file.exists()) {
        file.createNewFile();
      }
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(sb.toString().getBytes());
      fos.close();
      return fileName;
    } catch (Exception e) {
      Log.e(TAG, "an error occured while writing file...", e);
    }
    return null;
  }

  /**
   * 发送错误信息到服务端
   */
  private void sendCrash2Service() {
    StringBuffer sb = new StringBuffer();

  }

  /**
   * 将HashMap遍历转换成StringBuffer
   */
  public String getInfosStr(Map<String, String> infos) {
    StringBuffer sb = new StringBuffer();
    for (Map.Entry<String, String> entry : infos.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      sb.append(key + "=" + value + "\r\n");
    }
    return sb.toString();
  }


  /**
   * 重新启动客户端
   */
  private void restartApplication() {
    final Intent intent =
        mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    mContext.startActivity(intent);
  }

  /**
   * 退出应用
   */
  private void killProcess() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      Log.i(TAG, "error : ", e);
    }
    // 退出程序
    android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(1);
  }
}
