package com.sf.sofarmusic.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.sf.sofarmusic.R;
import com.sf.sofarmusic.enity.MenuItem;
import com.sf.sofarmusic.menu.PopMenuAdapter;

import java.util.List;
import com.sf.utility.DeviceUtil;
import com.sf.utility.DensityUtil;

/**
 * Created by sufan on 16/11/24.
 */

public class PopUtil {

    public static void showMenuPop(final Context context, final List<MenuItem> menuList) {

        int width = DensityUtil.dp2px(context, 200);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pop_menu, null);
        final PopupWindow popupWindow = new PopupWindow(view, width,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);// 为false表示点击popwindow外面不可消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // true加这句表示点击popwindow可消失
        int x = DensityUtil.dp2px(context, 0);
        int y = DeviceUtil.getStatusBarHeight(context);
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, x, y);

        RecyclerView pop_rv = (RecyclerView) view.findViewById(R.id.pop_rv);
        pop_rv.setLayoutManager(new LinearLayoutManager(context));
        PopMenuAdapter adapter = new PopMenuAdapter(context, menuList);
        pop_rv.setAdapter(adapter);

    }
}
