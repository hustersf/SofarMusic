package com.sf.sofarmusic.local;

import java.util.List;
import com.sf.base.recycler.LocalRecyclerFragment;
import com.sf.sofarmusic.local.model.AuthorItem;
import com.sf.sofarmusic.local.model.LocalSongDataHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * Created by sufan on 16/12/1.
 * 歌手
 */
public class AuthorFragment extends LocalRecyclerFragment<AuthorItem> {

  @Override
  protected RecyclerAdapter<AuthorItem> onCreateAdapter() {
    return new AuthorAdapter();
  }

  @Override
  protected List<AuthorItem> onCreateModelList() {
    List<AuthorItem> list =
        MusicLoader.getInstance().sortByAuthor(LocalSongDataHolder.getInstance().getAllSongs());
    return list;
  }
}
