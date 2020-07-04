package com.example.karthik.testuploadtotomcat;

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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PastViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_viewer);
        getFromServer();
    }
    public void getFromServer(){

            String getUrl="http://192.168.1.11:8080/iyer/GetAllDetailServlet";
            Request request=new Request.Builder().url(getUrl).build();
            new OkHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PastViewer.this,"Failed to fetch data",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String jsonResponse=response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                ArrayList<EachRecord> empty=new ArrayList<>();
//                                RecordAdapter emptyAdapter=new RecordAdapter(PastViewer.this,empty);
//                                ListView emptyListView=findViewById(R.id.allRecords);
//                                emptyListView.setAdapter(emptyAdapter);
                                final ArrayList<EachRecord> alldetails=new ArrayList<>();

                                JSONArray jsonArray=new JSONArray(jsonResponse);
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    EachRecord eachRecord=new EachRecord(jsonObject.getString("name"),jsonObject.getString("prediction"));
                                    alldetails.add(eachRecord);
                                }
                                RecordAdapter recordAdapter=new RecordAdapter(PastViewer.this,alldetails);
                                ListView listView=findViewById(R.id.allRecords);
                                listView.setAdapter(recordAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        EachRecord newRecord=alldetails.get(i);
                                        Toast.makeText(PastViewer.this,newRecord.getCname(),Toast.LENGTH_SHORT).show();
                                        Intent gotoImageViewActivity=new Intent(PastViewer.this,ImageViewerActivty.class);
                                        gotoImageViewActivity.putExtra("NAME",newRecord.getCname());
                                        startActivity(gotoImageViewActivity);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });


    }

}
