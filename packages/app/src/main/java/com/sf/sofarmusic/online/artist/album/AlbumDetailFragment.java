package com.sf.sofarmusic.online.artist.album;

import android.os.Bundle;
import android.view.View;
import com.sf.base.mvp.Presenter;
import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.base.util.eventbus.BindEventBus;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.online.artist.model.AlbumDetailPageList;
import com.sf.sofarmusic.online.artist.model.AlbumDetailResponse;
import com.sf.sofarmusic.online.artist.presenter.AlbumHeadPresenter;
import com.sf.sofarmusic.online.artist.presenter.AlbumTitlePresenter;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.widget.recyclerview.RecyclerAdapter;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.Nullable;

@BindEventBus
public class AlbumDetailFragment extends RecyclerFragment<Song> {

  private Album album;

  private Presenter presenter = new Presenter();

  @Override
  protected RecyclerAdapter<Song> onCreateAdapter() {
    return new AlbumDetailAdapter();
  }

  @Override
  protected PageList<?, Song> onCreatePageList() {
    return new AlbumDetailPageList(album);
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.album_detail_fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    album = (Album) getArguments().getSerializable(AlbumDetailActivity.KEY_ALBUM);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    presenter.destroy();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    presenter.add(new AlbumTitlePresenter());
    presenter.add(new AlbumHeadPresenter());
    presenter.create(view);
  }

  @Override
  public void onFinishLoading(boolean firstPage, boolean cache) {
    super.onFinishLoading(firstPage, cache);
    if (getPageList().getPageResponse() instanceof AlbumDetailResponse) {
      album = ((AlbumDetailResponse) getPageList().getPageResponse()).album;
    }
    presenter.bind(album, this);
  }

  @Subscribe
  public void onSelectSongEvent(PlayEvent.SelectSongEvent event) {
    if (getOriginAdapter() instanceof AlbumDetailAdapter) {
      AlbumDetailAdapter adapter = (AlbumDetailAdapter) getOriginAdapter();
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
