package com.sultan.lasttest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sultan.lasttest.database.Course;

import java.util.List;

public class mySpinner extends BaseAdapter {
    private List<Course> lstData;
    private Activity activity;
    private LayoutInflater inflater;

    public mySpinner(List<Course> lstData, Activity activity) {
        this.lstData = lstData;
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lstData.size();
    }


    @Override
    public Object getItem(int postion) {
        return lstData.get(postion).courseName;
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(int postion, View convertview, ViewGroup parent) {
        View view = convertview;
        if(convertview == null)
            view = inflater.inflate(R.layout.spinner_item,null);
            TextView tv = (TextView)view.findViewById(R.id.textViewspinner2);
            tv.setText(lstData.get(postion).courseName);

        return view;

    }
    @Override
    public View getDropDownView(int postion , View convertview,ViewGroup parent){
        View view =super.getDropDownView(postion,convertview,parent);
        LinearLayout ll = (LinearLayout)view;
        TextView tv =(TextView)ll.findViewById(R.id.textViewspinner2);
        tv.setGravity(Gravity.LEFT);
        tv.setTextColor(Color.parseColor("#333639"));
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

        return view;
    }
}
