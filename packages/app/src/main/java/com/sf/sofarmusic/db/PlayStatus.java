package com.sf.sofarmusic.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.IntDef;

/**
 * Created by sufan on 17/6/7.
 * 0代表在线歌曲，1代表本地歌曲
 */
@Deprecated
public class PlayStatus {

  private static PlayStatus instance = null;
  private MusicDB mMusicDB;


  // 暂停或者播放
  public static final int PAUSE = 0;
  public static final int PLAY = 1;

  // 在线歌曲还是本地歌曲
  public static final int ONLINE = 0;
  public static final int LOCAL = 1;


  // 歌曲循环模式
  public static final int LIST_CYCLE = 0;
  public static final int SINGLE_CYCLE = 1;
  public static final int RANDOW_CYCLE = 2;


  private PlayStatus(Context context) {
    mMusicDB = MusicDB.getInstance(context);
  }

  public static PlayStatus getInstance(Context context) {
    if (instance == null) {
      synchronized (PlayStatus.class) {
        if (instance == null) {
          instance = new PlayStatus(context.getApplicationContext());
        }
      }
    }
    return instance;
  }


  public void onCreate(final SQLiteDatabase db) {
    StringBuilder builder = new StringBuilder();
    builder.append("CREATE TABLE IF NOT EXISTS ");
    builder.append(StatusColumns.NAME);
    builder.append("(");

    builder.append(StatusColumns._ID);
    builder.append(" INT NOT NULL PRIMARY KEY,");

    builder.append(StatusColumns.STATUS);
    builder.append(" INT,");

    builder.append(StatusColumns.IS_LOCAL);
    builder.append(" INT,");

    builder.append(StatusColumns.POSITION);
    builder.append(" INT,");

    builder.append(StatusColumns.MODE);
    builder.append(" INT);");

    db.execSQL(builder.toString());


    // 初始化的时候插入一条数据
    ContentValues cv = new ContentValues();
    cv.put(StatusColumns._ID, 1);
    cv.put(StatusColumns.STATUS, 0);
    cv.put(StatusColumns.IS_LOCAL, -1);
    cv.put(StatusColumns.POSITION, -1);
    cv.put(StatusColumns.MODE, 0);
    db.insert(StatusColumns.NAME, null, cv);
  }

  public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + StatusColumns.NAME);
    onCreate(db);

  }


  public void setStatus(@SongStatus int status) {
    SQLiteDatabase database = mMusicDB.getWritableDatabase();
    String updateSql = "update play_status set status=" + status + " where _id=1";
    database.execSQL(updateSql);
  }

  // 获取播放状态(暂停or播放)
  public int getStatus() {
    int status = -1;
    SQLiteDatabase database = mMusicDB.getReadableDatabase();

    Cursor cursor = database.query(StatusColumns.NAME, null, null, null, null, null, null);
    if (cursor != null && cursor.moveToFirst()) {
      status = cursor.getInt(cursor.getColumnIndex(StatusColumns.STATUS));
      cursor.close();
    }
    return status;
  }

  public void setType(@SongType int type) {
    SQLiteDatabase database = mMusicDB.getWritableDatabase();
    String updateSql = "update play_status set isLocal=" + type + " where _id=1";
    database.execSQL(updateSql);
  }

  // 获取歌曲是本地的还是在线的
  public int getType() {
    int type = -1;
    SQLiteDatabase database = mMusicDB.getReadableDatabase();

    Cursor cursor = database.query(StatusColumns.NAME, null, null, null, null, null, null);
    if (cursor != null && cursor.moveToFirst()) {
      type = cursor.getInt(cursor.getColumnIndex(StatusColumns.IS_LOCAL));
      cursor.close();
    }
    return type;
  }


  public void setPosition(int position) {
    SQLiteDatabase database = mMusicDB.getWritableDatabase();
    String updateSql = "update play_status set position=" + position + " where _id=1";
    database.execSQL(updateSql);

  }

  // 获取需要播放歌曲在列表中的位置
  public int getPosition() {
    int position = -1;
    SQLiteDatabase database = mMusicDB.getReadableDatabase();

    Cursor cursor = database.query(StatusColumns.NAME, null, null, null, null, null, null);
    if (cursor != null && cursor.moveToFirst()) {
      position = cursor.getInt(cursor.getColumnIndex(StatusColumns.POSITION));
      cursor.close();
    }
    return position;
  }


  public void setMode(@SongMode int mode) {
    SQLiteDatabase database = mMusicDB.getWritableDatabase();
    String updateSql = "update play_status set mode=" + mode + " where _id=1";
    database.execSQL(updateSql);
  }

  // 获取当前的播放模式
  public int getMode() {
    int mode = 0;
    SQLiteDatabase database = mMusicDB.getReadableDatabase();

    Cursor cursor = database.query(StatusColumns.NAME, null, null, null, null, null, null);
    if (cursor != null && cursor.moveToFirst()) {
      mode = cursor.getInt(cursor.getColumnIndex(StatusColumns.MODE));
      cursor.close();
    }
    return mode;
  }

  @IntDef({PAUSE, PLAY})
  @Retention(RetentionPolicy.SOURCE)
  public @interface SongStatus {

  }

  @IntDef({LOCAL, ONLINE})
  @Retention(RetentionPolicy.SOURCE)
  public @interface SongType {

  }

  @IntDef({LIST_CYCLE, SINGLE_CYCLE, RANDOW_CYCLE})
  @Retention(RetentionPolicy.SOURCE)
  public @interface SongMode {

  }

  public interface StatusColumns {

    // 表名
    String NAME = "play_status";


    // 表中的字段
    String _ID = "_id"; // 主键

    String STATUS = "status";

    String IS_LOCAL = "isLocal";

    String POSITION = "position";

    String MODE = "mode";

  }
}
