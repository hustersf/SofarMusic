package com.sf.sofarmusic.online.artist.presenter;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sf.base.mvp.Presenter;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.online.artist.ArtistGroupHeaderAdapter;
import com.sf.utility.DensityUtil;
import com.sf.widget.recyclerview.itemdecoration.GridDividerItemDecoration;

import java.util.List;

public class ArtistGroupHeaderPresenter extends Presenter<List<Artist>> {

  RecyclerView artistHotRv;
  ArtistGroupHeaderAdapter adapter;

  @Override
  protected void onCreate() {
    super.onCreate();
    artistHotRv = getView().findViewById(R.id.artist_hot_rv);
    adapter = new ArtistGroupHeaderAdapter();
    artistHotRv.setAdapter(adapter);
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
    artistHotRv.setLayoutManager(gridLayoutManager);

    int space = DensityUtil.dp2px(getContext(), 10);
    int color = Color.parseColor("#FFFFFF");
    GridDividerItemDecoration itemDecoration = new GridDividerItemDecoration(space, color);
    artistHotRv.addItemDecoration(itemDecoration);
  }

  @Override
  protected void onBind(List<Artist> model, Object callerContext) {
    super.onBind(model, callerContext);
    adapter.setList(model);
    adapter.notifyDataSetChanged();
  }
}
