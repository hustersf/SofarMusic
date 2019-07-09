package com.sf.sofarmusic.db.song;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SongRecordDBOpenHelper extends DaoMaster.DevOpenHelper {

  public SongRecordDBOpenHelper(Context context, String name) {
    super(context, name);
  }

  public SongRecordDBOpenHelper(Context context, String name,
      SQLiteDatabase.CursorFactory factory) {
    super(context, name, factory);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    super.onCreate(db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    super.onUpgrade(db, oldVersion, newVersion);
  }

  @Override
  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    DaoMaster.dropAllTables(wrap(db), true);
    onCreate(db);
  }
}
