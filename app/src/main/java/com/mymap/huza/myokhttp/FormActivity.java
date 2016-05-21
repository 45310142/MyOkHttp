package com.mymap.huza.myokhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Huza on 2016/4/19.
 */
public class FormActivity extends AppCompatActivity {

    private EditText et_name, et_age, et_tel, post_et_image;
    private TextView tv;

    private OkHttpUtils okHttpUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        initView();

        okHttpUtils = new OkHttpUtils();

    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_age = (EditText) findViewById(R.id.et_age);
        et_tel = (EditText) findViewById(R.id.et_tel);
        post_et_image = (EditText) findViewById(R.id.post_et_image);
        tv = (TextView) findViewById(R.id.tv);
    }


    public void btnClick(View v) {
        String name = et_name.getText().toString();
        int age = Integer.parseInt(et_age.getText().toString());
        String phone = et_tel.getText().toString();
        String image_name = post_et_image.getText().toString();
        Person person = new Person(name, age, phone);
        switch (v.getId()) {
            case R.id.get_btn:
                okHttpUtils.SubmitDataByGetJoinStr(person, this, tv);
                break;
            case R.id.post_btn:
                okHttpUtils.SubmitDataByPostKeyValue(person, this, tv);
                break;
            case R.id.post_img_btn:
                person.setImage_name(image_name);
                Log.i("Success", person.getImage_name());
                okHttpUtils.SubmitDataByPostFileKeyValue(person, this, tv);

                break;
        }
    }

}
