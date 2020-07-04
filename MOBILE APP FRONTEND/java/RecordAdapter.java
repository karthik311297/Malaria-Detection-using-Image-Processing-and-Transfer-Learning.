package com.example.karthik.testuploadtotomcat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.karthik.testuploadtotomcat.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecordAdapter extends ArrayAdapter<EachRecord> {

    public RecordAdapter(Context context, ArrayList<EachRecord> each){
        super(context,0,each);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemview=convertView;
        if(listitemview==null) {
            listitemview= LayoutInflater.from(getContext()).inflate(R.layout.record_layout,parent,false);
        }
        EachRecord record=getItem(position);
        if(record!=null){
            TextView cname=listitemview.findViewById(R.id.cname);
            TextView cpred=listitemview.findViewById(R.id.cpred);
            cname.setText(record.getCname());
            cpred.setText(record.getCpred());
        }
    return listitemview;
    }
}
