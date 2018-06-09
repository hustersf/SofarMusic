package com.sf.sofarmusic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sf.sofarmusic.enity.PlayItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/9/26.
 * 存放播放列表
 */

public class PlayList {

    private static PlayList instance = null;
    private MusicDB mMusicDB;

    private PlayList(Context context) {
        mMusicDB = MusicDB.getInstance(context);
    }

    public static PlayList getInstance(Context context) {
        if (instance == null) {
            synchronized (PlayList.class) {
                if (instance == null) {
                    instance = new PlayList(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void onCreate(final SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(PlayListColumns.NAME);
        builder.append("(");

        builder.append(PlayListColumns.SONG_ID);
        builder.append(" TEXT NOT NULL PRIMARY KEY,");

        builder.append(PlayListColumns.SONG_NAME);
        builder.append(" TEXT NOT NULL,");

        builder.append(PlayListColumns.ARTIST);
        builder.append(" TEXT NOT NULL,");

        builder.append(PlayListColumns.ALBUM);
        builder.append(" TEXT,");

        builder.append(PlayListColumns.SHOW_URL);
        builder.append(" TEXT,");

        builder.append(PlayListColumns.DURATION);
        builder.append(" INT,");

        builder.append(PlayListColumns.ALBUM_ID);
        builder.append(" LONG,");

        builder.append(PlayListColumns.IMG_URI);
        builder.append(" TEXT,");

        builder.append(PlayListColumns.SMALL_URL);
        builder.append(" TEXT,");

        builder.append(PlayListColumns.BIG_URL);
        builder.append(" TEXT,");

        builder.append(PlayListColumns.LRC_URL);
        builder.append(" TEXT);");

        db.execSQL(builder.toString());
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlayListColumns.NAME);
        onCreate(db);
    }

    public void savePlayList(List<PlayItem> playList) {
        SQLiteDatabase database = mMusicDB.getWritableDatabase();
        database.beginTransaction();
        database.delete(PlayListColumns.NAME, null, null);
        for (PlayItem item : playList) {
            ContentValues cv = new ContentValues();
            cv.put(PlayListColumns.SONG_ID, item.songId);
            cv.put(PlayListColumns.SONG_NAME, item.name);
            cv.put(PlayListColumns.ARTIST, item.artist);
            cv.put(PlayListColumns.ALBUM, item.album);
            cv.put(PlayListColumns.SHOW_URL, item.showUrl);
            cv.put(PlayListColumns.DURATION, item.duration);
            cv.put(PlayListColumns.ALBUM_ID, item.albumId);
            cv.put(PlayListColumns.IMG_URI, item.imgUri);
            cv.put(PlayListColumns.SMALL_URL, item.smallUrl);
            cv.put(PlayListColumns.BIG_URL, item.bigUrl);
            cv.put(PlayListColumns.LRC_URL, item.lrcLinkUrl);
            database.insert(PlayListColumns.NAME,null,cv);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public List<PlayItem> getPlayList() {
        List<PlayItem> list = new ArrayList<>();
        SQLiteDatabase database = mMusicDB.getReadableDatabase();
        Cursor cursor = database.query(PlayListColumns.NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            PlayItem item=new PlayItem();
            item.songId=cursor.getString(0);
            item.name=cursor.getString(1);
            item.artist=cursor.getString(2);
            item.album=cursor.getString(3);
            item.showUrl=cursor.getString(4);
            item.duration=cursor.getInt(5);
            item.albumId=cursor.getLong(6);
            item.imgUri=cursor.getString(7);
            item.smallUrl=cursor.getString(8);
            item.bigUrl=cursor.getString(9);
            item.lrcLinkUrl=cursor.getString(10);
            list.add(item);
        }
        cursor.close();
        return list;
    }


    /**
     * 清空表
     */
    public void clearList(){
        SQLiteDatabase database = mMusicDB.getWritableDatabase();
        database.delete(PlayListColumns.NAME, null, null);
    }


    public interface PlayListColumns {

        //表名
        String NAME = "play_list";

        //表中的字段
        String SONG_ID = "songId";

        String SONG_NAME = "name";

        String ARTIST = "artist";

        String ALBUM = "album";

        String SHOW_URL = "showUrl";

        String DURATION = "duration";

        String ALBUM_ID = "albumId";

        String IMG_URI = "imgUri";

        String SMALL_URL = "smallUrl";

        String BIG_URL = "bigUrl";

        String LRC_URL = "lrcLinkUrl";

    }
}
