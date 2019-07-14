package com.sf.sofarmusic.online.rank;

import android.view.View;
import android.widget.TextView;
import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.rank.detail.RankDetailActivity;
import com.sf.sofarmusic.online.rank.model.Rank;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;


public class RankAdapter extends RecyclerAdapter<Rank> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.adapter_rank_list;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new RankViewHolder(itemView);
  }

  class RankViewHolder extends RecyclerViewHolder<Rank> {

    private SofarImageView rankIv;
    private TextView firstTv;
    private TextView secondTv;
    private TextView thirdTv;

    public RankViewHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onCreateView(View itemView) {
      rankIv = itemView.findViewById(R.id.rank_iv);
      firstTv = itemView.findViewById(R.id.first_tv);
      secondTv = itemView.findViewById(R.id.second_tv);
      thirdTv = itemView.findViewById(R.id.third_tv);
    }

    @Override
    protected void onBindData(Rank data) {
      rankIv.bindUrl(data.squareThumbUrl);
      firstTv.setText("1." + data.songs.get(0).name + "-" + data.songs.get(0).author);
      secondTv.setText("2." + data.songs.get(1).name + "-" + data.songs.get(1).author);
      thirdTv.setText("3." + data.songs.get(2).name + "-" + data.songs.get(2).author);

      itemView.setOnClickListener(v -> {
        RankDetailActivity.launch(context, data.type);
      });
    }
  }

}
