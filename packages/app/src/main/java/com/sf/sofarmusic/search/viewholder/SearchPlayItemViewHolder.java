package com.sf.sofarmusic.search.viewholder;

import android.view.View;
import android.widget.TextView;
import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.PlayInfo;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class SearchPlayItemViewHolder extends RecyclerViewHolder<PlayInfo> {

  SofarImageView coverIv;
  TextView nameTv;
  TextView tagTv;
  SofarImageView authorCoverIv;
  TextView authorNameTv;
  TextView songCountTv;


  public SearchPlayItemViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    coverIv = itemView.findViewWithTag(R.id.cover);
    nameTv = itemView.findViewWithTag(R.id.name);
    tagTv = itemView.findViewWithTag(R.id.tag);
    authorCoverIv = itemView.findViewWithTag(R.id.author_cover);
    authorNameTv = itemView.findViewWithTag(R.id.author_name);
    songCountTv = itemView.findViewWithTag(R.id.song_count);
  }

  @Override
  protected void onBindData(PlayInfo item) {
    coverIv.bindUrl(item.thumbUrl);
    nameTv.setText(item.title);
    tagTv.setText(item.styleTag);
    if (item.userInfo != null) {
      authorCoverIv.bindUrl(item.userInfo.thumbSmallUrl);
      authorNameTv.setText(item.userInfo.name);
    }
    songCountTv.setText(item.songNum + "首单曲");
  }
}
