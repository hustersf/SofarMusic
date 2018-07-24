package com.sf.demo.picker;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.demo.picker.city.CityPicker;
import com.sf.demo.picker.city2.CityPicker2;
import com.sf.demo.picker.date.DatePicker;
import com.sf.demo.picker.text.TextPicker;
import com.sf.demo.picker.time.TimePicker;
import com.sf.utility.ToastUtil;
import com.sf.widget.flowlayout.FlowTagList;

/**
 * Created by sufan on 17/6/20.
 * 后缀的数字相同，代表时同一种style
 */

public class PickerShowActivity extends UIRootActivity{

    private FlowTagList tag_fl;

    private String[] mTags = {"城市选择器", "文字滚轮", "日期选择器", "时间选择器",
            "城市选择器2"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_demo_show;
    }

    @Override
    protected void initTitle() {
        head_title.setText("展示滚轮组件");
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

    private void showCityPicker2(){
        CityPicker2 picker2=new CityPicker2(this,null);
        picker2.show();

    }
}
