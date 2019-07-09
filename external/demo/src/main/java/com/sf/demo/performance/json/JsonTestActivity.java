package com.sf.demo.performance.json;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sf.base.UIRootActivity;
import com.sf.demo.R;

import java.util.ArrayList;
import java.util.List;

public class JsonTestActivity extends UIRootActivity {

  private static final String TAG = "JsonTestActivity";

  private EditText numEt;
  private Button gsonBtn, fastJsonBtn, numBtn, clearBtn;
  private TextView resultTv;

  private List<Person> personList = new ArrayList<>();
  private StringBuffer sb = new StringBuffer();

  @Override
  protected int getLayoutId() {
    return R.layout.activity_json_test;
  }

  @Override
  protected void initTitle() {
    mHeadTitleTv.setText("json解析对比");
  }

  @Override
  protected void initView() {
    numEt = findViewById(R.id.et_num);
    gsonBtn = findViewById(R.id.btn_gson);
    fastJsonBtn = findViewById(R.id.btn_fastjson);
    numBtn = findViewById(R.id.btn_num);
    clearBtn = findViewById(R.id.btn_clear);
    resultTv = findViewById(R.id.tv_result);
  }

  @Override
  protected void initData() {
    setPersonList();
  }

  @Override
  protected void initEvent() {
    numBtn.setOnClickListener(v -> {
      setPersonList();
    });

    gsonBtn.setOnClickListener(v -> {
      gson();
    });

    fastJsonBtn.setOnClickListener(v -> {
      fastJson();
    });

    clearBtn.setOnClickListener(v -> {
      sb.delete(0, sb.toString().length());
      resultTv.setText(sb.toString());
    });
  }

  private void setPersonList() {
    int num = Integer.valueOf(numEt.getText().toString().trim());
    personList.clear();
    for (int i = 0; i < num; i++) {
      Person person = new Person();
      if (i % 2 == 0) {
        person.sex = "男";
      } else {
        person.sex = "女";
      }
      person.name = "小" + (i + 1);
      person.age = i + 10;
      personList.add(person);
    }
    sb.append("\n");
    sb.append("开始：").append(num).append("数据生成");
    resultTv.setText(sb.toString());
  }

  private void gson() {
    // 对象转json字符串
    sb.append("\n").append("gson time:" + personList.size() + "条数据");
    long start1 = System.currentTimeMillis();
    Gson gson = new Gson();
    String jsonStr = gson.toJson(personList);
    long time1 = System.currentTimeMillis() - start1;
    sb.append("\n").append("对象转化json字符串：").append(time1);

    Log.d(TAG, "gson:" + jsonStr);
    // json字符串转对象
    long start2 = System.currentTimeMillis();
    List<Person> list = gson.fromJson(jsonStr, List.class);
    long time2 = System.currentTimeMillis() - start2;
    sb.append("\n").append("json字符串转对象：").append(time2);
    sb.append("\t(" + list.size() + "条数据)");
    resultTv.setText(sb.toString());
  }

  private void fastJson() {
    // 对象转json字符串
    sb.append("\n").append("fastjson time:" + personList.size() + "条数据");
    long start1 = System.currentTimeMillis();
    String jsonStr = JSON.toJSONString(personList);
    long time1 = System.currentTimeMillis() - start1;
    sb.append("\n").append("对象转化json字符串：").append(time1);

    Log.d(TAG, "fastJson:" + jsonStr);
    // json字符串转对象
    long start2 = System.currentTimeMillis();
    List<Person> list = JSON.parseObject(jsonStr, List.class);
    long time2 = System.currentTimeMillis() - start2;
    sb.append("\n").append("json字符串转对象：").append(time2);
    sb.append("\t(" + list.size() + "条数据)");

    resultTv.setText(sb.toString());
  }
}
