package com.example.appbanvemaybay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class VeMayBayAdapter extends RecyclerView.Adapter<VeMayBayAdapter.MyViewHolder>{
    List<VeMayBayMoi> array;
    Context context;

    public VeMayBayAdapter(Context context, List<VeMayBayMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vemaybay_moi,parent,false);
        return new VeMayBayAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VeMayBayMoi vemaybayMoi = array.get(position);

        holder.tensp.setText(vemaybayMoi.getTENSP());
        holder.NoiDen.setText(vemaybayMoi.getNOIDEN());
        holder.NoiVe.setText(vemaybayMoi.getNOIVE());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        if (vemaybayMoi.getSOLUONG() <= 0){
            holder.giatien.setText("TẠM HẾT VÉ");
            holder.giatien.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.giatien.setText("Giá: "+decimalFormat.format(Double.parseDouble(String.valueOf(vemaybayMoi.getDONGIA()))) + " VNĐ");
            holder.giatien.setTextColor(Color.parseColor("#000000"));
        }
        Glide.with(context).load(vemaybayMoi.getHINHANH()).into(holder.imgHinhanh);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChiTietVeActivity.class);
                i.putExtra("chitiet",vemaybayMoi);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView giatien, tensp, NoiDen, NoiVe;
        ImageView imgHinhanh;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            giatien = itemView.findViewById(R.id.tvPrice);
            tensp = itemView.findViewById(R.id.tvTittle);
            NoiDen = itemView.findViewById(R.id.tvNoiden);
            NoiVe = itemView.findViewById(R.id.tvNoive);
            imgHinhanh = itemView.findViewById(R.id.imgItemSp);
        }
    }
}
