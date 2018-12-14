package com.sf.utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoardUtil {

  public static void hideKeyBoard(Context context, EditText editText) {
    InputMethodManager imm = (InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
  }

  public static void hideKeyBoard(Activity context) {
    try {
      View focus = context.getCurrentFocus();
      if (focus != null) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
            .hideSoftInputFromWindow(
                focus.getWindowToken(), 0);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * 建议给个延迟
   */
  @Deprecated
  public static void openKeyBoard(Context context, EditText editText) {
    openKeyBoard(context, editText, 0);
  }

  /**
   * 建议给个延迟
   */
  public static void openKeyBoard(final Context context, final View view, int delay) {
    if (delay > 0) {
      view.postDelayed(new Runnable() {
        @Override
        public void run() {
          openKeyBoard(context, view, 0);
        }
      }, delay);
    } else {
      try {
        InputMethodManager imm =
            (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


}
