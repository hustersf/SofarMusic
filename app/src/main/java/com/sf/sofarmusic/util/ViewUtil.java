package com.sf.sofarmusic.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**  
 * 自定义布局工具类
* @author 田裕杰 
* @version 1.0.0 
*/
public class ViewUtil {
	private static int BtnColor = Color.parseColor("#007aff");

	/**
	 * 获取自定义dialog View布局
	 * @param context 上下文
	 * @param title 标题
	 * @param msg 信息
	 * @param submitStr 确认按钮信息
	 * @param cancelStr 取消按钮信息
	 * @param hasCancel 是否包含取消按钮
	 * @param listener 按钮点击事件监听
	 * @return
	 */
	public static LinearLayout getAlertView(Context context, String title,
			String msg, String submitStr, String cancelStr, Boolean hasCancel,
			final onAlertClickListener listener) {
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
		layout.setBackgroundDrawable(gd);
		// 标题 view
		TextView title_tv = new TextView(context);
		LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		title_tv.setLayoutParams(titleParams);
		title_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		title_tv.setPadding(0, dip2px(context, 20), 0, dip2px(context, 20));
		title_tv.getPaint().setFakeBoldText(true);
		title_tv.setTextColor(Color.BLACK);
		title_tv.setTextSize(22);
		title_tv.setText(title);
		// 横线view
		View lien_h = new View(context);
		lien_h.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		lien_h.setBackgroundColor(Color.BLACK);
		// 内容view
		TextView msg_tv = new TextView(context);
		LayoutParams msgParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		msgParams.setMargins(dip2px(context, 20), dip2px(context, 5),
				dip2px(context, 20), dip2px(context, 30));
		msgParams.gravity = Gravity.CENTER_HORIZONTAL;
		msg_tv.setLayoutParams(msgParams);
		msg_tv.setTextColor(Color.BLACK);
		msg_tv.setTextSize(18);
		msg_tv.setText(msg);
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
		submit.setPadding(0, dip2px(context, 12), 0, dip2px(context, 12));
		submit.setTextColor(BtnColor);
		submit.setTextSize(20);
		submit.setText(submitStr);
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onPositive();
				}
			}
		});
		setControlBg(submit);
		if (hasCancel) {
			// 竖线view
			View line_v = new View(context);
			line_v.setBackgroundColor(Color.BLACK);
			line_v.setLayoutParams(new LayoutParams(1,
					LayoutParams.MATCH_PARENT));
			// 取消按钮view
			TextView cancel = new TextView(context);
			cancel.setLayoutParams(buttonParams);
			cancel.setGravity(Gravity.CENTER_HORIZONTAL);
			cancel.setPadding(0, dip2px(context, 12), 0, dip2px(context, 12));
			cancel.setTextColor(BtnColor);
			cancel.setTextSize(20);
			cancel.setText(cancelStr);
			cancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (listener != null) {
						listener.onNegative();
					}
				}
			});
			setControlBg(cancel);
			buttonLL.addView(submit);
			buttonLL.addView(line_v);
			buttonLL.addView(cancel);
		} else {
			buttonLL.addView(submit);
		}

		layout.addView(title_tv);
		layout.addView(msg_tv);
		layout.addView(lien_h);
		layout.addView(buttonLL);
		return layout;
	}
	public static LinearLayout getDownLoadView(Context context, String title,View view,String submitStr,
			final onAlertClickListener listener) {
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
		layout.setBackgroundDrawable(gd);
		// 标题 view
		TextView title_tv = new TextView(context);
		LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		title_tv.setLayoutParams(titleParams);
		title_tv.setGravity(Gravity.CENTER_HORIZONTAL);
		title_tv.setPadding(0, dip2px(context, 20), 0, dip2px(context, 20));
		title_tv.getPaint().setFakeBoldText(true);
		title_tv.setTextColor(Color.BLACK);
		title_tv.setTextSize(22);
		title_tv.setText(title);
		// 横线view
		View lien_h = new View(context);
		lien_h.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		lien_h.setBackgroundColor(Color.BLACK);
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
		submit.setPadding(0, dip2px(context, 12), 0, dip2px(context, 12));
		submit.setTextColor(BtnColor);
		submit.setTextSize(20);
		submit.setText(submitStr);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onPositive();
				}
			}
		});
		setControlBg(submit);
		buttonLL.addView(submit);
		
		layout.addView(title_tv);
		layout.addView(view);
		if (listener!=null) {
			layout.addView(lien_h);
			layout.addView(buttonLL);
		}
		return layout;
	}

	private static int dip2px(Context activity, float dipValue) {
		float m = activity.getResources().getDisplayMetrics().density;
		return (int) (dipValue * m + 0.5f);
	}

	/**
	 * 获取手机屏幕尺寸 宽度
	 * 
	 * @param context
	 *            上下文
	 * @return int
	 */
	private static int getMetricsWidth(Context context) {
		// String str = "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;// 屏幕高（像素，如：800px）
		return screenWidth;
	}

	/**
	 * 设置view点击效果
	 * 
	 * @param view
	 */
	private static void setControlBg(final View view) {
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
	public static GradientDrawable getFilletDrawable(Context context,int color,float upRadius,float DownRadius){
		GradientDrawable gd = new GradientDrawable();// 创建drawable   圆角
		gd.setColor(color);
		upRadius = dip2px(context,upRadius);
		DownRadius = dip2px(context,DownRadius);
		float[] Radius = {upRadius,upRadius,upRadius,upRadius,DownRadius,DownRadius,DownRadius,DownRadius};
		gd.setCornerRadii(Radius);
		gd.setStroke(1, Color.parseColor("#2E3135"));
		return gd;
	}
	/**
	 * 从Assets文件夹中读取图片
	 * @param context 上下文对象
	 * @param fileName 文件路径
	 * @return Bitmap 图片对象
	 */
	public static Bitmap getImageFromAssets(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;

	}
	/**
	 *  设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果
	 * @param drawable
	 *            图片对象
	 * @return StateListDrawable
	 * */
	public static StateListDrawable createSLD(Drawable drawable) {
		StateListDrawable bg = new StateListDrawable();
		Paint p = new Paint();
		p.setColor(0x40222222); // Paint ARGB色值，A = 0x40 不透明。RGB222222 暗色
		Drawable normal = drawable;
		Drawable pressed = createDrawable(drawable, p);
		bg.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, pressed);
		bg.addState(new int[] { android.R.attr.state_enabled }, normal);
		bg.addState(new int[] {}, normal);
		return bg;
	}
	public static StateListDrawable createSLD(Drawable defaultDb,Drawable pressedDb) {
		StateListDrawable bg = new StateListDrawable();
		Paint p = new Paint();
		p.setColor(0x40222222); // Paint ARGB色值，A = 0x40 不透明。RGB222222 暗色
		bg.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, pressedDb);
		bg.addState(new int[] { android.R.attr.state_enabled }, defaultDb);
		bg.addState(new int[] {}, defaultDb);
		return bg;
	}
	/**
	 *  通过画笔从新绘制图片
	 * @param d
	 *            图片对象
	 * @param p
	 *            画笔
	 * @return Drawable
	 * */
	private static Drawable createDrawable(Drawable d, Paint p) {
		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，
		return new BitmapDrawable(bitmap);
	}


	public interface onAlertClickListener{
		/**
		 * 确认
		 */
		public void onPositive();
		/**
		 * 取消
		 */
		public void onNegative();
	}
}
