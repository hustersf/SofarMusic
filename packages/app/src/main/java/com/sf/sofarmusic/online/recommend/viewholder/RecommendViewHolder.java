package com.sf.sofarmusic.online.recommend.viewholder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.recommend.FeedItemAdapter;
import com.sf.sofarmusic.online.recommend.model.FeedGroup;
import com.sf.utility.DensityUtil;
import com.sf.widget.recyclerview.RecyclerViewHolder;
import com.sf.widget.recyclerview.itemdecoration.LinearMarginItemDecoration;

public class RecommendViewHolder extends RecyclerViewHolder<FeedGroup> {

  TextView groupTv;
  RecyclerView recyclerView;

  FeedItemAdapter adapter;

  public RecommendViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    groupTv = itemView.findViewById(R.id.group_tv);
    recyclerView = itemView.findViewById(R.id.recommend_rv);
    adapter = new FeedItemAdapter();
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

    int sideSpace = DensityUtil.dp2px(getContext(), 16);
    int betweenSpace = DensityUtil.dp2px(getContext(), 5);
    LinearMarginItemDecoration itemDecoration =
        new LinearMarginItemDecoration(RecyclerView.HORIZONTAL, sideSpace, betweenSpace);
    recyclerView.addItemDecoration(itemDecoration);
  }

  @Override
  protected void onBindData(FeedGroup item) {
    groupTv.setText(item.title);
    adapter.setList(item.feeds);
    adapter.notifyDataSetChanged();
  }
}
