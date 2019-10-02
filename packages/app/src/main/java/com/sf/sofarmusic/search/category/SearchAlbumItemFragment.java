package com.sf.sofarmusic.search.category;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.online.artist.ArtistAlbumAdapter;
import com.sf.sofarmusic.search.category.model.SearchAlbumPageList;
import com.sf.sofarmusic.search.model.SearchAllInfo;
import com.sf.sofarmusic.search.model.SearchCategoryResponse;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * 专辑
 */
public class SearchAlbumItemFragment extends RecyclerFragment<Album> {

  private SearchCategoryResponse response;
  private String key;

  @Override
  protected RecyclerAdapter<Album> onCreateAdapter() {
    return new ArtistAlbumAdapter();
  }

  @Override
  protected PageList<?, Album> onCreatePageList() {
    return new SearchAlbumPageList(key, SearchAllInfo.TYPE_ALBUM);
  }

  public void setData(SearchCategoryResponse response) {
    this.response = response;
    if (response != null) {
      key = response.query;
    }
  }
}
