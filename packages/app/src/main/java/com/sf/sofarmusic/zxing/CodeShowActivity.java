package com.sf.sofarmusic.zxing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.sf.base.UIRootActivity;
import com.sf.libzxing.util.QRCodeUtil;
import com.sf.sofarmusic.R;

/**
 * Created by sufan on 17/7/13.
 */

public class CodeShowActivity extends UIRootActivity {

  private ImageView one_code_iv, two_code_iv;


  @Override
  protected int getLayoutId() {
    return R.layout.activity_code_show;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("我的二维码");
  }

  @Override
  public void initView() {
    one_code_iv = (ImageView) findViewById(R.id.one_code_iv);
    two_code_iv = (ImageView) findViewById(R.id.two_code_iv);
  }

  @Override
  public void initData() {
    String oneStr = "123457890";
    String twoStr = "Hello,我是sofarsogood!";

    QRCodeUtil qrCodeUtil = new QRCodeUtil(this);
    one_code_iv.setImageBitmap(qrCodeUtil.CreateOneCode(oneStr));

    Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.author_head_img);
    two_code_iv.setImageBitmap(qrCodeUtil.CreateQRCode(twoStr, logo));
  }

  @Override
  public void initEvent() {

  }
}
