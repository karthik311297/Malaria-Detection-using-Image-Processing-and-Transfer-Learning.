package com.example.karthik.testuploadtotomcat;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class ImageViewerActivty extends AppCompatActivity {
    Intent come;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer_activty);
        come=getIntent();
        if(!come.getStringExtra("NAME").equals("")) {
            getImageFromServer(come.getStringExtra("NAME"));
        }
        }
    public void getImageFromServer(String cname){
        String geturl="http://192.168.1.11:8080/iyer/SpecificCellServlet?name="+cname;
        Request request=new Request.Builder().url(geturl).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ImageViewerActivty.this,"Failed to fetch image",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                InputStream inputStream=response.body().byteStream();
                final Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ImageView obtainedCell=findViewById(R.id.obtainedCell);
                        obtainedCell.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
}
