package com.sf.sofarmusic.online.artist.viewholder;

import android.view.View;
import android.widget.TextView;
import com.sf.base.widget.SofarImageView;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.MV;
import com.sf.sofarmusic.online.artist.mv.MVDetailActivity;
import com.sf.widget.recyclerview.RecyclerViewHolder;

public class ArtistMVViewHolder extends RecyclerViewHolder<MV> {

  SofarImageView cover;
  TextView nameTv;


  public ArtistMVViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  protected void onCreateView(View itemView) {
    cover = itemView.findViewById(R.id.cover);
    nameTv = itemView.findViewById(R.id.name_tv);
  }

  @Override
  protected void onBindData(MV item) {
    cover.bindUrl(item.thumbUrl);
    nameTv.setText(item.name);

    itemView.setOnClickListener(v -> {
      MVDetailActivity.launch(getContext(), item);
    });
  }
}
