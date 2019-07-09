package com.sf.demo.list.drama;

import android.view.View;

import com.sf.demo.R;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class DramaBoardAdapter extends RecyclerAdapter<DramaInfo> {
  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.adapter_drama_board;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new DramaBoardViewHolder(itemView);
  }
}
