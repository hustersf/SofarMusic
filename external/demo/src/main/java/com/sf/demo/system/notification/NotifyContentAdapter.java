package com.sf.demo.system.notification;

import android.widget.TextView;

import com.sf.demo.R;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class NotifyContentAdapter extends RecyclerAdapter<NotifyContent> {

  private TextView mPackageTv;
  private TextView mContentTv;
  private TextView mSourceTv;

  /**
   * 数据插在最前面
   */
  public void setData(NotifyContent content) {
    mDatas.add(0, content);
    notifyItemInserted(0);
  }

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.adapter_notify_content;
  }

  @Override
  protected void onCreateView(RecyclerViewHolder holder) {
    mPackageTv = holder.getView(R.id.tv_package);
    mContentTv = holder.getView(R.id.tv_content);
    mSourceTv = holder.getView(R.id.tv_source);
  }

  @Override
  protected void onBindData(NotifyContent data, RecyclerViewHolder holder) {
    mPackageTv.setText(data.mPackageName + "(" + data.mAppName + ")");
    mSourceTv.setText(data.mSoruce);
    mContentTv.setText(data.mTitle + "\n" + data.mContent);
  }
}
