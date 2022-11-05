package com.sf.sofarmusic.online.artist;

import android.os.Bundle;
import android.view.View;

import com.sf.base.recycler.LocalRecyclerFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.model.Artist;
import com.sf.sofarmusic.online.artist.model.ArtistGroup;
import com.sf.sofarmusic.online.artist.presenter.ArtistGroupHeaderPresenter;
import com.sf.utility.ViewUtil;
import com.sf.widget.recyclerview.RecyclerAdapter;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class ArtistGroupFragment extends LocalRecyclerFragment<ArtistGroup> {

  ArtistGroupHeaderPresenter headerPresenter = new ArtistGroupHeaderPresenter();

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    view.setBackgroundColor(getResources().getColor(R.color.white));
    fetchHotArtist();
  }

  @Override
  protected List<View> onCreateHeaderViews() {
    List<View> views = new ArrayList<>();
    View view = ViewUtil.inflate(getRecyclerView(), R.layout.artist_group_header);
    headerPresenter.create(view);
    views.add(view);
    return views;
  }

  @Override
  protected RecyclerAdapter<ArtistGroup> onCreateAdapter() {
    return new ArtistGroupAdApter();
  }

  @Override
  protected List<ArtistGroup> onCreateModelList() {
    List<ArtistGroup> artistGroups = new ArrayList<>();

    // 华语
    for (int i = 1; i <= 3; i++) {
      ArtistGroup group = new ArtistGroup();
      group.area = Artist.AREA_CHINA;
      group.sex = i;
      String key;
      if (i == 1) {
        key = "男歌手";
      } else if (i == 2) {
        key = "女歌手";
      } else {
        key = "组合";
      }
      group.name = "华语" + key;
      artistGroups.add(group);
    }

    // 欧美
    for (int i = 1; i <= 3; i++) {
      ArtistGroup group = new ArtistGroup();
      group.area = Artist.AREA_WEST;
      group.sex = i;
      String key;
      if (i == 1) {
        key = "男歌手";
      } else if (i == 2) {
        key = "女歌手";
      } else {
        key = "组合";
      }
      group.name = "欧美" + key;
      artistGroups.add(group);
    }

    // 韩国
    for (int i = 1; i <= 3; i++) {
      ArtistGroup group = new ArtistGroup();
      group.area = Artist.AREA_KOREA;
      group.sex = i;
      String key;
      if (i == 1) {
        key = "男歌手";
      } else if (i == 2) {
        key = "女歌手";
      } else {
        key = "组合";
      }
      group.name = "韩国" + key;
      artistGroups.add(group);
    }

    // 日本
    for (int i = 1; i <= 3; i++) {
      ArtistGroup group = new ArtistGroup();
      group.area = Artist.AREA_JAPAN;
      group.sex = i;
      String key;
      if (i == 1) {
        key = "男歌手";
      } else if (i == 2) {
        key = "女歌手";
      } else {
        key = "组合";
      }
      group.name = "日本" + key;
      artistGroups.add(group);
    }

    ArtistGroup group = new ArtistGroup();
    group.area = Artist.AREA_OTHER;
    group.sex = 0;
    group.name = "其他歌手";
    artistGroups.add(group);

    return artistGroups;
  }

  private void fetchHotArtist() {
    ApiProvider.getMusicApiService().artistList(Artist.AREA_ALL, Artist.SEX_ALL, 0, 48)
        .subscribe(artistResponse -> {
          headerPresenter.bind(artistResponse.artists, this);
        });
  }
}
