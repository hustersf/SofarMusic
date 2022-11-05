package com.sf.demo.view.highlight.pager;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sf.demo.R;
import com.sf.demo.view.highlight.HighLightDialogFragment;
import com.sf.widget.dialog.SofarDialogController;

/**
 * Created by sufan on 17/7/28.
 */

public class Pager4 extends BasePager {

  private Button dialogBtn;

  public Pager4(Activity activity) {
    super(activity);
  }

  @Override
  public View initView() {
    View view = View.inflate(mActivity, R.layout.fragment_tab4, null);
    dialogBtn = view.findViewById(R.id.btn_dialog);
    return view;
  }

  public void initData() {
    dialogBtn.setOnClickListener(v -> {
      showDialog();
    });
  }

  public void showDialog() {
    HighLightDialogFragment dialogFragment = new HighLightDialogFragment();
    SofarDialogController.show((AppCompatActivity) mActivity, dialogFragment);
  }
}
