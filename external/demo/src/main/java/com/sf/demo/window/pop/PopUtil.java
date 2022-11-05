package com.sf.demo.window.pop;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sf.demo.R;
import com.sf.utility.DensityUtil;
import com.sf.utility.DeviceUtil;


/**
 * Created by sufan on 17/6/22.
 */

public class PopUtil {

    public static void showPopList(Activity context, List<PopInfo> popList) {

        int width = DensityUtil.dp2px(context, 120);
        int height = DensityUtil.dp2px(context, 40) * popList.size() + DensityUtil.dp2px(context, 10);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_list, null);
        final PopupWindow popupWindow = new PopupWindow(view, width, height, true);// 为false表示点击popwindow外面不可消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // true加这句表示点击popwindow可消失

        int head = DensityUtil.dp2px(context, 50 - 10);
        int x = DensityUtil.dp2px(context, 10);
        int y = DeviceUtil.getStatusBarHeight(context) + head;
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, x, y);

        RecyclerView pop_rv = (RecyclerView) view.findViewById(R.id.pop_rv);
        pop_rv.setLayoutManager(new LinearLayoutManager(context));

        PopListAdapter adapter = new PopListAdapter(context, popList);
        pop_rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new PopListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, PopInfo item) {
                popupWindow.dismiss();
            }
        });

    }

    public static void showBottomPop(final Activity context, final List<String> datas, final PopCallback callback) {
        int width = DeviceUtil.getMetricsWidth(context)-DensityUtil.dp2px(context,30);
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_ios_bottom, null);
        final PopupWindow popupWindow = new PopupWindow(view, width, height, true);// 为false表示点击popwindow外面不可消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // true加这句表示点击popwindow可消失

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        TextView cancel_tv = (TextView) view.findViewById(R.id.cancel_tv);
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        LinearLayout list_ll = (LinearLayout) view.findViewById(R.id.list_ll);

        for (int i = 0; i < datas.size(); i++) {
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(context, 50));
            textViewParams.gravity = Gravity.CENTER;  //相当于android:layout_gravity
            textView.setLayoutParams(textViewParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#0076FF"));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            list_ll.addView(textView);

            final String text=datas.get(i);
            textView.setText(text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backgroundAlpha(context,1.0f);
                    popupWindow.dismiss();
                    callback.onText(text);
                }
            });

            if (i != datas.size() - 1) {
                ImageView imageView=new ImageView(context);
                LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,DensityUtil.dp2px(context,1));
                imageView.setLayoutParams(imageViewParams);
                imageView.setBackgroundColor(Color.parseColor("#E5E5E5"));
                list_ll.addView(imageView);
            }
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(context,1.0f);
            }
        });

        backgroundAlpha(context,0.5f);

    }

    public interface PopCallback {
        void onText(String str);
    }


    private static void backgroundAlpha(Activity context,float alpha) {
        Window window =context.getWindow() ;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.setAttributes(lp);
    }
}
