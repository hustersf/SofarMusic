package com.sf.demo.system.notification;

import android.view.View;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class NotifyContentAdapter extends RecyclerAdapter<NotifyContent> {

  /**
   * 数据插在最前面
   */
  public void setData(NotifyContent content) {
    items.add(0, content);
    notifyItemInserted(0);
  }

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.adapter_notify_content;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new NotifyViewHolder(itemView);
  }

  class NotifyViewHolder extends RecyclerViewHolder<NotifyContent> {
    TextView packageTv;
    TextView contentTv;
    TextView sourceTv;

    public NotifyViewHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onCreateView(View itemView) {
      packageTv = itemView.findViewById(R.id.tv_package);
      contentTv = itemView.findViewById(R.id.tv_content);
      sourceTv = itemView.findViewById(R.id.tv_source);
    }

    @Override
    protected void onBindData(NotifyContent data, RecyclerViewHolder holder) {
      packageTv.setText(data.mPackageName + "(" + data.mAppName + ")");
      contentTv.setText(data.mSoruce);
      sourceTv.setText(data.mTitle + "\n" + data.mContent);
    }
  }
}
