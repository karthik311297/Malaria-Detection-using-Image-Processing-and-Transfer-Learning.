package com.example.karthik.testuploadtotomcat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    int received;
    int PICK_IMAGE_REQUEST=234;
    Bitmap bitmap;
    Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        received=0;
        chooserIntent();
        Button pastRecordsButton=findViewById(R.id.pastRecords);
        pastRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoPastViewer=new Intent(MainActivity.this,PastViewer.class);
                startActivity(gotoPastViewer);
            }
        });
        Button uploadButton=findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(received==1){
                    uploadToFlaskServer();
                }
                else{
                    Toast.makeText(MainActivity.this,"Choose File First",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button recordButton=findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView predictedOutput=findViewById(R.id.responseText);
                String pout=predictedOutput.getText().toString();
                if(pout.equals("HEALTHY")||pout.equals("INFECTED")){
                    uploadToServer(pout);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img=(ImageView)findViewById(R.id.imageToUpload);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img.setImageBitmap(bitmap);
                received=1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void chooserIntent(){
        Button chooseImage=findViewById(R.id.chooseButton);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }
    public void uploadToServer(String predictionText){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        EditText fileName=findViewById(R.id.fileName);

        if(!fileName.getText().toString().equals("")) {
            String postUrl="http://192.168.1.11:8080/iyer/UploadServlet";
            RequestBody postBodyImage = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("prediction",predictionText)
                    .addFormDataPart("image", fileName.getText().toString() + ".png", RequestBody.create(MediaType.parse("image/*"), byteArray))
                    .build();

            postRequest(postUrl,postBodyImage);
        }
        else{
            Toast.makeText(MainActivity.this,"Fields cannot be empty",Toast.LENGTH_SHORT).show();
        }

    }
    public void postRequest(String postUrl,RequestBody requestBody){
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(postUrl)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                            TextView responseText=findViewById(R.id.responseText);
  //                          responseText.setText("Failed to Connect To Storage Server");
                            Toast.makeText(MainActivity.this,"Failed to connect to storage server",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseString=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                              TextView responseText=findViewById(R.id.responseText);
//                            responseText.setText(response.body().string());
                            Toast.makeText(MainActivity.this,responseString,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    public void uploadToFlaskServer(){
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();

        EditText imageName=findViewById(R.id.fileName);
        EditText enterUrl=findViewById(R.id.serverAddress);

        if(!imageName.getText().toString().equals("")&&!enterUrl.getText().toString().equals("")) {
            String postUrl=enterUrl.getText().toString();
            RequestBody postBodyImage = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imageName.getText().toString() + ".png", RequestBody.create(MediaType.parse("image/*"), byteArray))
                    .build();
            TextView predictedOutput=findViewById(R.id.responseText);
            predictedOutput.setText("Please Wait...");

            postmlRequest(postUrl,postBodyImage);
        }
        else{
            Toast.makeText(MainActivity.this,"Fields cannot be empty",Toast.LENGTH_SHORT).show();
        }

    }
    public void postmlRequest(String postUrl,RequestBody requestBody){

        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url(postUrl)
                .post(requestBody)
                .build();
//        Toast.makeText(MainActivity.this,request.toString()+"--",Toast.LENGTH_SHORT).show();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView predictedOutput=findViewById(R.id.responseText);
                        predictedOutput.setText("Failed to connect to server");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String x=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            TextView predictedOutput=findViewById(R.id.responseText);
                            predictedOutput.setText(x);

                    }
                });
            }
        });
    }


}
