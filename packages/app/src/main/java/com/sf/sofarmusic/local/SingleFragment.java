package com.sf.sofarmusic.local;

import android.view.View;
import java.util.ArrayList;
import java.util.List;
import com.sf.base.recycler.LocalRecyclerFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.local.model.LocalSongDataHolder;
import com.sf.sofarmusic.local.presenter.SingleHeaderPresenter;
import com.sf.sofarmusic.model.Song;
import com.sf.utility.ViewUtil;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * Created by sufan on 16/12/1.
 * 单曲
 */
public class SingleFragment extends LocalRecyclerFragment<Song> {

  @Override
  protected RecyclerAdapter<Song> onCreateAdapter() {
    return new SingleAdapter();
  }

  @Override
  protected List<Song> onCreateModelList() {
    return LocalSongDataHolder.getInstance().getSongs();
  }


  @Override
  protected List<View> onCreateHeaderViews() {
    List<View> list = new ArrayList<>();

    View view = ViewUtil.inflate(mRecyclerView, R.layout.rank_detail_list_head);
    SingleHeaderPresenter presenter = new SingleHeaderPresenter(this);
    presenter.create(view);

    list.add(view);
    return list;
  }

  public void playAll() {
    if (getOriginAdapter() instanceof SingleAdapter) {
      ((SingleAdapter) getOriginAdapter()).selectSong(0);
    }
  }
}
