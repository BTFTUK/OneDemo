package com.yk.onedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.yk.onedemo.utils.ActivityBarUtils;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        RelativeLayout rl_bar = (RelativeLayout) findViewById(R.id.rl_bar);
        ActivityBarUtils.setImmerseLayout(this,rl_bar);
    }
}
