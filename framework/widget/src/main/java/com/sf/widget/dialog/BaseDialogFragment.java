package com.sf.widget.dialog;

import android.content.DialogInterface;
import com.sf.utility.CollectionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 封装一些弹窗基本操作
 *
 * 支持弹窗队列（一个一个弹）
 */
public class BaseDialogFragment extends DialogFragment {

  public static WeakHashMap<FragmentManager, List<BaseDialogFragment>> sDialogFragments =
      new WeakHashMap<>();

  private String mTag;
  private List<BaseDialogFragment> mQueueDialogs;

  // 弹窗展示是否受弹窗队列影响
  private boolean mShowOnDialogList = false;

  /**
   * 重写show方法，保证弹窗的展示受弹窗队列控制
   */
  @Override
  public final void show(FragmentManager manager, String tag) {
    // 不受弹窗队列影响
    if (!mShowOnDialogList) {
      showImmediate(manager, tag);
      return;
    }

    mQueueDialogs = sDialogFragments.get(manager);
    if (mQueueDialogs == null) {
      mQueueDialogs = new ArrayList<>();
      sDialogFragments.put(manager, mQueueDialogs);
    }
    if (mQueueDialogs.contains(this)) {
      return;
    }

    this.mTag = tag;
    if (mQueueDialogs.isEmpty()) {
      mQueueDialogs.add(this);
      superShow(manager, tag);
    } else {
      mQueueDialogs.add(this);
    }
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);

    if (!CollectionUtil.isEmpty(mQueueDialogs)) {
      mQueueDialogs.remove(this);
      popShowFragment();
    }
  }

  private void popShowFragment() {
    if (!CollectionUtil.isEmpty(mQueueDialogs)) {
      BaseDialogFragment baseDialogFragment = getFirstFragmentFromList(mQueueDialogs);
      if (baseDialogFragment != null) {
        if (!baseDialogFragment.isAdded()) {
          baseDialogFragment.superShow(getFragmentManager(), baseDialogFragment.mTag);
        } else {
          mQueueDialogs.remove(baseDialogFragment);
          popShowFragment();
        }
      }
    }
  }

  private BaseDialogFragment getFirstFragmentFromList(List<BaseDialogFragment> fragmentList) {
    for (int i = 0; i < fragmentList.size(); i++) {
      if (fragmentList.get(i) != null) {
        return fragmentList.get(i);
      }
    }
    return null;
  }


  /**
   * 真正的弹窗展示
   */
  private void superShow(FragmentManager manager, String tag) {
    super.show(manager, tag);
  }

  /**
   * 不受弹窗队列影响，立即展示
   */
  public final void showImmediate(FragmentManager manager, String tag) {
    this.mTag = tag;
    superShow(manager, tag);
  }
}
