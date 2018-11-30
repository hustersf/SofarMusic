package com.sf.demo.media.video;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.sf.base.UIRootActivity;
import com.sf.base.permission.PermissionUtil;
import com.sf.demo.R;

/**
 * Created by sufan on 2018/4/23.
 */

public class VideoActivity extends UIRootActivity {
  private Button button1, button2, button3, button4;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_media_video;
  }

  @Override
  protected void initTitle() {
    head_title.setText("视频的采集预览");
  }

  @Override
  protected void initView() {
    button1 = findViewById(R.id.button1);
    button2 = findViewById(R.id.button2);
    button3 = findViewById(R.id.button3);
    button4 = findViewById(R.id.button4);
  }

  @Override
  protected void initData() {

    String des = "拍照和摄像权限被禁止，我们需要打开权限";
    PermissionUtil.requestPermission(this, Manifest.permission.CAMERA, des, "")
        .subscribe(permission -> {
          if (!permission.granted) {
            finish();
          }
        });
  }

  @Override
  protected void initEvent() {
    button1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(baseAt, CameraSurfaceActivity.class);
        intent.putExtra("api", 1);
        startActivity(intent);

      }
    });

    button2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(baseAt, CameraTextureActivity.class);
        intent.putExtra("api", 1);
        startActivity(intent);
      }
    });

    button3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(baseAt, CameraSurfaceActivity.class);
        intent.putExtra("api", 2);
        startActivity(intent);
      }
    });

    button4.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(baseAt, CameraTextureActivity.class);
        intent.putExtra("api", 2);
        startActivity(intent);

      }
    });
  }
}
