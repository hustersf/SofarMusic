package com.sf.sofarmusic.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.adapter.PayListAdapter;
import com.sf.sofarmusic.adapter.PhoneListAdapter;
import com.sf.sofarmusic.enity.PayInfo;
import com.sf.sofarmusic.view.MyBottomSheetDialog;

import java.util.List;

import com.sf.utility.DeviceUtil;
import com.sf.utility.TintUtil;


/**
 * Created by sufan on 17/6/15.
 */

public class SheetDialogUtil {

    //两端取消和确定按钮，中间titile, 下面是一个列表，列表只显示文字
    public static void showPhoneList(final Context context, final String title,
                                     List<String> phoneList, final PhoneCallback callback) {

        final MyBottomSheetDialog sheetDialog = new MyBottomSheetDialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.sheet_phone_list, null);
        TextView list_tv = (TextView) view.findViewById(R.id.list_tv);
        TextView cancel_tv = (TextView) view.findViewById(R.id.cancel_tv);
        TextView confirm_tv = (TextView) view.findViewById(R.id.confirm_tv);
        cancel_tv.setVisibility(View.GONE);
        confirm_tv.setVisibility(View.GONE);


        //设置Recyclerview的高度
        int height = (int) (DeviceUtil.getMetricsHeight(context) * 0.4);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);


        RecyclerView phone_rv = (RecyclerView) view.findViewById(R.id.phone_list_rv);
        phone_rv.setLayoutParams(lp);
        phone_rv.setLayoutManager(new LinearLayoutManager(context));
        PhoneListAdapter adapter = new PhoneListAdapter(context, phoneList);
        phone_rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new PhoneListAdapter.OnItemCliclkListener() {
            @Override
            public void OnItemClick(String phone) {
                callback.OnPhone(phone);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.setContentView(view);
        sheetDialog.show();
    }


    public static void showPayList(final Context context, final String title, List<PayInfo> payList, final PayCallback callback) {
        final MyBottomSheetDialog sheetDialog = new MyBottomSheetDialog(context);

        View view = LayoutInflater.from(context).inflate(R.layout.sheet_pay_list, null);
        TextView list_tv = (TextView) view.findViewById(R.id.list_tv);
        final ImageView back_iv = (ImageView) view.findViewById(R.id.back_iv);

        TintUtil.setImageViewColor(back_iv, Color.parseColor("#A3A3A3"));
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();
            }
        });

        RecyclerView pay_rv = (RecyclerView) view.findViewById(R.id.pay_list_rv);
        pay_rv.setLayoutManager(new LinearLayoutManager(context));
        PayListAdapter adapter = new PayListAdapter(context, payList);
        pay_rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new PayListAdapter.OnItemCliclkListener() {
            @Override
            public void OnItemClick(PayInfo item) {
                callback.OnPay(item);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.setContentView(view);
        sheetDialog.show();


    }

    public interface PhoneCallback {
        void OnPhone(String phoneNum);
    }

    public interface PayCallback {
        void OnPay(PayInfo item);
    }
}
