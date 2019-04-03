package com.sf.sofarmusic.exception;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sf.sofarmusic.R;

public class CrashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_crash);

    TextView crashTv = findViewById(R.id.tv_crash);

    String crashInfo = getIntent().getStringExtra("crash");

    crashTv.setText(crashInfo);
  }
}
