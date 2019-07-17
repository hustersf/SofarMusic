package com.sf.sofarmusic.online.artist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.online.artist.model.ArtistSongPageList;
import com.sf.widget.recyclerview.RecyclerAdapter;

public class ArtistSongFragment extends RecyclerFragment<Song> {

  private Artist artist;

  @Override
  protected RecyclerAdapter<Song> onCreateAdapter() {
    return new ArtistSongAdapter();
  }

  @Override
  protected PageList<?, Song> onCreatePageList() {
    return new ArtistSongPageList(artist);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    artist = (Artist) getArguments().getSerializable(ArtistActivity.KEY_ARTIST);
  }

}
