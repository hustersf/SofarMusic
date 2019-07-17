package com.sf.sofarmusic.online.artist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.model.MV;
import com.sf.sofarmusic.online.artist.model.ArtistMVPageList;
import com.sf.widget.recyclerview.RecyclerAdapter;

public class ArtistMVFragment extends RecyclerFragment<MV> {

  private Artist artist;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    artist = (Artist) getArguments().getSerializable(ArtistActivity.KEY_ARTIST);
  }

  @Override
  protected RecyclerAdapter<MV> onCreateAdapter() {
    return new ArtistMVAdapter();
  }

  @Override
  protected PageList<?, MV> onCreatePageList() {
    return new ArtistMVPageList(artist);
  }
}
