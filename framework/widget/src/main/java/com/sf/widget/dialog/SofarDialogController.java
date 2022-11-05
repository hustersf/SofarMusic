package com.sf.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
