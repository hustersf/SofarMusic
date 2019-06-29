package com.sf.sofarmusic.local;

import java.util.List;
import com.sf.base.recycler.LocalRecyclerFragment;
import com.sf.sofarmusic.local.model.AlbumItem;
import com.sf.sofarmusic.local.model.LocalSongDataHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * Created by sufan on 16/12/1.
 * 专辑
 */
public class AlbumFragment extends LocalRecyclerFragment<AlbumItem> {


  @Override
  protected RecyclerAdapter<AlbumItem> onCreateAdapter() {
    return new AlbumAdapter();
  }

  @Override
  protected List<AlbumItem> onCreateModelList() {
    List<AlbumItem> list =
        MusicLoader.getInstance().sortByAlbum(LocalSongDataHolder.getInstance().getSongs());
    return list;
  }
}
