package com.sf.sofarmusic.online.rank;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.online.rank.model.Rank;
import com.sf.utility.LogUtil;
import com.sf.utility.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RankAdapter2 extends RecyclerView.Adapter<RankAdapter2.RankViewHolder> {

  protected List<Rank> mDatas;

  public RankAdapter2(){
    mDatas=new ArrayList<>();
  }

  public void setList(List<Rank> datas) {
    if (mDatas == null) {
      mDatas = new ArrayList<>();
    }
    mDatas.clear();
    mDatas.addAll(datas);
  }

  public List<Rank> getList(){
    return mDatas;
  }

  @Override
  public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = ViewUtil.inflate(parent, R.layout.adapter_rank_list, false);
    return new RankViewHolder(view);
  }

  @Override
  public void onBindViewHolder(RankViewHolder holder, int position) {
    Rank data = mDatas.get(holder.getAdapterPosition());
    holder.rankIv.bindUrl(data.squareThumbUrl);
    final String title0 = data.songs.get(0).name;
    final String title1 = data.songs.get(1).name;
    final String title2 = data.songs.get(2).name;

    final String author0 = data.songs.get(0).author;
    final String author1 = data.songs.get(1).author;
    final String author2 = data.songs.get(2).author;
    holder.firstTv.setText("1." + title0 + "-" + author0);
    holder.secondTv.setText("2." + title1 + "-" + author1);
    holder.thirdTv.setText("3." + title2 + "-" + author2);

    LogUtil.d("RankAdapter2", holder.getAdapterPosition() + " " + data.songs.get(0).name + "-"
            + data.songs.get(0).author);
  }

  @Override
  public int getItemCount() {
    return mDatas.size();
  }

  class RankViewHolder extends RecyclerView.ViewHolder {

    private SofarImageView rankIv;
    private TextView firstTv;
    private TextView secondTv;
    private TextView thirdTv;

    public RankViewHolder(View itemView) {
      super(itemView);
      rankIv = itemView.findViewById(R.id.rank_iv);
      firstTv = itemView.findViewById(R.id.first_tv);
      secondTv = itemView.findViewById(R.id.second_tv);
      thirdTv = itemView.findViewById(R.id.third_tv);
    }
  }
}
