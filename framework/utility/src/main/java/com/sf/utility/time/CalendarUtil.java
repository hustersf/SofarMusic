package com.sf.utility.time;

import java.util.Calendar;

public class CalendarUtil {

  /**
   * 获取给定time时间 几天后的时间
   */
  public static Calendar getDateBefor(long time, int day) {
    Calendar now = Calendar.getInstance();
    now.setTimeInMillis(time);
    now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
    return now;
  }

  /**
   * 获取给定time时间 几天后的时间
   */
  public static Calendar getDateAfter(long time, int day) {
    Calendar now = Calendar.getInstance();
    now.setTimeInMillis(time);
    now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
    return now;
  }

}
