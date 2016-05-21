package com.mymap.huza.myokhttp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Huza on 2016/4/19.
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void btnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_down_img:
                intent.setClass(this, MainActivity.class);
                break;
            case R.id.btn_post_form:
                intent.setClass(this, FormActivity.class);
                break;
        }
        startActivity(intent);
    }
}
