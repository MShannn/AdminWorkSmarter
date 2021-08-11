package com.jvidal.worksmarter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jvidal.worksmarter.Models.Anomalies;
import com.jvidal.worksmarter.R;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter {
    Context context; //context

    ArrayList<String> items;
    List<Anomalies> anomaliesArrayList;
    LayoutInflater inflter;
    boolean isFile = false;

    public CustomSpinnerAdapter(Context context, ArrayList<String> items, List<Anomalies> anomaliesArrayList, boolean check) {
        this.context = context;
        this.items = items;
        this.anomaliesArrayList = anomaliesArrayList;
        inflter = (LayoutInflater.from((context)));
        this.isFile = check;

    }

    @Override
    public int getCount() {
        if (isFile){
            return items.size();
        }else {
            return anomaliesArrayList.size();
        }

    }


    @Override
    public Object getItem(int position) {
        if (isFile){
           return items.get(position);
        }else {
            return anomaliesArrayList.get(position);
        }
        //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.spinnet_layout, parent, false);
        TextView txt_file_name = (TextView) convertView.findViewById(R.id.txt_file_name);
        if (isFile)
            txt_file_name.setText(items.get(position));
        else {
            txt_file_name.setText(anomaliesArrayList.get(position).getCode());
        }
        return convertView;


    }
}