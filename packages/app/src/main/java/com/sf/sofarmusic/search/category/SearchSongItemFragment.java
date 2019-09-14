package com.sf.sofarmusic.search.category;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.model.Song;
import com.sf.sofarmusic.online.artist.ArtistSongAdapter;
import com.sf.sofarmusic.search.category.model.SearchSongPageList;
import com.sf.sofarmusic.search.model.SearchAllInfo;
import com.sf.sofarmusic.search.model.SearchCategoryResponse;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * 单曲
 */
public class SearchSongItemFragment extends RecyclerFragment<Song> {

  private SearchCategoryResponse response;
  private String key;

  @Override
  protected RecyclerAdapter<Song> onCreateAdapter() {
    return new ArtistSongAdapter();
  }

  @Override
  protected PageList<?, Song> onCreatePageList() {
    return new SearchSongPageList(key, SearchAllInfo.TYPE_SONG);
  }

  public void setData(SearchCategoryResponse response) {
    this.response = response;
    if (response != null) {
      key = response.query;
    }
  }

}
