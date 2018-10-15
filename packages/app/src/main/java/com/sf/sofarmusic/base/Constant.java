package com.sf.sofarmusic.base;

import com.sf.sofarmusic.enity.PlayItem;

import java.util.List;

/**
 * Created by sufan on 17/2/28.
 */

public class Constant {

    //百度音乐api接口的ip
    public static final String Ip = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&";

    //歌曲列表相关
    public static final String SongList = "method=baidu.ting.billboard.billList&";
    public static final int RankCount = 3;

    //主打榜单
    public static final String NewSong = SongList + "type=1&size=" + RankCount + "&offset=0";
    public static final String HotSong = SongList + "type=2&size=" + RankCount + "&offset=0";
    public static final String SelfSong = SongList + "type=200&size=" + RankCount + "&offset=0";

    //分类榜单
    public static final String ChineseSong = SongList + "type=20&size=" + RankCount + "&offset=0";
    public static final String EASong = SongList + "type=21&size=" + RankCount + "&offset=0";
    public static final String FilmSong = SongList + "type=24&size=" + RankCount + "&offset=0";
    public static final String DoubleSong = SongList + "type=23&size=" + RankCount + "&offset=0";
    public static final String NetSong = SongList + "type=25&size=" + RankCount + "&offset=0";
    public static final String OldSong = SongList + "type=22&size=" + RankCount + "&offset=0";
    public static final String RockSong = SongList + "type=11&size=" + RankCount + "&offset=0";


    //媒体榜单
    public static final String BillboardSong = SongList + "type=8&size=" + RankCount + "&offset=0";

    //其他榜单
    public static final String KtvSong = "method=baidu.ting.billboard.billList&type=6&size=10&offset=0";
    public static final String SpriteSong = "method=baidu.ting.billboard.billList&type=9&size=10&offset=0";
    public static final String KingSong = "method=baidu.ting.billboard.billList&type=100&size=10&offset=0";  //没图

    public static final String Player = "method=baidu.ting.billboard.billList&type=26&size=100&offset=0";

    public static final String NNIp = "http://tingapi.ting.baidu.com/v1/restserver/ting" +
            "?format=json&calback=&from=webapp_music&method=baidu.ting.billboard.billList&type=26&size=100&offset=0";


    //酷狗获取歌词接口
    public static final String searchLrc = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.lrcys&format=json&query=";



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
