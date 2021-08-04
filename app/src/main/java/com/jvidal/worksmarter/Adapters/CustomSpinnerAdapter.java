package com.jvidal.worksmarter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jvidal.worksmarter.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends BaseAdapter {
    Context context; //context

    ArrayList<String> items;
    LayoutInflater inflter;

    public CustomSpinnerAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.items = items;
        inflter = (LayoutInflater.from((context)));

    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.spinnet_layout, parent, false);
        TextView txt_file_name = (TextView) convertView.findViewById(R.id.txt_file_name);
        txt_file_name.setText(items.get(position));

        return convertView;


    }
}