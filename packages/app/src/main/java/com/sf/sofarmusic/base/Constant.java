package com.sf.sofarmusic.base;

import java.util.List;

import com.sf.sofarmusic.enity.PlayItem;

/**
 * Created by sufan on 17/2/28.
 */

public class Constant {
    //底部播放列表相关
    public static List<PlayItem> sPlayList;   //播放列表

    //本地音乐相关
    public static List<PlayItem> sLocalList;   //本地音乐列表
    public static List<PlayItem> sPreList;   //预播放列表

    public static final String RefreshProgress = "com.action.refresh.progress";
    public static final String RefreshPosition = "com.action.refresh.position";

    public static final String NOTIFY_CLOSE = "com.action.notify.close";
    public static final String NOTIFY_NEXT = "com.action.notify.next";
    public static final String NOTIFY_PLAY = "com.action.notify.play";

    public static final String NOTIFY_SERVICE_PLAY = "com.action.notify.service.play";
    public static final String NOTIFY_SERVICE_PAUSE = "com.action.notify.service.pause";
}
