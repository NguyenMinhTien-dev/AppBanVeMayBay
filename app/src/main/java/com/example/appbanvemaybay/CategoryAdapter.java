package com.example.appbanvemaybay;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> {
    Activity context = null; // Activity chứa Adapter này
    ArrayList<Category> categories = null; //List chứa các Category
    int layoutID; //Layout custom do ta tạo, cụ thể là Category View

    public CategoryAdapter(Activity context, int layoutID, ArrayList<Category> categories) {
        super(context, layoutID, categories);
        this.context = context;
        this.categories = categories;
        this.layoutID = layoutID;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        // nơi lấy textView trong Category View để hiển thị lên
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutID, null);
        final TextView txtdisplay = (TextView) convertView.findViewById(R.id.tvCategoryContent);
        final Category emp = categories.get(position);
        txtdisplay.setText(emp.getContent()); //Lấy tên của Category[posiontion], gán vào textview
        return convertView;
    }
}
