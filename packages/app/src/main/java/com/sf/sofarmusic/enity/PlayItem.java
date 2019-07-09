package com.sf.sofarmusic.enity;

import java.io.Serializable;

/**
 * Created by sufan on 16/11/18.
 * 歌曲实体
 * 通过songId和tingId可以查询任何和歌曲有关的信息
 *
 * --表示从榜单接口中查询到的歌曲信息
 * 。。表示本地媒体库查询到的歌曲信息
 */
@Deprecated
public class PlayItem implements Serializable {

    public String content;   //sheet列表内容
    public boolean isSelected;   //是否被选中

    public boolean isImport;   //是否加重
    public boolean isFirstClick=true;   //是否是第一次点击

    public String songId;    //歌的id      --         。。
    public String name;    //歌名         --          。。
    public String artist;   //演唱者       --         。。
    public String tingId;    //歌手的id
    public String album;   //专辑名                    。。

    public String smallUrl;   //小图     --
    public String bigUrl;     //大图     --
    public String lrcLinkUrl;   //歌词   --

    public String showUrl;    //在线播放              。。
    public String fileUrl;    //下载地址
    public int duration;    //播放时间秒              。。

    //获取艺术家和专辑图片所需要
    public long albumId;    //                          。。
    public long artistId;    //                         。。
    public String imgUri;  //图片的uri                 。。




}
