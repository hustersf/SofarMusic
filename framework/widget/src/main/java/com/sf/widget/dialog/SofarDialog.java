package com.sf.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.sf.widget.R;

public class SofarDialog extends Dialog {


  public SofarDialog(@NonNull Context context) {
    this(context, R.style.Theme_Dialog_Translucent);
  }

  public SofarDialog(@NonNull Context context, int themeResId) {
    super(context, themeResId);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_sofar);
  }


  @Override
  protected void onStart() {
    super.onStart();
  }


  @Override
  protected void onStop() {
    super.onStop();
  }
}
