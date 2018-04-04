package com.sf.sofarmusic.demo.picker.date1;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.sf.sofarmusic.util.ViewUtil;

public class DatePickerDialog {
	private Context context;
	private AlertDialog dialog;
	private Display display;
	private int BtnColor = Color.parseColor("#007aff");

	public DatePickerDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public DatePickerDialog builder(String date, final DateCallBack callBack) {
		// 定义Dialog布局和参数
		// dialog = new Dialog(context,R.style.dialog_style);
		// dialog.setTitle("日期选择");
		// dialog.setContentView(view);
		dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		Window window = dialog.getWindow();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.width = getMetricsWidth(context) * 17 / 20;
		dialog.getWindow().setAttributes(params);
		// 最外层布局view
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(layoutParams);
		// int strokeWidth = 5; // 3dp 边框宽度
		int roundRadius = dip2px(context, 5); // 8dp 圆角半径
		// int strokeColor = Color.parseColor("#2E3135");//边框颜色
		int fillColor = Color.WHITE;// 内部填充颜色
		// int fillColor = Color.parseColor("#DFDFE0");//内部填充颜色
		GradientDrawable gd = new GradientDrawable();// 创建drawable
		gd.setColor(fillColor);
		gd.setCornerRadius(roundRadius);
		// gd.setStroke(strokeWidth, strokeColor);
//		layout.setBackgroundDrawable(gd);
		layout.setBackgroundDrawable(ViewUtil.getFilletDrawable(context, fillColor, 5, 5));
		
		// 标题 view
		TextView title_tv = new TextView(context);
		LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		title_tv.setLayoutParams(titleParams);
		title_tv.setBackgroundColor(Color.parseColor("#e5e5e5"));
		title_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		title_tv.setPadding(0, dip2px(context, 20), 0, dip2px(context, 20));
		title_tv.getPaint().setFakeBoldText(true);
		title_tv.setTextColor(Color.BLACK);
		title_tv.setTextSize(22);
		title_tv.setText("日期选择");
		title_tv.setBackgroundDrawable(ViewUtil.getFilletDrawable(context, fillColor, 5, 0));
		// 横线view
		View lien_h = new View(context);
		lien_h.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		lien_h.setBackgroundColor(Color.BLACK);
		// 内容view
		
		final DatePickerView pickerview = new DatePickerView(context);
		pickerview.setBeginDate(date);
		LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		pickerview.setLayoutParams(contentParams);
		// button组合view
		LinearLayout buttonLL = new LinearLayout(context);
		LayoutParams btnllParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		buttonLL.setLayoutParams(btnllParams);
		buttonLL.setOrientation(LinearLayout.HORIZONTAL);

		LayoutParams buttonParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, 1.0f);
		// 确认按钮view
		final TextView submit = new TextView(context);
		submit.setLayoutParams(buttonParams);
		submit.setGravity(Gravity.CENTER_HORIZONTAL);
		submit.setPadding(0, dip2px(context, 15), 0, dip2px(context, 15));
		submit.setTextColor(BtnColor);
		submit.setTextSize(20);
		submit.setText("确定");
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				callBack.onDate(pickerview.getDate());
			}
		});
		setControlBg(submit);
		// 竖线view
		View line_v = new View(context);
		line_v.setBackgroundColor(Color.BLACK);
		line_v.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
		// 取消按钮view
		TextView cancel = new TextView(context);
		cancel.setLayoutParams(buttonParams);
		cancel.setGravity(Gravity.CENTER_HORIZONTAL);
		cancel.setPadding(0, dip2px(context, 15), 0, dip2px(context, 15));
		cancel.setTextColor(BtnColor);
		cancel.setTextSize(20);
		cancel.setText("取消");
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		setControlBg(cancel);
		buttonLL.addView(submit);
		buttonLL.addView(line_v);
		buttonLL.addView(cancel);

		layout.addView(title_tv);
		layout.addView(pickerview);
		layout.addView(lien_h);
		layout.addView(buttonLL);
		window.setContentView(layout);
		return this;
	}

	private int dip2px(Context context, float dipValue) {
		float m = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * m + 0.5f);
	}

	/**
	 * 设置view点击效果
	 * 
	 * @param view
	 */
	private void setControlBg(final View view) {
		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					view.setBackgroundColor(Color.parseColor("#e7e7e7"));
					break;
				case MotionEvent.ACTION_UP:
					view.setBackgroundColor(Color.WHITE);
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	/**
	 * 获取手机屏幕尺寸 宽度
	 * 
	 * @param context
	 *            上下文
	 * @return int
	 */
	private int getMetricsWidth(Context context) {
		// String str = "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;// 屏幕高（像素，如：800px）
		return screenWidth;
	}

	public void dismiss() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public void show() {
		dialog.show();
	}
	public interface DateCallBack{
		public void onDate(String date);
	}
}
