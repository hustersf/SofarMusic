package com.sf.sofarmusic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sufan on 17/5/24.
 */
@Deprecated
public class MusicDB extends SQLiteOpenHelper {

    private final static String NAME = "music.db";
    private final static int VERSION = 1;

    private static MusicDB sInstance = null;
    private final Context mContext;


    private MusicDB(Context context) {
        super(context, NAME, null, VERSION);
        mContext = context;
    }

    public static MusicDB getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (MusicDB.class) {
                if (sInstance == null) {
                    sInstance = new MusicDB(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PlayStatus.getInstance(mContext).onCreate(db);
        PlayList.getInstance(mContext).onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PlayStatus.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        PlayList.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
    }
}
