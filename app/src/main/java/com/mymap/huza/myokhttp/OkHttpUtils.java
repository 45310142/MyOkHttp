package com.mymap.huza.myokhttp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Huza on 2016/4/18.
 */
public class OkHttpUtils {
    // 创建okHttpClient对象
    private OkHttpClient okHttpClient;

    private Bitmap bitmap = null;
    private boolean value = true;


    public OkHttpUtils() {
        okHttpClient = new OkHttpClient();
    }

    public Bitmap getBitmap(String imagePath) throws Exception {
        // 创建Request
        final Request request = new Request.Builder().url(imagePath).build();
        // 实例化一个Call对象
        Call call = okHttpClient.newCall(request);
        // 执行Call返回Response对象
        Response response = call.execute();

        if (response.isSuccessful()) {

            //response.body().string();
            //response.body().bytes();
            return BitmapFactory.decodeStream(response.body().byteStream());
        }
        return null;

    }


    public Bitmap getBitmapAsync(String imagePath, final ImageView img, final MainActivity context) {
        // 创建Request
        Request request = new Request.Builder().url(imagePath).build();
        // 实例化一个Call对象
        Call call = okHttpClient.newCall(request);

        // 调度call
        call.enqueue(new Callback() {
            // 失败时调用
            @Override
            public void onFailure(Request request, IOException e) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap == null) {
                            img.setImageResource(R.mipmap.ic_launcher);
                        }
                    }
                });
            }

            // 成功调用, 子线程中完成
            @Override
            public void onResponse(final Response response) throws IOException {
                if (response.isSuccessful()) {
                    bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        });

        return null;
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x100) {
                //普通get返回
                return;
            }
        }
    };


    // 以get方式提交数据到服务器端(拼接字符串)
    public void SubmitDataByGetJoinStr(Person person, final FormActivity activity, final TextView tv) {
        String Post_Base_URL = "http://10.30.10.113:8080/AndroidProjectServer/GetPostServlet";
        String destUrl = Post_Base_URL + "?name=" + person.getName() + "&age=" + person.getAge() + "&tel=" + person.getPhone();
        // 创建Request对象
        final Request request = new Request.Builder().url(destUrl).build();
        // 实例化一个Call对象
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("连接服务端失败!");
                    }
                });
            }

            // 在子线程中完成
            @Override
            public void onResponse(Response response) throws IOException {
                String result = "GET提交失败!";
                if (response.isSuccessful() && "Success".equals(response.body().string())) {
                    result = "GET提交成功!";
                }
                final String finalResult = result;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(finalResult);
                    }
                });
            }
        });

    }

    //POST提交键值对
    public void SubmitDataByPostKeyValue(Person person, final FormActivity activity, final TextView tv) {

        String Post_Base_URL = "http://10.30.10.113:8080/AndroidProjectServer/GetPostServlet";

        // 创建RequestBody对象
        RequestBody formBody = new FormEncodingBuilder()
                .add("name", person.getName())
                .add("age", person.getAge() + "")
                .add("tel", person.getPhone())
                .build();

        // 创建request对象
        Request request = new Request.Builder().url(Post_Base_URL).post(formBody).build();
        // 创建call对象
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("连接服务端失败!");
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = "POST提交失败!";
                if (response.isSuccessful() && "Success".equals(response.body().string())) {
                    result = "POST提交成功!";
                }
                final String finalResult = result;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(finalResult);
                    }
                });
            }
        });

    }

    // Post提交文件与普通数据
    public void SubmitDataByPostFileKeyValue(Person person, final FormActivity activity, final TextView tv) {

        String Base_Url="http://10.30.10.113:8080/AndroidProjectServer/UpdateServlet";
        Log.i("Success",Environment.getExternalStorageDirectory().getAbsolutePath());
        File file = new File(Environment.getExternalStorageDirectory(), person.getImage_name());

        if(file.exists()){
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), file);

            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"name\""), RequestBody.create(null, person.getName()))
                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"age\""), RequestBody.create(null, person.getAge() + ""))
                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"tel\""), RequestBody.create(null, person.getPhone()))
                    .addPart(Headers.of("Content-Disposition", "form-data; name=\"image\"; filename=\"" + person.getImage_name() + "\""), fileBody)
                    .build();

            final Request request=new Request.Builder().url(Base_Url).post(requestBody).build();

            Call call=okHttpClient.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText("连接服务端失败!");
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String result = "POST提交普通数据与文件失败!";
                    if (response.isSuccessful() && "Success".equals(response.body().string())) {
                        result = "POST提交普通数据与文件成功!";
                    }
                    final String finalResult = result;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(finalResult);
                        }
                    });
                }
            });
        }
    }


}
