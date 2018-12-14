package com.sf.sofarmusic.local;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.base.LazyLoadBaseFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.base.Constant;
import com.sf.sofarmusic.enity.ArtistItem;
import com.sf.utility.CollectionUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by sufan on 16/12/1.
 * 歌手
 */

public class ArtistFragment extends LazyLoadBaseFragment
    implements
      ArtistAdapter.OnItemClickListener {


  private View view;

  private RecyclerView artist_rv;
  private List<ArtistItem> mArtistList;
  private ArtistAdapter mAdapter;

  private int mIndex = 0; // 获取歌手url的次数

  private static final int REQUEST_CODE = 100; // >0的整数即可

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_local_artist, container, false);
    return view;
  }


  @Override
  protected void initData() {
    mArtistList = MusicLoader.getInstance().getLocalArtistList(Constant.sLocalList);
    mAdapter = new ArtistAdapter(activity, mArtistList);
    artist_rv.setAdapter(mAdapter);
    mAdapter.setOnItemClickListener(this);
    getArtistUrl();
  }


  // 通过酷狗接口获取歌手图片的url
  private void getArtistUrl() {
    for (int i = 0; i < mArtistList.size(); i++) {
      final ArtistItem item = mArtistList.get(i);
      String baseUrl =
          "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&api_key=fdb3a51437d4281d4d64964d333531d4&format=json";
      ApiProvider.getMusicApiService().getArtistInfo(baseUrl, item.name)
          .compose(this.bindToLifecycle())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(artistResponse -> {
            if (artistResponse.mArtist != null) {
              if (!CollectionUtil.isEmpty(artistResponse.mArtist.mArtistImages)
                  && artistResponse.mArtist.mArtistImages.size() > 3) {
                item.mediumUrl = artistResponse.mArtist.mArtistImages.get(1).mImageUrl;
                item.extraLargeUrl = artistResponse.mArtist.mArtistImages.get(3).mImageUrl;
              }
            }
            mIndex++;
            if (mIndex == mArtistList.size()) {
              mAdapter = new ArtistAdapter(activity, mArtistList);
              artist_rv.setAdapter(mAdapter);
            }

          }, throwable -> {

      });
    }
  }


  @Override
  protected void initView() {
    artist_rv = (RecyclerView) view.findViewById(R.id.artist_rv);
    artist_rv.setLayoutManager(new LinearLayoutManager(activity));

  }

  @Override
  protected void initEvent() {

  }

  @Override
  public void OnArtistItem(int position) {
    ArtistItem item = mArtistList.get(position);

    Intent intent = new Intent(activity, ShowDetailActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    Bundle bundle = new Bundle();
    bundle.putString("title", item.name);
    // bundle.putSerializable("list",playList);
    Constant.sPreList = item.artistList; //
    intent.putExtras(bundle);

    startActivityForResult(intent, REQUEST_CODE);

  }

  public void refreshData() {
    if (isInit) {
      initData();
    }
  }
}
