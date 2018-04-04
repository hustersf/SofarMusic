package com.sf.sofarmusic.demo.picker.city2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.picker.city.City;
import com.sf.sofarmusic.demo.picker.city.County;
import com.sf.sofarmusic.demo.picker.city.Province;
import com.sf.sofarmusic.util.AssetUtil;
import com.sf.sofarmusic.util.DensityUtil;
import com.sf.sofarmusic.util.ToastUtil;
import com.sf.sofarmusic.view.MyBottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/6/21.
 */

public class CityPicker2 implements View.OnClickListener {

    private Context mContext;
    private View mParent;
    private View mPickerView;
    private MyBottomSheetDialog mSheetDialog;

    private List<Province> mDatas;   //存储所有数据
    private List<City> mData1s;     //存储选中的省下面的城市数据

    private List<TextItem> mProvinceList;
    private List<TextItem> mCityList;
    private List<TextItem> mCountyList;

    private TextListAdapter mAdapter;
    private RecyclerView data_rv;

    private TextView province_tv, city_tv, county_tv;
    private View province_line, city_line, county_line;

    private final int SELECT_TEXT_COLOR = Color.parseColor("#2ED4A9");
    private final int UN_SELECT_TEXT_COLOR = Color.parseColor("#333333");

    //当前选中的省/市/区
    private String selectProvince;
    private String selectCity;
    private String selectCounty;

    //当前选中的省/市/区的位置
    private int selectProvincePosition=-1;
    private int selectCityPosition=-1;
    private int selectCountyPosition=-1;


    //上一次选中的省/市/区
    private String preSelectProvince;
    private String preSelectCity;
    private String preSelectCounty;


    public CityPicker2(Activity context, View parent) {
        mContext = context;
        mParent = parent;

        //初始化选择器
        mSheetDialog = new MyBottomSheetDialog(mContext);
        mPickerView = LayoutInflater.from(mContext).inflate(R.layout.layout_city_picker2, null);
        mPickerView.findViewById(R.id.province_rl).setOnClickListener(this);
        mPickerView.findViewById(R.id.city_rl).setOnClickListener(this);
        mPickerView.findViewById(R.id.county_rl).setOnClickListener(this);
        mPickerView.findViewById(R.id.confirm_tv).setOnClickListener(this);

        province_tv = (TextView) mPickerView.findViewById(R.id.province_tv);
        city_tv = (TextView) mPickerView.findViewById(R.id.city_tv);
        county_tv = (TextView) mPickerView.findViewById(R.id.county_tv);

        province_line = (View) mPickerView.findViewById(R.id.province_line);
        city_line = (View) mPickerView.findViewById(R.id.city_line);
        county_line = (View) mPickerView.findViewById(R.id.county_line);

        data_rv = (RecyclerView) mPickerView.findViewById(R.id.data_rv);
        //设置Recyclerview的高度，实验发现设置固定高度，当数据项比较多时，导致最后一行看不到
//        int height = (int) (DeviceUtil.getMetricsHeight(context) * 0.6);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
//        data_rv.setLayoutParams(lp);
        data_rv.setLayoutManager(new LinearLayoutManager(context));


        initData();
        initParams();
    }


    private void initData() {
        String cityJson = AssetUtil.getTextFromAssets(mContext, "json/city.json");
        mDatas = JSONArray.parseArray(cityJson, Province.class);

        mCityList = new ArrayList<>();
        mCountyList = new ArrayList<>();

        mProvinceList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            TextItem item = new TextItem();
            item.text = mDatas.get(i).areaName;
            mProvinceList.add(item);
        }
        mAdapter = new TextListAdapter(mContext, mProvinceList);
        data_rv.setAdapter(mAdapter);
    }

    private void initParams() {

        selectPos(0);

        mAdapter.setOnItemClickListener(new TextListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, String text, int tag) {
                Log.i("TAG",position+" "+text+" "+tag);
                if (tag == 0) {
                    selectProvince = text;
                    selectProvincePosition=position;
                    setText(province_tv, text);
                    if (!selectProvince.equals(preSelectProvince)) {
                        setText(city_tv, "城市");
                        setText(county_tv, "区/县");
                    }
                    selectPos(0);
                    doCity();
                } else if (tag == 1) {
                    selectCity = text;
                    selectCityPosition=position;
                    setText(city_tv, text);
                    if (!selectCity.equals(preSelectCity)) {
                        setText(county_tv, "区/县");
                    }
                    selectPos(1);
                    doCounty();
                } else {
                    selectCounty = text;
                    selectCountyPosition=position;
                    setText(county_tv, text);
                    selectPos(2);
                }
            }
        });
    }


    /**
     * @param position 0省份  1城市  2县/区
     */
    private void selectPos(int position) {
        String province = province_tv.getText().toString();
        String city = city_tv.getText().toString();
        String county = county_tv.getText().toString();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                DensityUtil.dp2px(mContext, 2));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        if (position == 0) {
            lp.width = (int) province_tv.getPaint().measureText(province);
            province_line.setLayoutParams(lp);

            province_line.setVisibility(View.VISIBLE);
            city_line.setVisibility(View.GONE);
            county_line.setVisibility(View.GONE);

            province_tv.setTextColor(SELECT_TEXT_COLOR);
            city_tv.setTextColor(UN_SELECT_TEXT_COLOR);
            county_tv.setTextColor(UN_SELECT_TEXT_COLOR);
        } else if (position == 1) {
            lp.width = (int) city_tv.getPaint().measureText(city);
            city_line.setLayoutParams(lp);

            province_line.setVisibility(View.GONE);
            city_line.setVisibility(View.VISIBLE);
            county_line.setVisibility(View.GONE);

            province_tv.setTextColor(UN_SELECT_TEXT_COLOR);
            city_tv.setTextColor(SELECT_TEXT_COLOR);
            county_tv.setTextColor(UN_SELECT_TEXT_COLOR);
        } else if (position == 2) {
            lp.width = (int) county_tv.getPaint().measureText(county);
            county_line.setLayoutParams(lp);

            province_line.setVisibility(View.GONE);
            city_line.setVisibility(View.GONE);
            county_line.setVisibility(View.VISIBLE);

            province_tv.setTextColor(UN_SELECT_TEXT_COLOR);
            city_tv.setTextColor(UN_SELECT_TEXT_COLOR);
            county_tv.setTextColor(SELECT_TEXT_COLOR);
        }
    }

    private void setText(TextView textView, String text) {
        if (text.length() > 3) {
            textView.setText(text.substring(0, 3) + "...");
        } else {
            textView.setText(text);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.province_rl:
                doProvince();
                break;
            case R.id.city_rl:
                doCity();
                break;
            case R.id.county_rl:
                doCounty();
                break;
            case R.id.confirm_tv:
                showResult();
                break;

        }
    }

    private void doProvince() {
        mAdapter.updateData(mProvinceList, 0);
        selectPos(0);
        smoothPosition(selectProvincePosition);
    }

    private void doCity() {
        String province = province_tv.getText().toString().trim();
        if ("省份".equals(province)) {
            ToastUtil.startShort(mContext, "请先选择省份");
            return;
        }
        getCityFromProvince(selectProvince);
    }

    private void doCounty() {
        String city = city_tv.getText().toString().trim();
        if ("城市".equals(city)) {
            ToastUtil.startShort(mContext, "请先选择城市");
            return;
        }
        getCountyFromCity(selectCity);
    }


    //通过省查询城市数据
    private void getCityFromProvince(String name) {
        selectPos(1);
        if (name.equals(preSelectProvince)) {
            mAdapter.updateData(mCityList, 1);
            smoothPosition(selectCityPosition);
            return;
        }
        preSelectProvince = name;
        mCityList.clear();
        for (Province province : mDatas) {
            if (name.equals(province.areaName)) {
                mData1s = province.cities;
                for (City city : province.cities) {
                    TextItem item = new TextItem();
                    item.text = city.areaName;
                    mCityList.add(item);
                }
            }
        }
        mAdapter.updateData(mCityList, 1);
        smoothPosition(selectCityPosition);
    }


    //通过城市查询区/县数据
    private void getCountyFromCity(String name) {
        selectPos(2);
        if (name.equals(preSelectCity)) {
            mAdapter.updateData(mCountyList, 2);
            smoothPosition(selectCountyPosition);
            return;
        }
        preSelectCity = name;

        mCountyList.clear();
        for (City city : mData1s) {
            if (name.equals(city.areaName)) {
                for (County county : city.counties) {
                    TextItem item = new TextItem();
                    item.text = county.areaName;
                    mCountyList.add(item);
                }
            }
        }

        mAdapter.updateData(mCountyList, 2);
        smoothPosition(selectCountyPosition);
    }

    public void show() {
        mSheetDialog.setContentView(mPickerView);
        mSheetDialog.show();
    }

    private void showResult() {
        String province = province_tv.getText().toString().trim();
        String city = city_tv.getText().toString().trim();
        String county = county_tv.getText().toString().trim();

        if ("省份".equals(province)) {
            ToastUtil.startShort(mContext, "请先选择省份");
            return;
        }

        if ("城市".equals(city)) {
            ToastUtil.startShort(mContext, "请先选择城市");
            return;
        }

        if ("区/县".equals(county)) {
            ToastUtil.startShort(mContext, "请先选择区/县");
            return;
        }

        ToastUtil.startShort(mContext, selectProvince + ":" + selectCity + ":" + selectCounty);
    }


    //是当前位置在视野内
    private void smoothPosition(int position){
       if(position!=-1){
           data_rv.scrollToPosition(position+1);
       }
    }
}
