package com.sf.demo.picker.city;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONArray;
import com.sf.demo.R;
import com.sf.utility.AssetUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sufan on 17/6/20.
 * mPickerWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
 * mPickerWindow.setFocusable(true);
 * 以上两句保证了点击popwindow之外的地方，可以使得pop消失
 */

public class CityPicker implements PopupWindow.OnDismissListener, View.OnClickListener {

    private OnCitySelectListener mOnCitySelectListener;

    private PopupWindow mPickerWindow;

    private View mParent;
    private View mPickerView;

    private WheelRecyclerView mProvinceWheel;

    private WheelRecyclerView mCityWheel;

    private WheelRecyclerView mCountyWheel;

    private Activity mContext;

    private List<Province> mDatas;

    public CityPicker(Activity context, View parent){
        mContext = context;
        mParent = parent;

        //初始化选择器
        mPickerView = LayoutInflater.from(context).inflate(R.layout.layout_city_picker, null);
        mProvinceWheel = (WheelRecyclerView) mPickerView.findViewById(R.id.wheel_province);
        mCityWheel = (WheelRecyclerView) mPickerView.findViewById(R.id.wheel_city);
        mCountyWheel = (WheelRecyclerView) mPickerView.findViewById(R.id.wheel_county);
        mPickerView.findViewById(R.id.cancel_tv).setOnClickListener(this);
        mPickerView.findViewById(R.id.confirm_tv).setOnClickListener(this);

        mPickerWindow = new PopupWindow(mPickerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPickerWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPickerWindow.setFocusable(true);
        mPickerWindow.setAnimationStyle(R.style.CityPickerAnim);
        mPickerWindow.setOnDismissListener(this);

        initData();
    }

    private void initData(){
        String cityJson= AssetUtil.getTextFromAssets(mContext,"json/city.json");
        mDatas= JSONArray.parseArray(cityJson,Province.class);

        mProvinceWheel.setData(getProvinceNames());
        mCityWheel.setData(getCityNames(0));
        mCountyWheel.setData(getCountyNames(0, 0));

        mProvinceWheel.setOnSelectListener(new WheelRecyclerView.OnSelectListener() {
            @Override
            public void onSelect(int position, String data) {
                onProvinceWheelRoll(position);
            }
        });
        mCityWheel.setOnSelectListener(new WheelRecyclerView.OnSelectListener() {
            @Override
            public void onSelect(int position, String data) {
                onCityWheelRoll(position);
            }
        });
    }

    /**
     * 弹出Window时使背景变暗
     *
     * @param alpha
     */
    private void backgroundAlpha(float alpha) {
        Window window = mContext.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.setAttributes(lp);
    }

    private void onProvinceWheelRoll(int position) {
        mCityWheel.setData(getCityNames(position));
        mCountyWheel.setData(getCountyNames(position, 0));
    }

    private void onCityWheelRoll(int position) {
        mCountyWheel.setData(getCountyNames(mProvinceWheel.getSelected(), position));
    }

    /**
     * 获取省份名称列表
     *
     * @return
     */
    private List<String> getProvinceNames() {
        List<String> provinces = new ArrayList<>();
        for (Province province : mDatas) {
            provinces.add(province.areaName);
        }
        return provinces;
    }

    /**
     * 获取某个省份的城市名称列表
     *
     * @param provincePos
     * @return
     */
    private List<String> getCityNames(int provincePos) {
        List<String> cities = new ArrayList<>();
        for (City city : mDatas.get(provincePos).cities) {
            cities.add(city.areaName);
        }
        return cities;
    }

    /**
     * 获取某个城市的县级区域名称列表
     *
     * @param provincePos
     * @param cityPos
     * @return
     */
    private List<String> getCountyNames(int provincePos, int cityPos) {
        List<String> counties = new ArrayList<>();
        if (mDatas.get(provincePos).cities.size() > 0) {
            for (County county : mDatas.get(provincePos).cities.get(cityPos).counties) {
                counties.add(county.areaName);
            }
        }
        return counties;
    }


    public CityPicker setOnCitySelectListener(OnCitySelectListener listener) {
        mOnCitySelectListener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel_tv) {
            mPickerWindow.dismiss();

        } else if (i == R.id.confirm_tv) {
            if (mOnCitySelectListener != null) {
                Province province = mDatas.get(mProvinceWheel.getSelected());
                City city = province.cities.size() > 0 ? province.cities.get(mCityWheel.getSelected()) : null;
                String provinceName = province.areaName;
                String cityName = city == null ? "" : city.areaName;
                String countyName = city == null ? "" : city.counties.get(mCountyWheel.getSelected()).areaName;
                mOnCitySelectListener.onCitySelect(provinceName, cityName, countyName);
                mPickerWindow.dismiss();
            }

        }
    }

    public void show() {
        backgroundAlpha(0.5f);
        if(mParent!=null) {
            mPickerWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        }else{
            mPickerWindow.showAtLocation(mPickerView, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }

    public interface OnCitySelectListener {
        void onCitySelect(String province, String city, String county);
    }
}
