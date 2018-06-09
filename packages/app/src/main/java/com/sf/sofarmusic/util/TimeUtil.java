package com.sf.sofarmusic.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.sf.sofarmusic.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sufan on 17/6/16.
 * md 日期和时间选择框,5.0以上的手机才是md风格
 * DatePickerDialog和TimePickerDialog都在.app包下面
 */

public class TimeUtil {



    //系统日期框
    public static void showNormalDatePicker(Context context){
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        android.app.DatePickerDialog dd=new android.app.DatePickerDialog(context, R.style.MyDatePickerDialogTheme, new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            }
        },year,month,day);

        dd.show();
    }

    //系统时间框
    public static void showNormalTimePicker(Context context){
        Calendar calendar=Calendar.getInstance();
        int hourOfDay=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        boolean is24HourView=true;
        TimePickerDialog timePickerDialog=new TimePickerDialog(context, R.style.MyDatePickerDialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        },hourOfDay,minute,is24HourView);

        timePickerDialog.show();
    }





}
