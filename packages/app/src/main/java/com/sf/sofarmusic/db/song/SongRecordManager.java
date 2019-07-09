package com.sf.sofarmusic.db.song;

import android.database.sqlite.SQLiteDatabase;
import com.sf.libnet.gson.Gsons;
import com.sf.sofarmusic.base.SofarApp;
import com.sf.sofarmusic.model.Song;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 封装增删改查操作
 */
public class SongRecordManager {

  private static SongRecordManager instance;

  private DaoSession daoSession;

  public static SongRecordManager getInstance() {
    if (instance == null) {
      synchronized (SongRecordManager.class) {
        if (instance == null) {
          instance = new SongRecordManager();
        }
      }
    }
    return instance;
  }

  private SongRecordManager() {
    init();
  }

  private void init() {
    String dbName = "song_record.db";
    SongRecordDBOpenHelper helper =
        new SongRecordDBOpenHelper(SofarApp.getAppContext(), dbName, null);
    SQLiteDatabase db = helper.getWritableDatabase();
    daoSession = new DaoMaster(db).newSession();
  }

  /**
   * 替换歌曲列表
   */
  public void asyncReplaceSongList(List<Song> songs) {
    Schedulers.newThread().scheduleDirect(() -> {

      // 转化成SongRecord
      List<SongRecord> records = new ArrayList<>();
      for (Song song : songs) {
        SongRecord record = new SongRecord();
        record.setSongId(song.songId);
        record.setContent(Gsons.SOFAR_GSON.toJson(song));
        records.add(record);
      }

      daoSession.getSongRecordDao().deleteAll();
      daoSession.getSongRecordDao().insertInTx(records);
    });
  }

  /**
   * 清除歌曲列表
   */
  public void asyncClearSongList() {
    Schedulers.newThread().scheduleDirect(() -> {
      daoSession.getSongRecordDao().deleteAll();
    });
  }


  /**
   * 更新歌曲
   */
  public void asyncUpdateSong(Song song) {
    Schedulers.newThread().scheduleDirect(() -> {
      SongRecord record = daoSession.getSongRecordDao().queryBuilder()
          .where(SongRecordDao.Properties.SongId.eq(song.songId)).unique();
      record.setSongId(song.songId);
      record.setContent(Gsons.SOFAR_GSON.toJson(song));
      daoSession.getSongRecordDao().update(record);
    });
  }

  /**
   * 获取歌曲列表
   */
  public Observable<List<Song>> asyncFetchSongList() {
    return Observable.fromCallable(new Callable<List<Song>>() {
      @Override
      public List<Song> call() {
        List<SongRecord> records = daoSession.getSongRecordDao().loadAll();

        List<Song> songs = new ArrayList<>();
        for (SongRecord record : records) {
          Song song = Gsons.SOFAR_GSON.fromJson(record.getContent(), Song.class);
          songs.add(song);
        }
        return songs;
      }
    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
  }


}
