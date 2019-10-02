package com.sf.sofarmusic.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sf.base.BaseFragment;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.db.search.SearchRecordManager;
import com.sf.sofarmusic.search.presenter.SearchHistoryPresenter;
import com.sf.sofarmusic.search.presenter.SearchHotPresenter;
import com.sf.utility.CollectionUtil;

/**
 * 搜索热门，历史
 */
public class SearchHotFragment extends BaseFragment {

  private SearchHistoryPresenter historyPresenter;
  private SearchHotPresenter hotPresenter;

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (historyPresenter != null) {
      historyPresenter.destroy();
    }
    if (hotPresenter != null) {
      hotPresenter.destroy();
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.search_hot_fragment, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // 历史
    if (historyPresenter == null) {
      historyPresenter = new SearchHistoryPresenter();
      historyPresenter.create(view);
    }
    fetchHistory();

    // 热门
    if (hotPresenter == null) {
      hotPresenter = new SearchHotPresenter();
      hotPresenter.create(view);
    }
    fetchHot();
  }

  private void fetchHistory() {
    SearchRecordManager.getInstance().asyncFetchSearchList().subscribe(searchInfos -> {
      SearchDataHolder.getInstance().setSearchInfos(searchInfos);
      historyPresenter.bind(null, getActivity());
    },throwable -> {
      System.currentTimeMillis();
    });
  }

  private void fetchHot() {
    ApiProvider.getMusicApiService().searchHot().subscribe(searchHotResponse -> {
      if (!CollectionUtil.isEmpty(searchHotResponse.hotSearchInfos)) {
        hotPresenter.bind(searchHotResponse.hotSearchInfos, getActivity());
      }
    });
  }

}
