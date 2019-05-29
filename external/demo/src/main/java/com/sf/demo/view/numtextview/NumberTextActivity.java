package com.sf.demo.view.numtextview;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.widget.textview.NumberAnimTextView;

/**
 * Created by sufan on 17/7/11.
 */

public class NumberTextActivity extends UIRootActivity {

  private NumberAnimTextView number1_tv, number2_tv, number3_tv, number4_tv;


  @Override
  protected int getLayoutId() {
    return R.layout.activity_number_textview;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("滚动数字");
  }

  @Override
  public void initView() {
    number1_tv = findViewById(R.id.number1_tv);
    number2_tv = findViewById(R.id.number2_tv);
    number3_tv = findViewById(R.id.number3_tv);
    number4_tv = findViewById(R.id.number4_tv);
  }

  @Override
  public void initData() {

    number1_tv.setDuration(2000);
    number1_tv.startNumberAnim("99.7588");

    number2_tv.setDuration(1000);
    number2_tv.setFormatInt(true);
    number2_tv.setPostfixString("%");
    number2_tv.startNumberAnim("123456");


    number3_tv.setDuration(1000);
    number3_tv.setPrefixString("￥");
    number3_tv.startNumberAnim("99999.99");

    number4_tv.setDuration(900);
    number4_tv.startNumberAnim("16000", "18000");

  }

  @Override
  public void initEvent() {

  }
}
