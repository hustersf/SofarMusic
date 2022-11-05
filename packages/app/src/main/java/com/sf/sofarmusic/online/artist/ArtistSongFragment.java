package com.sf.sofarmusic.online.artist;

import org.greenrobot.eventbus.Subscribe;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.base.util.eventbus.BindEventBus;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.online.artist.model.ArtistSongPageList;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.widget.recyclerview.RecyclerAdapter;

@BindEventBus
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

  @Subscribe
  public void onSelectSongEvent(PlayEvent.SelectSongEvent event) {
    if (getOriginAdapter() instanceof ArtistSongAdapter) {
      ArtistSongAdapter adapter = (ArtistSongAdapter) getOriginAdapter();
      for (int i = 0; i < adapter.getList().size(); i++) {
        if (adapter.getList().get(i).songId.equals(event.song.songId)) {
          adapter.selectSong(i);

          //这里可能由于AppbarLayout嵌套导致滑动的位置不对
          getRecyclerView().getLayoutManager().scrollToPosition(i);
          break;
        }
      }
    }
  }

}
