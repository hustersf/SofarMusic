package com.sf.sofarmusic.online.artist.model;

import com.sf.base.network.retrofit.page.SofarRetrofitPageList;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.model.Album;
import com.sf.sofarmusic.model.Song;

import io.reactivex.Observable;

public class AlbumDetailPageList extends SofarRetrofitPageList<AlbumDetailResponse, Song> {

  private Album album;

  public AlbumDetailPageList(Album album) {
    this.album = album;
  }

  @Override
  protected Observable<AlbumDetailResponse> onCreateRequest() {
    return ApiProvider.getMusicApiService().albumSongList(album.albumId);
  }
}
