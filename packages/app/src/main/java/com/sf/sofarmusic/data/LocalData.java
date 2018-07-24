package com.sf.sofarmusic.data;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.ColorItem;
import com.sf.sofarmusic.enity.MenuItem;
import com.sf.sofarmusic.enity.PlayItem;
import com.sf.sofarmusic.enity.SkinItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sufan on 17/3/29.
 */

public class LocalData {

    //左侧menu的数据
    public static List<MenuItem> getMenuListData() {
        List<MenuItem> menuList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MenuItem item = new MenuItem();
            switch (i) {
                case 0:
                    item.des=("我的消息");
                    item.icon=(R.string.icon_msg);
                    break;
                case 1:
                    item.des=("商城");
                    item.icon=(R.string.icon_shop);
                    break;
                case 2:
                    item.des=("会员中心");
                    item.icon=(R.string.icon_vip);
                    break;
                case 3:
                    item.des=("在线听歌免流量");
                    item.icon=(R.string.icon_line_music);
                    break;
                case 4:
                    item.des=("听歌识曲");
                    item.icon=(R.string.icon_listen);
                    break;
                case 5:
                    item.des=("主题换肤");
                    item.icon=(R.string.icon_skin);
                    break;

                case 6:
                    item.des=("定时停止播放");
                    item.icon=(R.string.icon_time);
                    break;
                case 7:
                    item.des=("扫一扫");
                    item.icon=(R.string.icon_scan);
                    break;
                case 8:
                    item.des=("音乐闹钟");
                    item.icon=(R.string.icon_clock);
                    break;
                case 9:
                    item.des=("驾驶模式");
                    item.icon=(R.string.icon_car);
                    break;
            }
            menuList.add(item);
        }
        return menuList;
    }

    //皮肤页面数据
    public static List<SkinItem> getSkinListData() {
        List<SkinItem> skinList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            SkinItem item = new SkinItem();
            switch (i) {
                case 0:
                    item.isTitle=(true);
                    item.des=("纯色主题");
                    break;
                case 1:
                    item.imgName="skin_blue_rectangle";
                    item.des=("官方蓝");
                    break;
                case 2:
                    item.imgName="skin_pink_rectangle";
                    item.des=("官方粉");
                    break;
                case 3:
                    item.imgName="multi_color";
                    item.des=("自选颜色");
                    item.isSelected=(true);
                    item.status="使用中";
                    break;
                case 4:
                    item.isTitle=(true);
                    item.des=("个性主题");
                    break;
                case 5:
                    item.imgName="skin_huoyin";
                    item.des=("火影忍者");
                    break;
                case 6:
                    item.imgName="skin_yinyang_master";
                    item.des=("阴阳师");
                    break;
            }
            skinList.add(item);
        }
        return skinList;
    }

    //自选颜色界面数据
    public static List<ColorItem> getColorListData() {
        List<ColorItem> colorList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            ColorItem item = new ColorItem();
            switch (i) {
                case 0:
                    item.color=(R.color.color_1);
                    item.isSelected=(true);
                    break;
                case 1:
                    item.color=(R.color.color_2);
                    break;
                case 2:
                    item.color=(R.color.color_3);
                    break;
                case 3:
                    item.color=(R.color.color_4);
                    break;
                case 4:
                    item.color=(R.color.color_5);
                    break;
                case 5:
                    item.color=(R.color.color_6);
                    break;
                case 6:
                    item.color=(R.color.color_7);
                    break;
                case 7:
                    item.color=(R.color.color_8);
                    break;
                case 8:
                    item.color=(R.color.color_9);
                    break;
                case 9:
                    item.color=(R.color.color_10);
                    break;
                case 10:
                    item.color=(R.color.color_11);
                    break;
                case 11:
                    item.color=(R.color.color_12);
                    break;
                case 12:
                    item.color=(R.color.color_13);
                    break;
                case 13:
                    item.color=(R.color.color_14);
                    break;
                case 14:
                    item.color=(R.color.color_15);
                    break;
                case 15:
                    item.des=("i am the last");
                    break;
            }
            colorList.add(item);
        }
        return colorList;
    }

    public static List<PlayItem> getPlayListData(){
        List<PlayItem> playList=new ArrayList<>();
        for(int i=0;i<3;i++){
            PlayItem item=new PlayItem();
            switch (i){
                case 0:
                   item.artist="1111";
                    item.name="2222";
                    break;
                case 1:
                    item.artist="1111";
                    item.name="2222";
                    break;
                case 2:
                    item.artist="1111";
                    item.name="2222";
                    break;
            }
            playList.add(item);
        }
        return playList;
    }

    public static List<MenuItem> getRankListMenuData() {
        List<MenuItem> menuList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            MenuItem item = new MenuItem();
            switch (i) {
                case 0:
                    item.des=("选择排序");
                    item.icon=(R.string.icon_sort);
                    break;
                case 1:
                    item.des=("清空下载文件");
                    item.icon=(R.string.icon_dustbin);
                    break;
            }
            menuList.add(item);
        }
        return menuList;
    }



    public static List<String> getStringList(){
        String[] ss={"七天存款","哈哈哈"};
        return Arrays.asList(ss);
    }



}
