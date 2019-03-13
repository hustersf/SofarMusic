package com.sf.sofarmusic.online.rank;

import android.widget.TextView;
import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.rank.model.Rank;
import com.sf.utility.LogUtil;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;


public class RankAdapter extends RecyclerAdapter<Rank> {

  private SofarImageView rankIv;
  private TextView firstTv;
  private TextView secondTv;
  private TextView thirdTv;

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.adapter_rank_list;
  }

  @Override
  protected void onCreateView(RecyclerViewHolder holder) {
    rankIv = holder.getView(R.id.rank_iv);
    firstTv = holder.getView(R.id.first_tv);
    secondTv = holder.getView(R.id.second_tv);
    thirdTv = holder.getView(R.id.third_tv);
  }

  @Override
  protected void onBindData(Rank data, RecyclerViewHolder holder) {
    rankIv.bindUrl(data.squareThumbUrl);
    final String title0 = data.songs.get(0).title;
    final String title1 = data.songs.get(1).title;
    final String title2 = data.songs.get(2).title;

    final String author0 = data.songs.get(0).author;
    final String author1 = data.songs.get(1).author;
    final String author2 = data.songs.get(2).author;
    firstTv.setText("1." + title0 + "-" + author0);
    secondTv.setText("2." + title1 + "-" + author1);
    thirdTv.setText("3." + title2 + "-" + author2);

    LogUtil.d("RankAdapter", holder.getAdapterPosition() + " " + data.songs.get(0).title + "-"
        + data.songs.get(0).author);
  }

}
