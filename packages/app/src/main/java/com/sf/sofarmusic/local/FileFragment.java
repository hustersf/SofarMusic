package com.sf.sofarmusic.local;

import java.util.List;
import com.sf.base.recycler.LocalRecyclerFragment;
import com.sf.sofarmusic.local.model.FileItem;
import com.sf.sofarmusic.local.model.LocalSongDataHolder;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * Created by sufan on 16/12/1.
 * 文件夹
 */
public class FileFragment extends LocalRecyclerFragment<FileItem> {

  @Override
  protected RecyclerAdapter<FileItem> onCreateAdapter() {
    return new FileAdapter();
  }

  @Override
  protected List<FileItem> onCreateModelList() {
    List<FileItem> list =
        MusicLoader.getInstance().sortByFile(LocalSongDataHolder.getInstance().getAllSongs());
    return list;
  }
}
