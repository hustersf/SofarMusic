package com.sf.demo.system.notification;

import android.content.Context;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

import java.util.List;

public class NotifyContentAdapter extends RecyclerAdapter<NotifyContent> {

  private TextView mPackageTv;
  private TextView mContentTv;
  private TextView mSourceTv;

  public NotifyContentAdapter(Context context) {
    super(context);
  }

  public NotifyContentAdapter(Context context, List<NotifyContent> datas) {
    super(context, datas);
  }

  /**
   * 数据插在最前面
   */
  public void setData(NotifyContent content) {
    mDatas.add(0, content);
    notifyItemInserted(0);
  }

  @Override
  protected int getItemLayoutId() {
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
    mPackageTv.setText(data.mPackageName);
    mSourceTv.setText(data.mSoruce);
    mContentTv.setText(data.mContent);
  }
}
