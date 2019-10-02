package com.sf.sofarmusic.db.search;


import android.database.sqlite.SQLiteDatabase;
import com.sf.sofarmusic.base.SofarApp;
import com.sf.sofarmusic.search.model.SearchInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 管理本地搜索历史数据
 */
public class SearchRecordManager {

  private DaoSession daoSession;

  private static class HolderClass {
    private static SearchRecordManager INSTANCE = new SearchRecordManager();
  }

  public static SearchRecordManager getInstance() {
    return HolderClass.INSTANCE;
  }

  private SearchRecordManager() {
    init();
  }

  private void init() {
    String dbName = "search_record.db";
    SearchRecordDBOpenHelper helper =
        new SearchRecordDBOpenHelper(SofarApp.getAppContext(), dbName, null);
    SQLiteDatabase db = helper.getWritableDatabase();
    daoSession = new DaoMaster(db).newSession();
  }


  /**
   * 更新本地搜索历史
   */
  public void asyncReplaceSearchList(List<SearchInfo> searchInfos) {
    Schedulers.newThread().scheduleDirect(() -> {

      List<SearchRecord> records = new ArrayList<>();
      for (SearchInfo searchInfo : searchInfos) {
        SearchRecord record = new SearchRecord();
        record.setWord(searchInfo.word);
        record.setLinkType(searchInfo.linkType);
        record.setLinkUrl(searchInfo.linkUrl);
        records.add(record);
      }

      daoSession.getSearchRecordDao().deleteAll();
      daoSession.getSearchRecordDao().insertInTx(records);

    });
  }

  /**
   * 获取本地搜索列表
   */
  public Observable<List<SearchInfo>> asyncFetchSearchList() {
    return Observable.fromCallable(new Callable<List<SearchInfo>>() {
      @Override
      public List<SearchInfo> call() {
        List<SearchRecord> records = daoSession.getSearchRecordDao().loadAll();

        List<SearchInfo> searchInfos = new ArrayList<>();
        for (SearchRecord record : records) {
          SearchInfo info = new SearchInfo();
          info.word = record.getWord();
          info.linkType = record.getLinkType();
          info.linkUrl = record.getLinkUrl();
          searchInfos.add(info);
        }
        return searchInfos;
      }
    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
  }

  /**
   * 清空本地搜索历史
   */
  public void asyncClearSearchList() {
    Schedulers.newThread().scheduleDirect(() -> {
      daoSession.getSearchRecordDao().deleteAll();
    });

  }
}
