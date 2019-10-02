package com.sf.sofarmusic.search;

import com.sf.sofarmusic.db.search.SearchRecordManager;
import com.sf.sofarmusic.search.model.SearchInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地搜索历史列表
 */
public class SearchDataHolder {

  private List<SearchInfo> searchInfos;

  private SearchDataHolder() {}

  private static class HolderClass {
    private static SearchDataHolder sInstance = new SearchDataHolder();
  }

  public static SearchDataHolder getInstance() {
    return HolderClass.sInstance;
  }


  public void setSearchInfos(List<SearchInfo> data) {
    if (searchInfos == null) {
      searchInfos = new ArrayList<>();
    }

    searchInfos.clear();
    searchInfos.addAll(data);
  }

  public List<SearchInfo> getSearchInfos() {
    return searchInfos;
  }

  public void insertSearchInfo(SearchInfo info) {
    if (searchInfos == null) {
      searchInfos = new ArrayList<>();
    }
    searchInfos.remove(info);
    searchInfos.add(0, info);
  }

  public void clear(boolean clearCache) {
    searchInfos.clear();
    if (clearCache) {
      SearchRecordManager.getInstance().asyncClearSearchList();
    }
  }

}
