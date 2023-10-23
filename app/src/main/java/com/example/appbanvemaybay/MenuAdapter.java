package com.example.appbanvemaybay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends BaseAdapter {
    List<ItemMenu> list = new ArrayList<>();
    private int layout;
    Context context;
    public MenuAdapter(Context context){
        this.context = context;
        this.layout = R.layout.item_sanpham;
        list.add(new ItemMenu(R.drawable.baseline_home_24,"Home"));
        list.add(new ItemMenu(R.drawable.product,"Airline tickets"));
        list.add(new ItemMenu(R.drawable.introduce,"About us"));
        if (StatusLogin.login){
            list.add(new ItemMenu(R.drawable.baseline_logout_24,"Exit, " + StatusLogin.user));
        } else {
            list.add(new ItemMenu(R.drawable.baseline_login_24,"Login"));
        }
    }
    public MenuAdapter(List<ItemMenu> list, int layout, Context context) {
        this.list = list;
        this.layout = layout;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout,null);
            viewHolder = new ViewHolder();
            viewHolder.tv =(TextView) view.findViewById(R.id.tv_item);
            viewHolder.img =(ImageView) view.findViewById(R.id.imgItem);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv.setText(list.get(i).tenMenu);
        viewHolder.img.setImageResource(list.get(i).icon);
        return view;
    }
}
