package com.sf.sofarmusic.demo.zxing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sf.libzxing.util.QRCodeUtil;
import com.sf.sofarmusic.R;
import com.sf.sofarmusic.demo.DemoActivity;
import com.sf.sofarmusic.util.FontUtil;

/**
 * Created by sufan on 17/7/13.
 */

public class CodeShowActivity extends DemoActivity {

    private TextView head_back, head_title, head_right;
    private Toolbar toolbar;

    private ImageView one_code_iv,two_code_iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_code_show);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initHead() {
        head_back = (TextView) findViewById(R.id.head_back);
        head_title = (TextView) findViewById(R.id.head_title);
        head_right = (TextView) findViewById(R.id.head_right);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dynamicAddView(toolbar, "background", R.color.head_title_bg_color);

        //设置字体
        Typeface iconfont = FontUtil.setFont(this);
        head_back.setTypeface(iconfont);
        head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        head_title.setText("我的二维码");

        head_right.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        one_code_iv=(ImageView)findViewById(R.id.one_code_iv);
        two_code_iv=(ImageView)findViewById(R.id.two_code_iv);
    }

    @Override
    public void initData() {
        String oneStr="123457890";
        String twoStr="Hello,我是sofarsogood!";

        QRCodeUtil qrCodeUtil=new QRCodeUtil(this);
        one_code_iv.setImageBitmap(qrCodeUtil.CreateOneCode(oneStr));

        Bitmap logo= BitmapFactory.decodeResource(getResources(),R.drawable.author_head_img);
        two_code_iv.setImageBitmap(qrCodeUtil.CreateQRCode(twoStr,logo));
    }

    @Override
    public void initEvent() {

    }
}
