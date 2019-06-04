package com.sf.sofarmusic.play.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;

import com.sf.base.util.FileUtil;
import com.sf.sofarmusic.api.ApiProvider;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.model.Song;
import com.sf.utility.LogUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 歌词缓存工具类
 * Created by sufan on 2017/11/9.
 */

public class LrcCacheUtil {

  private static final String TAG = "LrcCacheUtil";

  private Context context;
  private Song item;
  private boolean isLocal;

  public LrcCacheUtil(Context context, Song item, boolean isLocal) {
    this.context = context;
    this.item = item;
    this.isLocal = isLocal;
  }


  public void getLrc(LrcCallback callback) {
    // 从本地获取歌词
    String diskLrc = getLrcFromFile();
    if (diskLrc != null) {
      LogUtil.d(TAG, "磁盘中存在歌词：" + item.name);
      callback.onSuccess(diskLrc);
      return;
    }

    // 从网络上获取歌词
    getLrcFromNet(callback);
  }


  private void getLrcFromNet(LrcCallback callback) {
    LogUtil.d(TAG, "从网络中获取歌词：" + item.name);
    if (isLocal) {
      searchLrc(callback);
    } else {
      getLrc(item.lrcLink, callback);
    }
  }

  private void searchLrc(final LrcCallback callback) {
    ApiProvider.getMusicApiService().getLrcLink(item.name).observeOn(AndroidSchedulers.mainThread())
        .subscribe(lrcResponse -> {
          if (lrcResponse.mLrcList.isEmpty()) {
            callback.onError("获取歌词失败");
            return;
          }
          getLrc(lrcResponse.mLrcList.get(0).mLrcLink, callback);
        }, throwable -> {
          callback.onError("获取歌词失败");
        });
  }


  private void getLrc(String lrcUrl, final LrcCallback callback) {
    ApiProvider.getMusicApiService().getLrc(lrcUrl).observeOn(AndroidSchedulers.mainThread())
        .subscribe(lrcStr -> {
          callback.onSuccess(lrcStr);
          saveLrcToFile(lrcStr);
        }, throwable -> {
          callback.onError("获取歌词失败");
        });
  }


  private String getLrcFromFile() {
    String fileName = item.name + ".lrc";
    File file = new File(FileUtil.getLrcDir(context), fileName);
    // 确保路径没有问题
    if (file.exists() && file.length() > 0) {
      return readFile(file.getAbsolutePath());
    } else {
      return null;
    }
  }


  /**
   * 保存歌词到文件中
   */
  private void saveLrcToFile(String lrc) {
    String fileName = item.name + ".lrc";
    File file = new File(FileUtil.getLrcDir(context), fileName);
    try {
      if (!file.exists()) {
        file.createNewFile();
      }
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(lrc.getBytes("utf-8"));
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * 注意
   * fis.available() 返回文件大小（B），int类型，最多支持1.99GB的文件，
   * fis.getChannel().size() ，返回文件大小（B），long类型
   * 
   * @param path
   * @return
   */
  private String readFile(String path) {
    try {
      File file = new File(path);
      FileInputStream fis = new FileInputStream(file);
      byte[] buffer = new byte[fis.available()];
      fis.read(buffer);
      String result = new String(buffer, "utf-8");
      fis.close();
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public interface LrcCallback {

    void onSuccess(String lrc);

    void onError(String error);

  }
}
