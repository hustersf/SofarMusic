package com.sf.sofarmusic.search.category;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.online.artist.ArtistListAdapter;
import com.sf.sofarmusic.search.category.model.SearchArtistPageList;
import com.sf.sofarmusic.search.model.SearchAllInfo;
import com.sf.sofarmusic.search.model.SearchCategoryResponse;
import com.sf.widget.recyclerview.RecyclerAdapter;

/**
 * 歌手
 */
public class SearchArtistItemFragment extends RecyclerFragment<Artist> {

  private SearchCategoryResponse response;
  private String key;

  @Override
  protected RecyclerAdapter<Artist> onCreateAdapter() {
    return new ArtistListAdapter();
  }

  @Override
  protected PageList<?, Artist> onCreatePageList() {
    return new SearchArtistPageList(key, SearchAllInfo.TYPE_ARTIST);
  }

  public void setData(SearchCategoryResponse response) {
    this.response = response;
    if (response != null) {
      key = response.query;
    }
  }
}
