package com.sf.sofarmusic.online.artist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.base.recycler.RecyclerViewTipHelper;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.online.artist.model.ArtistAlbumPageList;
import com.sf.widget.recyclerview.RecyclerAdapter;

public class ArtistAlbumFragment extends RecyclerFragment<Album> {

  private Artist artist;

  @Override
  protected RecyclerAdapter<Album> onCreateAdapter() {
    return new ArtistAlbumAdapter();
  }

  @Override
  protected PageList<?, Album> onCreatePageList() {
    return new ArtistAlbumPageList(artist);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    artist = (Artist) getArguments().getSerializable(ArtistActivity.KEY_ARTIST);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (getTipHelper() instanceof RecyclerViewTipHelper) {
      ((RecyclerViewTipHelper) getTipHelper()).setHasNoMoreTip(true);
    }
  }
}
