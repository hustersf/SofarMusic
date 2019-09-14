package com.sf.sofarmusic.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.sf.base.viewpager.TabFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.search.category.SearchAlbumItemFragment;
import com.sf.sofarmusic.search.category.SearchAllItemFragment;
import com.sf.sofarmusic.search.category.SearchArtistItemFragment;
import com.sf.sofarmusic.search.category.SearchPlayItemFragment;
import com.sf.sofarmusic.search.category.SearchSongItemFragment;
import com.sf.sofarmusic.search.model.SearchAllInfo;
import com.sf.sofarmusic.search.model.SearchCategoryResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 点击搜索键之后的分类结果
 */
public class SearchCategoryTabFragment extends TabFragment {

  private String[] titles = {"歌单", "单曲", "专辑", "歌手"};

  private String key;
  private SearchCategoryResponse response;
  private boolean created;

  private View synLayout;
  private TextView synWordTv;

  @Override
  protected int getLayoutResId() {
    return R.layout.search_category_tab_fragment;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    created = true;
    initView();
    if (!TextUtils.isEmpty(key)) {
      fetchSearchAll();
    }
  }

  private void initView() {
    synLayout = getView().findViewById(R.id.syn_layout);
    synWordTv = getView().findViewById(R.id.syn_words);
    dynamicAddView(tabLayout, "tabLayoutIndicator", R.color.themeColor);
    dynamicAddView(tabLayout, "tabLayoutTextColor", R.color.main_text_color);
  }

  @Override
  protected List<TabLayout.Tab> getTabs() {
    if (response == null) {
      return null;
    }

    List<TabLayout.Tab> tabs = new ArrayList<>();
    for (int i = 0; i < titles.length; i++) {
      TabLayout.Tab tab = tabLayout.newTab().setText(titles[i]);
      tabs.add(tab);
    }
    return tabs;
  }

  @Override
  protected List<Fragment> getTabFragments() {
    if (response == null) {
      return null;
    }

    List<Fragment> fragments = new ArrayList<>();
    // 全部
    // SearchAllItemFragment allFragment = new SearchAllItemFragment();
    // allFragment.setData(response);
    // fragments.add(allFragment);

    // 歌单
    SearchPlayItemFragment playFragment = new SearchPlayItemFragment();
    playFragment.setData(response);
    fragments.add(playFragment);

    // 单曲
    SearchSongItemFragment songFragment = new SearchSongItemFragment();
    songFragment.setData(response);
    fragments.add(songFragment);

    // 专辑
    SearchAlbumItemFragment albumFragment = new SearchAlbumItemFragment();
    albumFragment.setData(response);
    fragments.add(albumFragment);

    // 歌手
    SearchArtistItemFragment artistFragment = new SearchArtistItemFragment();
    artistFragment.setData(response);
    fragments.add(artistFragment);
    return fragments;
  }

  public void switchWord(String word) {
    key = word;
    if (created) {
      fetchSearchAll();
    }
  }

  private void fetchSearchAll() {
    ApiProvider.getMusicApiService().searchAll(key, 1, 10, SearchAllInfo.TYPE_NONE)
        .map(resultResponse -> resultResponse.result)
        .compose(this.bindToLifecycle())
        .subscribe(response -> {
          this.response = response;
          updateTabs();
          updateSynWords();
        }, throwable -> {});
  }

  private void updateSynWords() {
    if (response != null) {
      if (TextUtils.isEmpty(response.synWords)) {
        synLayout.setVisibility(View.GONE);
      } else {
        synLayout.setVisibility(View.VISIBLE);
        synWordTv.setText(response.synWords);
      }
    }
  }
}
