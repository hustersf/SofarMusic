package com.sf.sofarmusic.widget.recyclerview;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * areItemsTheSame和areContentsTheSame都返回true时
 * 则认为新旧两个item的数据是相同的，即不需要刷新该item
 */
public class CommonDiffCallback<MODEL> extends DiffUtil.Callback {

  private final List<MODEL> mOldData;
  private final List<MODEL> mNewData;


  public CommonDiffCallback(List<MODEL> oldData, List<MODEL> newData) {
    mOldData = oldData;
    mNewData = newData;
  }


  @Override
  public int getOldListSize() {
    return mOldData.size();
  }

  @Override
  public int getNewListSize() {
    return mNewData.size();
  }

  @Override
  public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    final MODEL oldModel = mOldData.get(oldItemPosition);
    final MODEL newModel = mNewData.get(newItemPosition);
    if (oldModel == null || newModel == null) {
      return oldModel == newModel;
    }
    return oldModel.equals(newModel);
  }

  //只有areItemsTheSame返回ture时，该方法才会被调用
  @Override
  public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    return true;
  }
}
