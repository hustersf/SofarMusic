package com.sf.demo.system.notification;

import android.widget.TextView;

import com.sf.demo.R;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class NotifyContentAdapter extends RecyclerAdapter<NotifyContent> {

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
  protected void onBindData(NotifyContent data, RecyclerViewHolder holder) {

    TextView packageTv = holder.getView(R.id.tv_package);
    TextView contentTv = holder.getView(R.id.tv_content);
    TextView sourceTv = holder.getView(R.id.tv_source);

    packageTv.setText(data.mPackageName + "(" + data.mAppName + ")");
    contentTv.setText(data.mSoruce);
    sourceTv.setText(data.mTitle + "\n" + data.mContent);
  }
}
