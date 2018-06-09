package com.sf.sofarmusic.util;


import com.sf.sofarmusic.enity.LrcItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sufan on 16/11/30.
 */

public class LrcUtil {

    public static List<LrcItem> getLrcList(String s) {
        List<LrcItem> lrcList = new ArrayList<>();
        String[] ss = s.split("\n");
        for (int i = 0; i < ss.length; i++) {
            if ((ss[i].indexOf("[ar:") != -1) || (ss[i].indexOf("[ti:") != -1)
                    || (ss[i].indexOf("[by:") != -1)
                    || (ss[i].indexOf("[al:") != -1)
                    || (ss[i].indexOf("[offset:") != -1) || s.equals(" ")) {
                continue;
            }

            ss[i] = ss[i].replace("[", "");

            //关键代码，歌词用的时候需要对时间进行排序
            String splitLrc_data[] = ss[i].split("]");
            if (splitLrc_data.length > 1) {
                for (int j = 0; j < splitLrc_data.length-1; j++) {
                    LrcItem item = new LrcItem();
                    item.setTime(TimeStr(splitLrc_data[j]));
                    item.setContent(splitLrc_data[splitLrc_data.length - 1]);
                    lrcList.add(item);
                }
            }

        }

        Collections.sort(lrcList);

        return lrcList;
    }

    private static int TimeStr(String timeStr) {

        timeStr = timeStr.replace(":", ".");
        timeStr = timeStr.replace(".", "@");
        String timeData[] = timeStr.split("@");
        int currentTime = 0;
        // 分离出分、秒并转换为整型
        try {
            int minute = Integer.parseInt(timeData[0]);
            int second = Integer.parseInt(timeData[1]);
            int millisecond = Integer.parseInt(timeData[2]);
            currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentTime;
    }
}
