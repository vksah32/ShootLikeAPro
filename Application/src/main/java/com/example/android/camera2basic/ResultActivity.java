package com.example.android.camera2basic;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        super.onCreate(savedInstanceState);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra("EXTRA_MESSAGE");

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textResult);
        textView.setText(message);
    }

    public void post(final String filepath){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    postImage(filepath);
                } catch (Exception e){
                    Log.d("DEBUG", "postImage Error: "+e.getMessage());
                }
            }
        });
    }
    public void postImage(String filepath) throws Exception {//

        MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

        OkHttpClient client = new OkHttpClient();
        String JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjE0NjMsImlhdCI6MTUwMDE5NjU2OSwiZXhwIjoxNTA3OTcyNTY5fQ.magroIZ9OuB8Clc73FbNBVHQNwngGSb51Q_dEGJKV_0";


        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "new.jpg",
                        RequestBody.create(MEDIA_TYPE_JPEG, new File(filepath)))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "JWT " + JWT_TOKEN)
                .url("http://cl-api.vize.it/1413")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();

//            client.newCall(request).execute(new Callback() {
//
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    Log.d(TAG, "postImage: "+response.body().string());
//                    sendMessage();
//
//                }
//            });
//            return "";
        if (!response.isSuccessful()){
            throw new IOException("Unexpected code " + response);
        } else {
            Log.d("DEBUG", "postImage: "+response.body().string());
            System.out.println(response.body().string());
        }

    }
}
