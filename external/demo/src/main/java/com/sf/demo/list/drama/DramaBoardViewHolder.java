package com.sf.demo.list.drama;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class DramaBoardViewHolder extends RecyclerViewHolder {

  public DramaBoardViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    Context context = itemView.getContext();
    ViewGroup.LayoutParams rootLayoutParams = itemView.getLayoutParams();
    rootLayoutParams.width =
        DeviceUtil.getMetricsWidth(context) - DensityUtil.dp2px(context, 16 + 30);
    itemView.setLayoutParams(rootLayoutParams);
  }

  @Override
  protected void onBindData(Object data, RecyclerViewHolder holder) {

  }
}
