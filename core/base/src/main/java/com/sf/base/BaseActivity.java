package com.sf.base;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sf.base.callback.ActivityCallback;
import com.sf.base.util.AppManager;
import com.sf.base.util.eventbus.BindEventBus;
import com.sf.base.view.LoadView;
import com.sf.libskin.base.SkinBaseActivity;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;

/**
 * Created by sufan on 17/2/28.
 * 抽出一些Activity公共公共的逻辑
 */
public class BaseActivity extends SkinBaseActivity {
  private static final String TAG = "BaseActivity";

  public static final String HOME_ACTIVITY_CLASS_NAME = "com.sf.sofarmusic.main.MainActivity";

  public BaseActivity baseAt;
  public View rootView;

  // 设置启动和退出Activity的动画
  public static final String START_EXIT_PAGE_ANIMATION = "start_exit_page_animation";
  public static final String START_ENTER_PAGE_ANIMATION = "start_enter_page_animation";
  public static final String FINISH_EXIT_PAGE_ANIMATION = "finish_exit_page_animation";
  public static final String FINISH_ENTER_PAGE_ANIMATION = "finish_enter_page_animation";
  public static final int NO_ANIM = 0;


  public FrameLayout mContentContainer; // 获取根视野
  private View mLoadView;
  private LoadView loadView;

  // activity请求回调相关
  private SparseArray<ActivityCallback> callbacks = new SparseArray<>();
  private static final int REQUEST_CODE = 100;

  private List<ConfigurationChangedListener> mConfigurationChangedListeners = new ArrayList<>();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    baseAt = this;
    rootView = getWindow().getDecorView().findViewById(android.R.id.content);
    AppManager.getAppManager().addActivity(baseAt);

    // 初始化loadview
    mContentContainer = (FrameLayout) rootView;
    mLoadView = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_loadview, null);
    loadView = mLoadView.findViewById(R.id.loadview);
    dynamicAddView(loadView, "loadColor", R.color.themeColor);
    dynamicAddView(loadView, "loadTextColor", R.color.main_text_color);
    loadView.setVisibility(View.GONE); // 默认隐藏

    if (hasBindEventBus() && !EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
  }

  /**
   * 暂时只检两层 当前类和其直接父类
   */
  private boolean hasBindEventBus() {
    if (this.getClass().isAnnotationPresent(BindEventBus.class)
      || this.getClass().getSuperclass().isAnnotationPresent(BindEventBus.class)) {
      return true;
    }
    return false;
  }

  /**
   * 在onCreate之后执行
   *
   * @param savedInstanceState
   */
  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // 将loadview放进布已经加载好的xml布局里
    FrameLayout.LayoutParams layoutParams =
      new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.CENTER;
    mContentContainer.addView(mLoadView, layoutParams);
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    AppManager.getAppManager().removeActivity(baseAt);
    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this);
    }

    if (mConfigurationChangedListeners != null) {
      mConfigurationChangedListeners.clear();
    }
  }

  /**
   * 显示loadview
   */
  public void show() {
    loadView.setVisibility(View.VISIBLE);
  }

  /**
   * 隐藏loadview
   */
  public void dismiss() {
    loadView.setVisibility(View.GONE);
  }

  /**
   * activity请求回调
   * 继承自该activity的页面，统一调用该方法
   */
  public void startActivityForResult(Intent intent, ActivityCallback callback) {
    callbacks.put(REQUEST_CODE, callback);
    startActivityForResult(intent, REQUEST_CODE);
  }

  /**
   * activity请求回调
   * 继承自该activity的页面，统一调用该方法
   */
  public void startActivityForResult(Intent intent, int requestCode, ActivityCallback callback) {
    callbacks.put(requestCode, callback);
    startActivityForResult(intent, requestCode);
  }

  /**
   * Activity回调成功
   */
  public void setActivityResultOK(Intent intent) {
    if (intent == null) {
      setResult(Activity.RESULT_OK, new Intent());
    } else {
      setResult(Activity.RESULT_OK, intent);
    }
    finish();
  }

  /**
   * Activity回调失败
   */
  public void setActivityResultCancel(Intent intent) {
    if (intent == null) {
      setResult(Activity.RESULT_CANCELED, new Intent());
    } else {
      setResult(Activity.RESULT_CANCELED, intent);
    }
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ActivityCallback callback = callbacks.get(requestCode);
    callbacks.remove(requestCode);
    if (callback == null) {
      return;
    }

    if (resultCode == Activity.RESULT_OK) {
      callback.onResult(data);
    } else {
      callback.onCancel(data);
    }
  }

  @Override
  public void startActivity(Intent intent) {
    super.startActivity(intent);
    overridePendingTransition(
      getIntent().getIntExtra(START_ENTER_PAGE_ANIMATION, R.anim.right_slide_in),
      getIntent().getIntExtra(START_EXIT_PAGE_ANIMATION, R.anim.placeholder_anim));
  }

  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(
      getIntent().getIntExtra(FINISH_ENTER_PAGE_ANIMATION, NO_ANIM),
      getIntent().getIntExtra(FINISH_EXIT_PAGE_ANIMATION, R.anim.right_slide_out));

    if (AppManager.getAppManager().isLastActivity() && !HOME_ACTIVITY_CLASS_NAME
      .equals(AppManager.getAppManager().currentActivity().getClass().getName())) {
      onFinishAsLastActivity();
    }
  }

  /**
   * 启动主页
   */
  protected void onFinishAsLastActivity() {
    try {
      Intent intent = new Intent();
      intent.setClassName(getPackageName(), HOME_ACTIVITY_CLASS_NAME);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    } catch (Exception e) {
    }
  }


  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    if (mConfigurationChangedListeners != null) {
      for (int i = 0; i < mConfigurationChangedListeners.size(); i++) {
        if (mConfigurationChangedListeners.get(i) != null) {
          mConfigurationChangedListeners.get(i).onConfigurationChanged(newConfig);
        }
      }
    }
  }

  public void addOnConfigurationChangedListener(ConfigurationChangedListener listener) {
    if (mConfigurationChangedListeners != null
      && !mConfigurationChangedListeners.contains(listener)) {
      mConfigurationChangedListeners.add(listener);
    }
  }

  public void removeOnConfigurationChangedListener(ConfigurationChangedListener listener) {
    if (mConfigurationChangedListeners != null) {
      mConfigurationChangedListeners.remove(listener);
    }
  }

  public interface ConfigurationChangedListener {
    void onConfigurationChanged(Configuration newConfig);
  }

}


