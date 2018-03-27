package com.yk.onedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                startActivity(new Intent(Main3Activity.this,MainActivity.class));
               break;
            case R.id.button2:
                startActivity(new Intent(Main3Activity.this,Main2Activity.class));
               break;
        }
    }
}
