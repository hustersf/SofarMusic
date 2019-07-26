package com.sf.demo.system.calendar;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 操作系统日历
 */
public class CalendarReminderUtil {

  private static String calanderURL = "content://com.android.calendar/calendars";
  private static String calanderEventURL = "content://com.android.calendar/events";
  private static String calanderRemiderURL = "content://com.android.calendar/reminders";

  private static String CALENDARS_NAME = "日历名字";
  private static String CALENDARS_ACCOUNT_NAME = "日历账户名字";
  private static String CALENDARS_ACCOUNT_TYPE = "日历账户类型";
  private static String CALENDARS_DISPLAY_NAME = "日历展示名字";

  /**
   * 获取日历ID
   * 
   * @param context
   * @return 日历ID
   */
  private static int checkAndAddCalendarAccounts(Context context) {
    int oldId = checkCalendarAccounts(context);
    if (oldId >= 0) {
      return oldId;
    } else {
      long addId = addCalendarAccount(context);
      if (addId >= 0) {
        return checkCalendarAccounts(context);
      } else {
        return -1;
      }
    }
  }

  /**
   * 检查是否存在日历账户
   * 
   * @param context
   * @return
   */
  private static int checkCalendarAccounts(Context context) {

    Cursor userCursor = context.getContentResolver().query(Uri.parse(calanderURL), null, null, null,
        CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL + " ASC ");
    try {
      if (userCursor == null)// 查询返回空值
        return -1;
      int count = userCursor.getCount();
      if (count > 0) {// 存在现有账户，取第一个账户的id返回
        userCursor.moveToLast();
        return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
      } else {
        return -1;
      }
    } finally {
      if (userCursor != null) {
        userCursor.close();
      }
    }
  }

  /**
   * 添加一个日历账户
   * 
   * @param context
   * @return
   */
  private static long addCalendarAccount(Context context) {
    TimeZone timeZone = TimeZone.getDefault();
    ContentValues value = new ContentValues();
    value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);

    value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
    value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
    value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
    value.put(CalendarContract.Calendars.VISIBLE, 1);
    value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
    value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
        CalendarContract.Calendars.CAL_ACCESS_OWNER);
    value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
    value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
    value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
    value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

    Uri calendarUri = Uri.parse(calanderURL);
    calendarUri = calendarUri.buildUpon()
        .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
        .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
        .build();

    Uri result = context.getContentResolver().insert(calendarUri, value);
    long id = result == null ? -1 : ContentUris.parseId(result);
    return id;
  }

  /**
   * 向日历中添加一个事件
   * 
   * @param context
   * @param calendar_id （必须参数）
   * @param title
   * @param description
   * @param beginTime 事件开始时间，以从公元纪年开始计算的协调世界时毫秒数表示。 （必须参数）
   * @param endTime 事件结束时间，以从公元纪年开始计算的协调世界时毫秒数表示。（非重复事件：必须参数）
   * @return
   */
  private static Uri insertCalendarEvent(Context context, long calendar_id, String title,
      String description, long beginTime, long endTime) {
    ContentValues event = new ContentValues();
    event.put("title", title);
    event.put("description", description);
    // 插入账户的id
    event.put("calendar_id", calendar_id);
    event.put(CalendarContract.Events.DTSTART, beginTime);// 必须有
    event.put(CalendarContract.Events.DTEND, endTime);// 非重复事件：必须有
    event.put(CalendarContract.Events.HAS_ALARM, 1);// 设置有闹钟提醒
    event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());// 这个是时区，必须有，
    // 添加事件
    Uri newEvent = context.getContentResolver().insert(Uri.parse(calanderEventURL), event);
    return newEvent;
  }

  /**
   * 查询日历事件
   * 
   * @param context
   * @param title 事件标题
   * @return 事件id,查询不到则返回""
   */
  private static String queryCalendarEvent(Context context, long calendar_id, String title,
      String description, long startTime, long endTime) {
    // 根据日期范围构造查询
    Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
    ContentUris.appendId(builder, startTime);
    ContentUris.appendId(builder, endTime);
    Cursor cursor = context.getContentResolver().query(builder.build(), null, null, null, null);
    String tmp_title;
    String tmp_desc;
    long temp_calendar_id;
    if (cursor.moveToFirst()) {
      do {
        tmp_title = cursor.getString(cursor.getColumnIndex("title"));
        tmp_desc = cursor.getString(cursor.getColumnIndex("description"));
        temp_calendar_id = cursor.getLong(cursor.getColumnIndex("calendar_id"));
        long dtstart = cursor.getLong(cursor.getColumnIndex("dtstart"));
        if (TextUtils.equals(title, tmp_title) && TextUtils.equals(description, tmp_desc)
            && calendar_id == temp_calendar_id && dtstart == startTime) {
          String eventId = cursor.getString(cursor.getColumnIndex("event_id"));
          return eventId;
        }
      } while (cursor.moveToNext());
    }
    cursor.close();
    return "";
  }

  /**
   * 添加日历提醒：标题、描述、开始时间共同标定一个单独的提醒事件
   * 
   * @param context
   * @param title 日历提醒的标题,不允许为空
   * @param description 日历的描述（备注）信息
   * @param beginTime 事件开始时间，以从公元纪年开始计算的协调世界时毫秒数表示。
   * @param endTime 事件结束时间，以从公元纪年开始计算的协调世界时毫秒数表示。
   * @param remindMinutes 提前remind_minutes分钟发出提醒
   * @param callback 添加提醒是否成功结果监听
   */
  public static void addCalendarEventRemind(Context context, String title, String description,
      long beginTime, long endTime, int remindMinutes, onCalendarRemindListener callback) {
    long calendar_id = checkAndAddCalendarAccounts(context);
    if (calendar_id < 0) {
      // 获取日历失败直接返回
      if (callback != null) {
        callback.onFailed(onCalendarRemindListener.CALENDAR_ERROR);
      }
      return;
    }
    // 根据标题、描述、开始时间查看提醒事件是否已经存在
    String event_id =
        queryCalendarEvent(context, calendar_id, title, description, beginTime, endTime);
    // 如果提醒事件不存在，则新建事件
    if (TextUtils.isEmpty(event_id)) {
      Uri newEvent =
          insertCalendarEvent(context, calendar_id, title, description, beginTime, endTime);
      if (newEvent == null) {
        // 添加日历事件失败直接返回
        if (callback != null) {
          callback.onFailed(onCalendarRemindListener.EVENT_ERROR);
        }
        return;
      }
      event_id = ContentUris.parseId(newEvent) + "";
    }
    // 为事件设定提醒
    ContentValues values = new ContentValues();
    values.put(CalendarContract.Reminders.EVENT_ID, event_id);
    // 提前remind_minutes分钟有提醒
    values.put(CalendarContract.Reminders.MINUTES, remindMinutes);
    values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
    Uri uri = context.getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
    if (uri == null) {
      // 添加提醒失败直接返回
      if (callback != null) {
        callback.onFailed(onCalendarRemindListener.REMIND_ERROR);
      }
      return;
    }

    // 添加提醒成功
    if (null != callback) {
      callback.onSuccess();
    }
  }

  /**
   * 删除日历提醒事件：根据标题、描述和开始时间来定位日历事件
   * 
   * @param context
   * @param title 提醒的标题
   * @param description 提醒的描述：deeplink URI
   * @param startTime 事件的开始时间
   * @param callback 删除成功与否的监听回调
   */
  public static void deleteCalendarEventRemind(Context context, String title, String description,
      long startTime, onCalendarRemindListener callback) {
    Cursor eventCursor =
        context.getContentResolver().query(Uri.parse(calanderEventURL), null, null, null, null);
    try {
      if (eventCursor == null)// 查询返回空值
        return;
      if (eventCursor.getCount() > 0) {
        // 遍历所有事件，找到title、description、startTime跟需要查询的title、descriptio、dtstart一样的项
        for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
          String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
          String eventDescription =
              eventCursor.getString(eventCursor.getColumnIndex("description"));
          long dtstart = eventCursor.getLong(eventCursor.getColumnIndex("dtstart"));
          if (!TextUtils.isEmpty(title) && title.equals(eventTitle)
              && !TextUtils.isEmpty(description) && description.equals(eventDescription)
              && dtstart == startTime) {
            int id = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));// 取得id
            Uri deleteUri = ContentUris.withAppendedId(Uri.parse(calanderEventURL), id);
            int rows = context.getContentResolver().delete(deleteUri, null, null);
            if (rows == -1) {
              // 删除提醒失败直接返回
              if (null != callback) {
                callback.onFailed(onCalendarRemindListener.REMIND_ERROR);
              }
              return;
            }
            // 删除提醒成功
            if (null != callback) {
              callback.onSuccess();
            }
          }
        }
      }
    } finally {
      if (eventCursor != null) {
        eventCursor.close();
      }
    }
  }

  /**
   * 日历提醒添加成功与否监控器
   */
  public interface onCalendarRemindListener {
    int CALENDAR_ERROR = 0;
    int EVENT_ERROR = 1;
    int REMIND_ERROR = 2;

    void onFailed(int error_code);

    void onSuccess();
  }

  /**
   * 辅助方法：获取设置时间起止时间的对应毫秒数
   * 
   * @param year
   * @param month 1-12
   * @param day 1-31
   * @param hour 0-23
   * @param minute 0-59
   * @return
   */
  public static long remindTimeCalculator(int year, int month, int day, int hour, int minute) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, day, hour, minute);
    return calendar.getTimeInMillis();
  }

}
