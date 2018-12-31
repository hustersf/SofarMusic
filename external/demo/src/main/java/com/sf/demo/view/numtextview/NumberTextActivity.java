package com.sf.demo.view.numtextview;

import com.sf.base.UIRootActivity;
import com.sf.demo.R;
import com.sf.widget.textview.NumberAnimTextView;

/**
 * Created by sufan on 17/7/11.
 */

public class NumberTextActivity extends UIRootActivity {

  private NumberAnimTextView number1_tv, number2_tv, number3_tv;


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
    number1_tv = (NumberAnimTextView) findViewById(R.id.number1_tv);
    number2_tv = (NumberAnimTextView) findViewById(R.id.number2_tv);
    number3_tv = (NumberAnimTextView) findViewById(R.id.number3_tv);

  }

  @Override
  public void initData() {

    number1_tv.setDuration(2000);
    number1_tv.setNumberString("99.7588");

    number2_tv.setDuration(1000);
    number2_tv.setNumberString("123456");
    number2_tv.setPostfixString("%");


    number3_tv.setNumberString("99999.99");
    number3_tv.setPrefixString("￥");
    number3_tv.setDuration(1000);

  }

  @Override
  public void initEvent() {

  }
}
