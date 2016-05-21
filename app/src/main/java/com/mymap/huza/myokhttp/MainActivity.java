package com.mymap.huza.myokhttp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView img;
    private OkHttpUtils okHttpUtils;

    private static final String IMAGE_PATH_ONE="https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_ca79a146.png";
    private static final String IMAGE_PATH_TWO="http://a.hiphotos.baidu.com/image/pic/item/0d338744ebf81a4c29870b98d52a6059252da62e.jpg";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x200) {
                img.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
        okHttpUtils = new OkHttpUtils();
    }

    public void btnClick(View v) {
        if (v.getId() == R.id.down) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bitmap bitmap = okHttpUtils.getBitmap(IMAGE_PATH_ONE);
                        Message msg = handler.obtainMessage(0x200);
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        } else if (v.getId() == R.id.down2) {
            okHttpUtils.getBitmapAsync(IMAGE_PATH_TWO,img,this);
        }
    }
}
