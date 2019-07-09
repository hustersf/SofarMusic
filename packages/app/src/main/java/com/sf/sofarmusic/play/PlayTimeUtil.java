package com.sf.sofarmusic.play;

public class PlayTimeUtil {

  /**
   * 返回 00:00格式的时间串
   */
  public static String getFormatTimeStr(long timeMills) {
    int minute = 0;
    int seconds = (int) (timeMills / 1000);

    if (seconds >= 60) {
      minute = seconds / 60;
      seconds = seconds % 60;
    }

    String timeStr = getTwoLength(minute) + ":" + getTwoLength(seconds);
    return timeStr;
  }

  private static String getTwoLength(int data) {
    if (data < 10) {
      return "0" + data;
    } else {
      return "" + data;
    }
  }
}
