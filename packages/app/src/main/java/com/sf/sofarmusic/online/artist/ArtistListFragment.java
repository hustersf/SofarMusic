package com.sf.sofarmusic.online.artist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sf.base.network.page.PageList;
import com.sf.base.recycler.RecyclerFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.online.artist.model.ArtistGroup;
import com.sf.sofarmusic.online.artist.model.ArtistListPageList;
import com.sf.sofarmusic.online.artist.presenter.ArtistListHeaderPresenter;
import com.sf.widget.recyclerview.RecyclerAdapter;

public class ArtistListFragment extends RecyclerFragment<Artist> {

  ArtistGroup artistGroup;
  ArtistListHeaderPresenter headerPresenter = new ArtistListHeaderPresenter();

  @Override
  protected int getLayoutResId() {
    return R.layout.artist_list_fragment;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    headerPresenter.destroy();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    artistGroup = (ArtistGroup) getArguments().getSerializable(ArtistListActivity.KEY_ARTIST_GROUP);
    super.onViewCreated(view, savedInstanceState);
    headerPresenter.create(view);
    headerPresenter.bind(artistGroup, this);
  }

  @Override
  protected RecyclerAdapter<Artist> onCreateAdapter() {
    return new ArtistListAdapter();
  }

  @Override
  protected PageList<?, Artist> onCreatePageList() {
    return new ArtistListPageList(artistGroup);
  }
}
