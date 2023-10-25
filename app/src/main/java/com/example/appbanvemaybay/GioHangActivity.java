package com.example.appbanvemaybay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong, tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GioHangAdapter adapter;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    List<GioHang> gioHangList;
    StatusLogin statusLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        intControl();
        totalMoney();
    }
    private void totalMoney() {
        long tongtiensp = 0;
        for (int i = 0; i < gioHangList.size(); i++){
            if (gioHangList.get(i).getIdVoucher().equals("")){
                tongtiensp += gioHangList.get(i).getSoLuong()*gioHangList.get(i).getDonGia();
                gioHangList.get(i).setTotalMoney(gioHangList.get(i).getSoLuong()*gioHangList.get(i).getDonGia());
            } else {
                Cursor cursor = databaseHelper.GetData("Select GIAM from VOUCHER where MAVOUCHER = '"+gioHangList.get(i).getIdVoucher()+"'");
                cursor.moveToFirst();
                double mucgiam = cursor.getDouble(0);
                tongtiensp += gioHangList.get(i).getSoLuong()*gioHangList.get(i).getDonGia()*(1-mucgiam);
                gioHangList.get(i).setTotalMoney(gioHangList.get(i).getSoLuong()*gioHangList.get(i).getDonGia()*(1-mucgiam));
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp) + " VNĐ ");
    }

    private void intControl() {
        setSupportActionBar(toolbar);//lay doi tuong actonbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//bat cai icon len

        //Click vào nút icon quay lại
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);//LayoutManager: xác định ra vị trí của các item trong RecyclerView.
        recyclerView.setLayoutManager(layoutManager);
        //trường hợp giỏ hàng trống
        Cursor cursor = databaseHelper.GetData("SELECT CARTLIST.IDCARTLIST, CARTLIST.IDVOUCHER, CARTLIST.IDSANPHAM, SANPHAM.TENSP, SANPHAM.HINHANH, CARTLIST.IDCUS, SANPHAM.DONGIA, CARTLIST.SOLUONG\n" +
                "FROM SANPHAM, CARTLIST\n" +
                "WHERE SANPHAM.MASP = CARTLIST.IDSANPHAM\n" +
                "AND CARTLIST.IDCUS = '"+statusLogin.getUser()+"'");
        gioHangList = new ArrayList<>();
        adapter = new GioHangAdapter(getApplicationContext(), gioHangList);
        recyclerView.setAdapter(adapter);
        Cursor cursor1 = cursor;
        if (!cursor1.moveToFirst()){
            giohangtrong.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
        }
        else{
            cursor.moveToFirst();
            GioHang gioHang = new GioHang();//Taọ class
            gioHang.setIdCartList(cursor.getInt(0));//Thêm thông tin
            gioHang.setIdVoucher(cursor.getString(1));
            gioHang.setIdSanPham(cursor.getString(2));
            gioHang.setTenSP(cursor.getString(3));
            gioHang.setHinhSanPham(cursor.getInt(4));
            gioHang.setIdCus(cursor.getString(5));
            gioHang.setDonGia(cursor.getLong(6));
            gioHang.setSoLuong(cursor.getInt(7));
            gioHangList.add(gioHang);
            while (cursor.moveToNext()){
                gioHang = new GioHang();
                gioHang.setIdCartList(cursor.getInt(0));
                gioHang.setIdVoucher(cursor.getString(1));
                gioHang.setIdSanPham(cursor.getString(2));
                gioHang.setTenSP(cursor.getString(3));
                gioHang.setHinhSanPham(cursor.getInt(4));
                gioHang.setIdCus(cursor.getString(5));
                gioHang.setDonGia(cursor.getLong(6));
                gioHang.setSoLuong(cursor.getInt(7));
                gioHangList.add(gioHang);
            }
            adapter.notifyDataSetChanged();
        }
}