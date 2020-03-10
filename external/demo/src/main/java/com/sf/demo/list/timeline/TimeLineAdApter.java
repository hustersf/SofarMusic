package com.sf.demo.list.timeline;

import android.view.View;
import android.widget.TextView;

import com.sf.demo.R;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class TimeLineAdApter extends RecyclerAdapter<TimeItem> {


  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.adapter_time_line_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new TimeLineViewHolder(itemView);
  }

  class TimeLineViewHolder extends RecyclerViewHolder<TimeItem> {

    TextView timeTv;
    TextView contentTv;

    public TimeLineViewHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onCreateView(View itemView) {
      timeTv = itemView.findViewById(R.id.time_tv);
      contentTv = itemView.findViewById(R.id.content_tv);
    }

    @Override
    protected void onBindData(TimeItem item) {
      timeTv.setText(item.time);
      contentTv.setText(item.content);
    }
  }
}
