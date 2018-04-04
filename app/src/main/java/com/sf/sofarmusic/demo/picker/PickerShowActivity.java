package com.sf.sofarmusic.demo.picker;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.demo.picker.city.CityPicker;
import com.sf.sofarmusic.demo.picker.city2.CityPicker2;
import com.sf.sofarmusic.demo.picker.date.DatePicker;
import com.sf.sofarmusic.demo.picker.date1.DatePickerDialog;
import com.sf.sofarmusic.demo.picker.text.TextPicker;
import com.sf.sofarmusic.demo.picker.time.TimePicker;
import com.sf.sofarmusic.util.DateUtil;
import com.sf.sofarmusic.util.FontUtil;
import com.sf.sofarmusic.util.ToastUtil;
import com.sf.sofarmusic.view.FlowTagList;

import java.text.SimpleDateFormat;

/**
 * Created by sufan on 17/6/20.
 * 后缀的数字相同，代表时同一种style
 */

public class PickerShowActivity extends DemoActivity{
    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private FlowTagList tag_fl;

    private String[] mTags={"城市选择器","文字滚轮","日期选择器","时间选择器",
                           "日期选择器1","城市选择器2"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_demo_show);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initHead() {
        head_back = (TextView) findViewById(R.id.head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        head_right = (TextView) findViewById(R.id.head_right);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dynamicAddView(toolbar, "background", R.color.head_title_bg_color);

        //设置字体
        Typeface iconfont = FontUtil.setFont(this);
        head_back.setTypeface(iconfont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head_title.setText("展示滚轮组件");

        head_right.setVisibility(View.GONE);


    }

    @Override
    public void initView() {
        tag_fl = (FlowTagList) findViewById(R.id.tag_fl);
        dynamicAddView(tag_fl, "tagColor", R.color.themeColor);
    }

    @Override
    public void initData() {
        tag_fl.setTags(mTags);
    }

    @Override
    public void initEvent() {
        tag_fl.setOnTagClickListener(new FlowTagList.OnTagClickListener() {
            @Override
            public void OnTagClick(String text, int position) {
                for (int i = 0; i < mTags.length; i++) {
                    if (i == position) {
                        tag_fl.setChecked(true, position);
                    } else {
                        tag_fl.setChecked(false, i);
                    }
                }
                tag_fl.notifyAllTag();

                doTag(text,position);
            }
        });

    }

    private void doTag(String text,int position){
        if("城市选择器".equals(text)){
            showCityPicker();
        }else if("文字滚轮".equals(text)){
            showTextPicker();
        }else if("日期选择器".equals(text)){
           showDatePicker();
        }else if("时间选择器".equals(text)){
           showTimePicker();
        }else if("日期选择器1".equals(text)){
            showDatePicker1();
        }else if("城市选择器2".equals(text)){
           showCityPicker2();
        }
    }



    private void showCityPicker(){
        CityPicker cityPicker=new CityPicker(this,null);
        cityPicker.show();
        cityPicker.setOnCitySelectListener(new CityPicker.OnCitySelectListener() {
            @Override
            public void onCitySelect(String province, String city, String county) {
                ToastUtil.startShort(baseAt,province+city+county);
            }
        });
    }

    private void showTextPicker(){
        TextPicker textPicker=new TextPicker(this,null);
        textPicker.show();
        textPicker.setOnTextSelectListener(new TextPicker.OnTextSelectListener() {
            @Override
            public void onTextSelect(String text) {
                ToastUtil.startShort(baseAt,text);
            }
        });
    }

    private void showDatePicker(){
        DatePicker datePicker=new DatePicker(this,null);
        datePicker.show();
        datePicker.setOnDateSelectListener(new DatePicker.OnDateSelectListener() {
            @Override
            public void onDateSelect(String year, String month, String day) {
                ToastUtil.startShort(baseAt,year+"-"+month+"-"+day);
            }
        });

    }

    private void showTimePicker(){
        TimePicker timePicker=new TimePicker(this,null);
        timePicker.show();
        timePicker.setOnTimeSelectListener(new TimePicker.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(String hour, String min) {
                ToastUtil.startShort(baseAt,hour+":"+min);
            }
        });
    }

    private void showDatePicker1(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String date= DateUtil.getCurrentTimeInString(dateFormat);
        DatePickerDialog datePickerDialog = new DatePickerDialog(baseAt);
        datePickerDialog.builder(date, new DatePickerDialog.DateCallBack() {

            @Override
            public void onDate(final String date) {
               ToastUtil.startShort(baseAt,date);

            }
        });
    }

    private void showCityPicker2(){
        CityPicker2 picker2=new CityPicker2(this,null);
        picker2.show();

    }
}
