package com.sf.sofarmusic.local;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import com.sf.base.recycler.LocalRecyclerFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.local.model.LocalSongDataHolder;
import com.sf.sofarmusic.local.presenter.SingleHeaderPresenter;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.play.core.PlayDataHolder;
import com.sf.sofarmusic.play.core.PlayEvent;
import com.sf.utility.ViewUtil;
import com.sf.widget.recyclerview.RecyclerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by sufan on 16/12/1.
 * 单曲
 */
public class SingleFragment extends LocalRecyclerFragment<Song> {

  private SingleHeaderPresenter headerPresenter;

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    EventBus.getDefault().register(this);
  }

  @Override
  protected RecyclerAdapter<Song> onCreateAdapter() {
    return new SingleAdapter();
  }

  @Override
  protected List<Song> onCreateModelList() {
    List<Song> songs;
    if (getActivity() instanceof LocalDetailActivity) {
      songs = LocalSongDataHolder.getInstance().getSelectSongs();
    } else {
      songs = LocalSongDataHolder.getInstance().getAllSongs();
    }
    if (headerPresenter != null) {
      headerPresenter.bind(songs, this);
    }
    return songs;
  }


  @Override
  protected List<View> onCreateHeaderViews() {
    List<View> list = new ArrayList<>();

    View view = ViewUtil.inflate(mRecyclerView, R.layout.rank_detail_list_head);
    headerPresenter = new SingleHeaderPresenter(this);
    headerPresenter.create(view);

    list.add(view);
    return list;
  }

  public void playAll() {
    if (getOriginAdapter() instanceof SingleAdapter) {
      ((SingleAdapter) getOriginAdapter()).selectSong(0);
      PlayDataHolder.getInstance().setSongs(getOriginAdapter().getList());
    }
  }

  @Subscribe
  public void onSelectSongEvent(PlayEvent.SelectSongEvent event) {
    if (getOriginAdapter() instanceof SingleAdapter) {
      SingleAdapter adapter = (SingleAdapter) getOriginAdapter();
      for (int i = 0; i < adapter.getList().size(); i++) {
        if (adapter.getList().get(i).songId.equals(event.song.songId)) {
          adapter.selectSong(i);
          getRecyclerView().getLayoutManager().scrollToPosition(i + 1);
        }
      }
    }
  }
}
