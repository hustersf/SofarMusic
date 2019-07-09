package com.sf.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sf.base.util.eventbus.BindEventBus;
import com.sf.libskin.base.SkinBaseFragment;
import com.sf.utility.LogUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by sufan on 17/2/28.
 */
public class BaseFragment extends SkinBaseFragment {

  private static final String TAG = "BaseFragment";

  public BaseActivity activity;
  private boolean isFragmentVisible = false;
  private boolean isInitVisible = false;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    activity = (BaseActivity) context;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this);
    }
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (this.getClass().isAnnotationPresent(BindEventBus.class)
        && !EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  /**
   * 调用场景，ViewPager+Fragment的场景
   *
   * 调用时机
   * 1.Adapter初始化时调用，且在任何Fragment的生命周期之前
   * 2.在切换Fragment时调用
   *
   * isVisibleToUser 当前Fragment是否可见
   */
  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);

    if (isVisibleToUser && !isFragmentVisible) {
      onVisible();
    } else if (!isVisibleToUser && isFragmentVisible) {
      onInVisible();
    }
  }

  /**
   * fragment第一次可见时回调
   */
  protected void onFirstVisible() {
    isInitVisible = true;
    LogUtil.d(TAG, this.getClass().getName() + ":onFirstVisible");
  }


  /**
   * fragment可见时回调
   */
  protected void onVisible() {
    isFragmentVisible = true;
    if (!isInitVisible) {
      onFirstVisible();
    }
    LogUtil.d(TAG, this.getClass().getName() + ":onVisible");
  }

  /**
   * fragment不可见时回调
   */
  protected void onInVisible() {
    isFragmentVisible = false;
    LogUtil.d(TAG, this.getClass().getName() + ":onInVisible");
  }

  /**
   * 当前Fragment是否可见
   */
  public boolean isFragmentVisible() {
    return isFragmentVisible;
  }
}
