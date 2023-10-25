package com.example.appbanvemaybay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder>{
    Context context;
    //cmt test github
    List<GioHang> gioHangList;
    DatabaseHelper databaseHelper;



    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
        databaseHelper =  new DatabaseHelper(context, "DBVeMayBay.sqlite", null, 1);
    }


    //tạo một viewholder xem mới
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);
        return new MyViewHolder(view);
    }

    //xử lí các dữ liệu item
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GioHang gioHang = gioHangList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        holder.item_giohang_tensp.setText(gioHang.getTenSP());
        holder.item_giohang_sl.setText((gioHang.getSoLuong() + " "));
        Glide.with(context).load(gioHang.getHinhSanPham()).into(holder.item_giohang_image);
        CalculatePrice(holder, position);

        holder.setListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                //Log.d("TAG","onImageClick: "+pos + "...."+giatri);
                if (giatri == 1)
                {
                    if (gioHangList.get(pos).getSoLuong() > 1){
                        gioHangList.get(pos).setSoLuong(gioHangList.get(pos).getSoLuong()-1);
                        databaseHelper.updateCartList(gioHangList.get(pos));
                        CalculatePrice(holder, position);
                        EventBus.getDefault().postSticky(new TinhTongEvent());//bắt sk tính tổng cho all sp

                    } else if (gioHangList.get(pos).getSoLuong() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa vé này khỏi giỏ hàng ?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                String whereClause = "IDCARTLIST=?";
                                String[] whereArgs = new String[] { gioHangList.get(pos).getIdCartList().toString() };
                                int deletedRows = db.delete("CARTLIST", whereClause, whereArgs);
                                Toast.makeText(context, "Đã xóa " + deletedRows + "vé", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().postSticky(new TinhTongEvent());//bắt sk tính tổng cho all sp
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();


                    }
                } else if (giatri == 2) {
                    int soluongmoi = gioHangList.get(pos).getSoLuong()+1;
                    Cursor cursor = databaseHelper.GetData("Select SOLUONG from SANPHAM WHERE MASP = '" + gioHangList.get(pos).getIdSanPham() + "'");
                    cursor.moveToFirst();
                    if (cursor.getInt(0) < soluongmoi) {
                        Toast.makeText(context,"Số lượng vượt số lượng trong hệ thống.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    gioHangList.get(pos).setSoLuong(soluongmoi);
                    databaseHelper.updateCartList(gioHangList.get(pos));
                    holder.item_giohang_sl.setText((gioHangList.get(pos).getSoLuong().toString()));
                    double gia = gioHangList.get(pos).getSoLuong() * gioHangList.get(pos).getDonGia();
                    holder.item_giohang_gia2.setText(decimalFormat.format(gia) + " VNĐ");
                    EventBus.getDefault().postSticky(new TinhTongEvent());//bắt sk tính tổng cho all sp
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return gioHangList.size();
    }
    public void CalculatePrice(@NonNull MyViewHolder holder, int position){
        GioHang gioHang = gioHangList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        if (!gioHang.getIdVoucher().equals("")){
            Cursor cursor = databaseHelper.GetData("Select GIAM from VOUCHER where MAVOUCHER = '"+gioHang.getIdVoucher()+"'");
            cursor.moveToFirst();
            double mucgiam = cursor.getDouble(0);
            holder.tvMaVoucher.setVisibility(View.VISIBLE);
            holder.tvNewPrice.setVisibility(View.VISIBLE);
            holder.tvMaVoucher.setText("Mã voucher áp dụng: " + gioHang.getIdVoucher());
            String donGiaCu = decimalFormat.format((gioHang.getDonGia()));
            String donGiaMoi = decimalFormat.format((gioHang.getDonGia()*(1-mucgiam)));
            holder.item_giohang_gia.setText(donGiaCu + " VNĐ");
            holder.item_giohang_gia.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvNewPrice.setText(donGiaMoi + " VNĐ");
            double gia = gioHang.getSoLuong() * gioHang.getDonGia()*(1-mucgiam);
            holder.item_giohang_gia2.setText(decimalFormat.format(gia) + " VNĐ");
        } else {
            holder.tvMaVoucher.setVisibility(View.INVISIBLE);
            holder.tvNewPrice.setVisibility(View.INVISIBLE);
            holder.item_giohang_gia.setText(decimalFormat.format((gioHang.getDonGia())) + " VNĐ");
            double gia = gioHang.getSoLuong() * gioHang.getDonGia();
            holder.item_giohang_gia2.setText(decimalFormat.format(gia) + " VNĐ");
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_image,item_giohang_cong,item_giohang_tru;
        TextView item_giohang_tensp,item_giohang_gia,item_giohang_sl,item_giohang_gia2;
        TextView tvMaVoucher, tvNewPrice;
        IImageClickListener listener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_sl = itemView.findViewById(R.id.item_giohang_sl);
            item_giohang_gia2 = itemView.findViewById(R.id.item_giohang_gia2);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_tru = itemView.findViewById(R.id.item_giohang_tru);
            item_giohang_cong = itemView.findViewById(R.id.item_giohang_cong);
            tvMaVoucher = itemView.findViewById(R.id.tvMaVoucher);
            tvNewPrice = itemView.findViewById(R.id.tvNewPrice);
            //event click
            item_giohang_cong.setOnClickListener(this);
            item_giohang_tru.setOnClickListener(this);
        }

        public void setListener(IImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if (view == item_giohang_tru) {
                listener.onImageClick(view, getAbsoluteAdapterPosition(), 1);
                //1 là trừ
            }
            if (view == item_giohang_cong){
                listener.onImageClick(view, getAbsoluteAdapterPosition(), 2);
                //2 là cộng
            }
        }
    }
}
