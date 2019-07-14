package com.sf.sofarmusic.local;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.local.model.AuthorItem;
import com.sf.sofarmusic.local.model.LocalSongDataHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

/**
 * Created by sufan on 16/11/23.
 */
public class AuthorAdapter extends RecyclerAdapter<AuthorItem> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.adapter_artist_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new AuthorItemHolder(itemView);
  }


  class AuthorItemHolder extends RecyclerViewHolder<AuthorItem> {

    ImageView authorIv;
    TextView authorNameTv, authorCountTv, dotTv, voiceTv;

    public AuthorItemHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onCreateView(View itemView) {
      authorIv = itemView.findViewById(R.id.author_iv);
      authorNameTv = itemView.findViewById(R.id.author_name_tv);
      authorCountTv = itemView.findViewById(R.id.author_count_tv);

      dotTv = itemView.findViewById(R.id.dot_tv);
      voiceTv = itemView.findViewById(R.id.voice_tv);
    }

    @Override
    protected void onBindData(AuthorItem item) {
      Glide.with(context).load(item.extraLargeUrl)
          .placeholder(R.drawable.placeholder_disk_210)
          .error(R.drawable.placeholder_disk_210)
          .into(authorIv);

      authorNameTv.setText(item.name);
      authorCountTv.setText(item.songs.size() + "首");

      if (item.selected) {
        voiceTv.setVisibility(View.VISIBLE);
        dotTv.setVisibility(View.GONE);
      } else {
        voiceTv.setVisibility(View.GONE);
        dotTv.setVisibility(View.VISIBLE);
      }

      itemView.setOnClickListener(v -> {
        LocalSongDataHolder.getInstance().setSelectSongs(item.songs);
        LocalDetailActivity.launch(getContext(), item.name);
      });
    }
  }

}
