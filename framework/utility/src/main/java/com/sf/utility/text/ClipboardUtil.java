package com.sf.utility.text;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

public class ClipboardUtil {


  public static void copy(String content, Context context) {
    // 得到剪贴板管理器
    ClipboardManager cbm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);;
    ClipData data = ClipData.newPlainText("", content);
    cbm.setPrimaryClip(data);
  }

  public static String paste(Context context) {
    // 得到剪贴板管理器
    ClipboardManager cbm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData data = cbm.getPrimaryClip();
    if (data == null || data.getItemCount() == 0) {
      return "";
    }

    ClipData.Item item = data.getItemAt(0);
    if (TextUtils.isEmpty(item.getText())) {
      return "";
    }
    return item.getText().toString();
  }
}
