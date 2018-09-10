package com.sf.sofarmusic.shortcuts;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.play.PlayActivity;
import com.sf.sofarmusic.skin.ColorActivity;
import com.sf.sofarmusic.skin.SkinActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 7.1新特性，长按桌面图标，会弹出快捷列表
 */
public class ShortCutInitMoudle {

  private Context mContext;
  private ShortcutManager mShortcutManager;

  public void init(Context context) {
    if (isCanUse()) {
      mContext = context;
      mShortcutManager = context.getSystemService(ShortcutManager.class);
    }
  }

  private boolean isCanUse() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && mContext != null;
  }


  // 重置快捷方式
  public void resetShortCuts() {
    if (isCanUse()) {
      ShortcutInfo playCut = getPlayShortCut();
      ShortcutInfo skinCut = getSkinShortCut();
      ShortcutInfo colorCut = getColorShortCut();
      mShortcutManager.setDynamicShortcuts(Arrays.asList(playCut,skinCut, colorCut));
    }
  }

  // 添加快捷方式
  public void addShortCuts(@ShortcutId String... shortcutId) {
    if (isCanUse()) {
      List<ShortcutInfo> list = new ArrayList<>();
      for (String id : shortcutId) {
        list.add(getShortcutInfoById(id));
      }
      mShortcutManager.addDynamicShortcuts(list);
    }
  }


  // 删除快捷方式
  public void removeShortCuts(@ShortcutId String... shortcutId) {
    if (isCanUse()) {
      mShortcutManager.removeDynamicShortcuts(Arrays.asList(shortcutId));
      mShortcutManager.disableShortcuts(Arrays.asList(shortcutId));
    }
  }



  private ShortcutInfo getShortcutInfoById(@ShortcutId String shortcutId) {
    ShortcutInfo shortcutInfo = null;
    if (isCanUse()) {
      switch (shortcutId) {
        case ShortcutId.PLAY:
          shortcutInfo = getPlayShortCut();
          break;
        case ShortcutId.SKIN:
          shortcutInfo = getSkinShortCut();
          break;
        case ShortcutId.COLOR:
          shortcutInfo = getColorShortCut();
          break;
        default:
          break;
      }
    }
    return shortcutInfo;
  }



  @RequiresApi(api = Build.VERSION_CODES.N_MR1)
  private ShortcutInfo getPlayShortCut() {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setClass(mContext, PlayActivity.class);
    ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(mContext, ShortcutId.PLAY)
        .setShortLabel("播放短标签")
        .setLongLabel("播放长标签")
        .setRank(3)// 弹出的排序,越大离应用图标越远,静态的比动态的离应用图标近
        .setIcon(Icon.createWithResource(mContext, R.mipmap.ic_launcher))
        .setIntent(intent)// 设置一个intent就是点击启动的页面
        .build();
    return shortcutInfo;
  }

  @RequiresApi(api = Build.VERSION_CODES.N_MR1)
  private ShortcutInfo getSkinShortCut() {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setClass(mContext, SkinActivity.class);
    ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(mContext, ShortcutId.SKIN)
        .setShortLabel("皮肤短标签")
        .setLongLabel("皮肤长标签")
        .setRank(1)
        .setIcon(Icon.createWithResource(mContext, R.mipmap.ic_launcher))
        .setIntent(intent)// 设置一个intent就是点击启动的页面
        .build();
    return shortcutInfo;
  }

  @RequiresApi(api = Build.VERSION_CODES.N_MR1)
  private ShortcutInfo getColorShortCut() {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setClass(mContext, ColorActivity.class);
    ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(mContext, ShortcutId.COLOR)
        .setShortLabel("颜色短标签")
        .setLongLabel("颜色长标签")
        .setRank(2)// 弹出的排序,越大离应用图标越远,静态的比动态的离应用图标近
        .setIcon(Icon.createWithResource(mContext, R.mipmap.ic_launcher))
        .setIntent(intent)// 设置一个intent就是点击启动的页面
        .build();
    return shortcutInfo;
  }

  @StringDef({ShortcutId.PLAY, ShortcutId.SKIN, ShortcutId.COLOR})
  @Retention(RetentionPolicy.SOURCE)
  public @interface ShortcutId {

    String PLAY = "play"; // 播放界面

    String SKIN = "skin"; // 皮肤界面

    String COLOR = "color"; // 颜色界面
  }

}
