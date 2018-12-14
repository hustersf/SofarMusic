package com.sf.sofarmusic.local;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import com.sf.sofarmusic.enity.AlbumItem;
import com.sf.sofarmusic.enity.ArtistItem;
import com.sf.sofarmusic.enity.FileItem;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.utility.LogUtil;


/**
 * Created by sufan on 16/12/1.
 */

public class MusicLoader {


    private static MusicLoader instance;
    private Handler mDelivery;
    private Context mContext;

    private static Uri contentUri = Media.EXTERNAL_CONTENT_URI;

    //contentUri   content://media/external/audio/media

    //安卓多媒体数据库表所在位置  /data/data/com.android.providers.media/external.db


    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private String mDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    private String[] projection = {
            Media._ID,
            Media.TITLE,
            Media.DATA,
            Media.ALBUM,
            Media.ARTIST,
            Media.DURATION,
            Media.SIZE

    };
    private String where = "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 ";
    private String sortOrder = Media.DATA;


    private MusicLoader() {
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static MusicLoader getInstance() {
        if (instance == null) {
            synchronized (MusicLoader.class) {
                if (instance == null) {
                    instance = new MusicLoader();
                }
            }
        }
        return instance;
    }

    /**
     * 发送广播让手机重新加载内存卡
     * 更新媒体库
     * 貌似4.4以上的手机不行
     */

    private void sendSDcardBroadcast() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
        } else {
            intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        }
        intent.setData(Uri.parse("file://" + Environment.getExternalStorageDirectory()));
        mContext.sendBroadcast(intent);
    }


    public void LoadLocalMusicList(final Context context, final LoadCallback callback) {
        mContext = context.getApplicationContext();
        new LoadThread(callback).start();
    }

    class LoadThread extends Thread {

        private LoadCallback callback;

        public LoadThread(LoadCallback callback) {
            this.callback = callback;

        }

        @Override
        public void run() {
            super.run();

            final List<PlayItem> localList = new ArrayList<>();

            Cursor cursor = mContext.getContentResolver().query(contentUri, null, null, null, null);

            if (cursor == null) {
                Log.i("TAG", "Line(116	)	Music Loader cursor == null.");
            } else if (!cursor.moveToFirst()) {
                Log.i("TAG", "Line(118	)	Music Loader cursor.moveToFirst() returns false.");
            } else {
                int displayNameCol = cursor.getColumnIndex(Media.TITLE);
                int albumIdCol = cursor.getColumnIndex(Media.ALBUM_ID);
                int albumCol = cursor.getColumnIndex(Media.ALBUM);
                int idCol = cursor.getColumnIndex(Media._ID);
                int durationCol = cursor.getColumnIndex(Media.DURATION);
                int sizeCol = cursor.getColumnIndex(Media.SIZE);
                int artistIdCol = cursor.getColumnIndex(Media.ARTIST_ID);
                int artistCol = cursor.getColumnIndex(Media.ARTIST);
                int urlCol = cursor.getColumnIndex(Media.DATA);
                do {
                    String title = cursor.getString(displayNameCol);
                    String album = cursor.getString(albumCol);
                    long id = cursor.getLong(idCol);
                    int duration = cursor.getInt(durationCol);
                    long size = cursor.getLong(sizeCol);
                    String artist = cursor.getString(artistCol);
                    String url = cursor.getString(urlCol);

                    long albumId = cursor.getLong(albumIdCol);
                    long artistId = cursor.getLong(artistIdCol);

                    PlayItem item = new PlayItem();
                    item.album = album;
                    item.duration = duration;
                    item.artist = artist;
                    item.showUrl = url;
                    item.songId = Long.toString(id);
                    item.name = title;

                    item.albumId = albumId;
                    item.artistId = artistId;

                    item.imgUri = getAlbumArt(albumId);
                    LogUtil.d("TAG","url:"+url);

                    localList.add(item);
                } while (cursor.moveToNext());
            }

            cursor.close();
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
                    callback.onLoad(localList);
                }
            });
        }
    }


    //文件列表
    public List<FileItem> getLocalFileList(List<PlayItem> playList) {

        List<String> urlString = new ArrayList<>();
        for (int i = 0; i < playList.size(); i++) {
            String url = playList.get(i).showUrl;
            int index = url.lastIndexOf("/");
            String parent = url.substring(0, index);
            if (!urlString.contains(parent)) {
                urlString.add(parent);
            }
        }

        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < urlString.size(); i++) {
            fileList.add(new File(urlString.get(i)));
        }


        //文件路径相同的在一起
        final List<FileItem> fileItemList = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            String path = fileList.get(i).getAbsolutePath();
            int index = path.lastIndexOf("/");
            String parent = path.substring(0, index + 1);
            String name = path.substring(index + 1);

            FileItem fileItem = new FileItem();
            fileItem.path = path;
            fileItem.parent = parent;
            fileItem.name = name;
            List<PlayItem> pList = new ArrayList<>();


            for (int j = 0; j < playList.size(); j++) {
                PlayItem playItem = playList.get(j);
                String url = playItem.showUrl;
                int pIndex = url.lastIndexOf("/");
                String purl = url.substring(0, pIndex);
                if (purl.equals(fileItem.path)) {
                    pList.add(playItem);
                }
            }

            for (int k = 0; k < pList.size(); k++) {
                if (pList.get(k).isSelected) {
                    fileItem.isSelected = true;
                }
            }
            fileItem.fileList = pList;
            fileItemList.add(fileItem);
            //     Log.i("TAG",fileItem.getParent()+" "+fileItem.getName()+"  "+fileItem.getFileList().size());
        }
        return fileItemList;
    }


    //艺术家列表
    public List<ArtistItem> getLocalArtistList(List<PlayItem> playList) {

        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < playList.size(); i++) {
            long id = playList.get(i).artistId;
            if (!idList.contains(id)) {
                idList.add(id);
            }
        }

        final List<ArtistItem> artistList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            ArtistItem item = new ArtistItem();
            List<PlayItem> pList = new ArrayList<>();
            for (int j = 0; j < playList.size(); j++) {
                PlayItem pItem = playList.get(j);
                if (pItem.artistId == idList.get(i)) {
                    pList.add(pItem);
                    item.name = pItem.artist;
                    item.artistId = pItem.artistId;
                    Long songId = Long.valueOf(pItem.songId);
                }
            }

            for (int k = 0; k < pList.size(); k++) {
                if (pList.get(k).isSelected) {
                    item.isSelected = true;
                }
            }

            item.artistList = pList;
            artistList.add(item);
        }
        return artistList;

    }


    //专辑列表
    public List<AlbumItem> getLocalAlbumList(List<PlayItem> playList) {

        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < playList.size(); i++) {
            long id = playList.get(i).albumId;
            if (!idList.contains(id)) {
                idList.add(id);
            }
        }

        final List<AlbumItem> albumList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            AlbumItem item = new AlbumItem();
            List<PlayItem> pList = new ArrayList<>();

            for (int j = 0; j < playList.size(); j++) {
                PlayItem pItem = playList.get(j);
                if (pItem.albumId == idList.get(i)) {
                    pList.add(pItem);
                    item.albumName = pItem.album;
                    item.albumId = pItem.albumId;
                    item.artistName = pItem.artist;
                    item.imgUri = getAlbumArt(idList.get(i));

                }
            }
            item.albumList = pList;
            for (int k = 0; k < pList.size(); k++) {
                if (pList.get(k).isSelected) {
                    item.isSelected = true;
                }
            }
            albumList.add(item);
        }

        return albumList;

    }

    public String getAlbumArt(long album_id) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), album_id).toString();
    }


    public interface LoadCallback {
        void onLoad(Object obj);
    }

}
