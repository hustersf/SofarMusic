package com.sf.sofarmusic.online.artist;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.sf.base.mvp.Presenter;
import com.sf.base.viewpager.TabFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.online.artist.presenter.ArtistHeadPresenter;
import com.sf.sofarmusic.online.artist.presenter.ArtistTitlePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ArtistFragment extends TabFragment {

  private String[] titles = {"热门单曲", "全部专辑", "MV"};

  private ArtistSongFragment songFragment;
  private ArtistAlbumFragment albumFragment;
  private ArtistMVFragment mvFragment;

  private Artist artist;
  private Presenter presenter = new Presenter();

  @Override
  protected int getLayoutResId() {
    return R.layout.artist_person_fragment;
  }

  @Override
  protected List<TabLayout.Tab> getTabs() {
    List<TabLayout.Tab> tabs = new ArrayList<>();
    for (int i = 0; i < titles.length; i++) {
      TabLayout.Tab tab = tabLayout.newTab().setText(titles[i]);
      tabs.add(tab);
    }
    return tabs;
  }

  @Override
  protected List<Fragment> getTabFragments() {
    songFragment = new ArtistSongFragment();
    albumFragment = new ArtistAlbumFragment();
    mvFragment = new ArtistMVFragment();

    songFragment.setArguments(getArguments());
    albumFragment.setArguments(getArguments());
    mvFragment.setArguments(getArguments());
    List<Fragment> fragments = new ArrayList<>();
    fragments.add(songFragment);
    fragments.add(albumFragment);
    fragments.add(mvFragment);
    return fragments;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    presenter.destroy();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    dynamicAddView(tabLayout, "tabLayoutIndicator", R.color.themeColor);
    dynamicAddView(tabLayout, "tabLayoutTextColor", R.color.main_text_color);

    artist = (Artist) getArguments().getSerializable(ArtistActivity.KEY_ARTIST);
    presenter.add(new ArtistHeadPresenter());
    presenter.add(new ArtistTitlePresenter());
    presenter.create(view);
    presenter.bind(artist, this);
  }

  @Override
  protected int getOffscreenPageLimit() {
    return 3;
  }
}
