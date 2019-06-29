package com.sf.sofarmusic.local;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.local.model.AlbumItem;
import com.sf.widget.recyclerview.RecyclerAdapter;
import com.sf.widget.recyclerview.RecyclerViewHolder;

/**
 * Created by sufan on 16/11/23.
 */

public class AlbumAdapter extends RecyclerAdapter<AlbumItem> {

  @Override
  protected int getItemLayoutId(int viewType) {
    return R.layout.adapter_album_item;
  }

  @Override
  protected RecyclerViewHolder onCreateViewHolder(int viewType, View itemView) {
    return new AlbumItemHolder(itemView);
  }

  class AlbumItemHolder extends RecyclerViewHolder<AlbumItem> {

    ImageView albumIv;
    TextView albumNameTv, albumCountTv, albumAuthorTv, dotTv, voiceTv;

    public AlbumItemHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onCreateView(View itemView) {
      albumIv = itemView.findViewById(R.id.album_iv);
      albumNameTv = itemView.findViewById(R.id.album_name_tv);
      albumCountTv = itemView.findViewById(R.id.album_count_tv);
      albumAuthorTv = itemView.findViewById(R.id.album_artist_tv);
      dotTv = itemView.findViewById(R.id.dot_tv);
      voiceTv = itemView.findViewById(R.id.voice_tv);
    }

    @Override
    protected void onBindData(AlbumItem item, RecyclerViewHolder holder) {
        Glide.with(context).load(item.imgUri).error(R.drawable.placeholder_disk_210)
                .into(albumIv);

        albumNameTv.setText(item.albumName);
       albumAuthorTv.setText(item.authorName);
       albumCountTv.setText(item.songs.size() + "首");


        if (item.selected) {
            voiceTv.setVisibility(View.VISIBLE);
            dotTv.setVisibility(View.GONE);
        } else {
            voiceTv.setVisibility(View.GONE);
            dotTv.setVisibility(View.VISIBLE);
        }

        itemView.setOnClickListener(v -> {
        });
    }
  }



}
