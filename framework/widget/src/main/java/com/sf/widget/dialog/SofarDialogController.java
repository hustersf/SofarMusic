package com.sf.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

public class SofarDialogController {

  public static void show(@NonNull AppCompatActivity activity, DialogFragment dialog) {
    if (activity == null || activity.isFinishing()) {
      return;
    }
    dialog.show(activity.getSupportFragmentManager(), dialog.getTag());
  }


  public static void show(Activity activity, Dialog dialog) {
    if (activity == null || activity.isFinishing()) {
      return;
    }
    dialog.show();
  }
}
