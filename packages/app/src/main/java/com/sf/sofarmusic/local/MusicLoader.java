package com.sf.sofarmusic.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import com.sf.sofarmusic.local.model.AlbumItem;
import com.sf.sofarmusic.local.model.AuthorItem;
import com.sf.sofarmusic.local.model.FileItem;
import com.sf.sofarmusic.model.Song;
import com.sf.utility.LogUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取手机本地音乐列表
 * 1. 如何获取本地音乐？
 * 安卓系统会为多媒体类型的文件（比如图片、音频、视频等）建立数据库，这些共享数据可以通过ContentProvider来获取
 * 
 * 安卓多媒体数据库表所在位置 /data/data/com.android.providers.media/external.db
 *
 * 
 * 2. 当本地新增或删除歌曲后，如何及时更新本地数据库信息？
 * 当新增或删除歌曲时，不重新开机的话是不会马上更细数据库的
 * 方案：在每次重进进入app时，重新扫描一下文件
 * 
 */
public class MusicLoader {

  private static MusicLoader instance;
  private static final String TAG = "MusicLoader";
  // 音频数据库共享地址
  private Uri audioUri = Media.EXTERNAL_CONTENT_URI;

  private final Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
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
  private String where =
      "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 ";
  private String sortOrder = Media.DATA;

  // 过滤歌曲
  private List<String> authorFilter;

  private MusicLoader() {
    authorFilter = new ArrayList<>();
    authorFilter.add("<unknown>");
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
  private void sendSDCardBroadcast(Context context) {
    Intent intent = new Intent();
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
    } else {
      intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    }
    intent.setData(Uri.parse("file://" + Environment.getExternalStorageDirectory()));
    context.sendBroadcast(intent);
  }


  /**
   * 异步获取本地音乐列表
   */
  public Observable<List<Song>> loadLocalMusicListAsync(final Context context) {
    return Observable.fromCallable(new Callable<List<Song>>() {
      @Override
      public List<Song> call() {
        return queryLocalMusic(context.getApplicationContext());
      }
    }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
  }


  private List<Song> queryLocalMusic(Context context) {
    final List<Song> localList = new ArrayList<>();
    Cursor cursor = context.getContentResolver().query(audioUri, null, null, null, null);

    if (cursor == null) {
      LogUtil.d(TAG, "Music Loader cursor == null.");
    } else if (!cursor.moveToFirst()) {
      LogUtil.d(TAG, "Music Loader cursor.moveToFirst() returns false.");
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
        long duration = cursor.getLong(durationCol);
        long size = cursor.getLong(sizeCol);
        String artist = cursor.getString(artistCol);
        String url = cursor.getString(urlCol);
        long albumId = cursor.getLong(albumIdCol);
        long artistId = cursor.getLong(artistIdCol);

        Song item = new Song();
        item.songId = Long.toString(id);
        item.name = title;
        item.albumId = Long.toString(albumId);
        item.albumTitle = album;
        item.author = artist;
        item.duration = duration;
        item.songUri = url;
        item.authorId = Long.toString(artistId);
        item.albumImgUri = getAlbumArt(albumId);
        if (!authorFilter.contains(artist)) {
          localList.add(item);
        }
      } while (cursor.moveToNext());
    }
    cursor.close();
    return localList;
  }


  /**
   * 将歌曲列表按文件分类
   */
  public List<FileItem> sortByFile(List<Song> songs) {
    List<FileItem> fileItems = new ArrayList<>();

    Map<String, FileItem> fileMap = new HashMap<>();
    for (int i = 0; i < songs.size(); i++) {
      // 将歌曲分成不用的文件目录
      Song song = songs.get(i);
      String path = song.songUri;
      String parentPath = path.substring(0, path.lastIndexOf("/"));
      FileItem item;
      if (!fileMap.containsKey(parentPath)) {
        item = new FileItem();
        fileMap.put(parentPath, item);
      } else {
        item = fileMap.get(parentPath);
      }

      // 文件目录的路径
      item.path = parentPath;
      int index = item.path.lastIndexOf("/");
      item.parent = item.path.substring(0, index + 1);
      item.name = item.path.substring(index + 1);
      if (song.play) {
        item.selected = true;
      }
      if (item.songs == null) {
        item.songs = new ArrayList<>();
        fileItems.add(item);
      }
      item.songs.add(song);
    }
    return fileItems;
  }

  /**
   * 将歌曲列表按作者分类
   */
  public List<AuthorItem> sortByAuthor(List<Song> songs) {
    List<AuthorItem> authorItems = new ArrayList<>();

    Map<String, AuthorItem> authorMap = new HashMap<>();
    for (int i = 0; i < songs.size(); i++) {
      // 找到每首歌的歌手
      Song song = songs.get(i);
      String authorId = song.authorId;
      AuthorItem item;
      if (!authorMap.containsKey(authorId)) {
        item = new AuthorItem();
        authorMap.put(authorId, item);
      } else {
        item = authorMap.get(authorId);
      }

      // 歌手信息
      item.authorId = authorId;
      item.name = song.author;
      if (song.play) {
        item.selected = true;
      }
      if (item.songs == null) {
        item.songs = new ArrayList<>();
        authorItems.add(item);
      }
      item.songs.add(song);
    }
    return authorItems;
  }

  /**
   * 将歌曲列表按专辑分类
   */
  public List<AlbumItem> sortByAlbum(List<Song> songs) {
    List<AlbumItem> albumItems = new ArrayList<>();

    Map<String, AlbumItem> albumMap = new HashMap<>();
    for (int i = 0; i < songs.size(); i++) {
      // 找到每首歌的专辑
      Song song = songs.get(i);
      String albumId = song.albumId;
      AlbumItem item;
      if (!albumMap.containsKey(albumId)) {
        item = new AlbumItem();
        albumMap.put(albumId, item);
      } else {
        item = albumMap.get(albumId);
      }

      // 专辑信息
      item.albumId = albumId;
      item.albumName = song.albumTitle;
      item.authorName = song.author;
      item.imgUri = song.albumImgUri;
      if (song.play) {
        item.selected = true;
      }
      if (item.songs == null) {
        item.songs = new ArrayList<>();
        albumItems.add(item);
      }
      item.songs.add(song);
    }
    return albumItems;
  }

  private String getAlbumArt(long album_id) {
    return ContentUris
        .withAppendedId(Uri.parse("content://media/external/audio/albumart"), album_id).toString();
  }

}
