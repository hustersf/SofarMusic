package com.sf.sofardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sf.base.BaseActivity;
import com.sf.demo.DemoListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, DemoListFragment.newInstance())
                .commit();
    }
}
