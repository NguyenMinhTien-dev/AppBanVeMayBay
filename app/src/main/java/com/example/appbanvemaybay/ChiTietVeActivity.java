package com.example.appbanvemaybay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChiTietVeActivity extends AppCompatActivity {
    TextView tensp, giasp, motasp, noinhapsp, tvVoucher;
    Button btnthem;
    ImageView imgHinhAnh;
    Spinner spnSL, spnVoucher;
    Toolbar toolbar;
    VeMayBayMoi sanPhamMoi;
    NotificationBadge badge;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    StatusLogin statusLogin;
    QLGioHang qlGioHang;
    ArrayList<String> listVoucher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_ve);
        intView();
        intData();
        Clickbtn();
        actionBar();
    }
    private void Clickbtn(){
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sanPhamMoi.getSOLUONG() < Integer.parseInt(spnSL.getSelectedItem().toString())){
                    Toast.makeText(getApplicationContext(), "Lượng vé còn lại không tồn tại.", Toast.LENGTH_SHORT).show();
                    return;
                }
                themGioHang();
            }
        });
        spnVoucher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý sự kiện khi người dùng chọn một phần tử trong Spinner
                if (spnVoucher.getSelectedItem() != "NONE"){
                    Cursor cursor = databaseHelper.GetData("Select VOUCHER.MAVOUCHER, VOUCHER.NOIDUNG, VOUCHER.HSD, VOUCHER.GIAM " +
                            "From VOUCHER_DETAIL, VOUCHER " +
                            "Where VOUCHER_DETAIL.MAVOUCHER = VOUCHER.MAVOUCHER " +
                            "And VOUCHER_DETAIL.MAVOUCHER = '"+ spnVoucher.getSelectedItem() + "' ");
                    cursor.moveToFirst();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietVeActivity.this);
                    String hsd = cursor.getString(2);
                    // Định dạng của chuỗi đầu vào
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    // Định dạng của chuỗi đầu ra
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                    // Chuyển đổi chuỗi đầu vào sang LocalDate
                    LocalDate date = LocalDate.parse(hsd, inputFormatter);

                    // Chuyển đổi LocalDate sang chuỗi đầu ra
                    String outputDate = date.format(outputFormatter);

                    String infoVoucher = "Mã voucher: " + cursor.getString(0)
                            + "\nNội dung: " + cursor.getString(1)
                            + "\nHạn sử dụng: " + outputDate
                            + "\nMức giảm: " + cursor.getDouble(3)*100 + "%";
                    builder.setMessage(infoVoucher);
                    builder.setTitle("THÔNG TIN VOUCHER");
                    builder.setPositiveButton("Áp dụng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            spnVoucher.setSelection(position);
                        }
                    });
                    builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            spnVoucher.setSelection(0);
                        }
                    });
                    builder.create().show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void themGioHang() {
        String masp = sanPhamMoi.getMASP();
        String statusLogin_User = statusLogin.getUser();
        int sl = Integer.parseInt(spnSL.getSelectedItem().toString());
        Cursor cursor = databaseHelper.GetData("Select* from CARTLIST where IDSANPHAM = '" + masp + "' and IDCUS = '" + statusLogin_User + "'");
        Cursor cursor1 = databaseHelper.GetData("Select SOLUONG from SANPHAM Where MASP = '" + masp + "'");
        boolean checkExisted = false;
        while (cursor.moveToNext() && cursor1.moveToFirst()){
            //Nếu Tìm thấy giỏ hàng đã tồn tại trong database
            if (cursor.getString(2).equals(masp) && cursor.getString(1).equals(statusLogin_User)){
                GioHang gioHang = new GioHang();
                gioHang.setIdCartList(cursor.getInt(0));
                gioHang.setIdCus(cursor.getString(1));
                gioHang.setIdSanPham(cursor.getString(2));
                gioHang.setIdVoucher((spnVoucher.getSelectedItem() == "NONE") ? "" : spnVoucher.getSelectedItem().toString());
                gioHang.setDonGia(sanPhamMoi.getDONGIA());
                gioHang.setTenSP(sanPhamMoi.getTENSP());
                gioHang.setSoLuong(((sl + cursor.getInt(4)) > cursor1.getInt(0)) ? cursor1.getInt(0) : sl + cursor.getInt(4));
                gioHang.setHinhSanPham(sanPhamMoi.getHINHANH());
                long kq = databaseHelper.updateCartList(gioHang);
                if (kq == 1){
                    Toast.makeText(getApplicationContext(), "Số vé đã có trong giỏ hàng, chỉnh số lượng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Số vé đã có trong vào giỏ hàng, lỗi update dữ liệu", Toast.LENGTH_SHORT).show();
                }
                checkExisted = true;
                return;
            }
        }
        if (!checkExisted){
            GioHang gioHang = new GioHang();
            gioHang.setIdCus(statusLogin_User);
            gioHang.setIdSanPham(sanPhamMoi.getMASP());
            String maVoucher = spnVoucher.getSelectedItem().toString();
            gioHang.setIdVoucher((maVoucher == "NONE") ? "" : maVoucher);
            gioHang.setDonGia(sanPhamMoi.getDONGIA());
            gioHang.setTenSP(sanPhamMoi.getTENSP());
            gioHang.setSoLuong(sl);
            gioHang.setHinhSanPham(sanPhamMoi.getHINHANH());
            long kq = databaseHelper.addCartList(gioHang);
            if (kq != -1){
                Toast.makeText(getApplicationContext(), "Vé đã được thêm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Vé thêm thất bại.", Toast.LENGTH_SHORT).show();
            }
        }
        updateBadge();
    }

    private void intData(){
        sanPhamMoi = (VeMayBayMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTENSP());

        Glide.with(getApplicationContext()).load(sanPhamMoi.getHINHANH()).into(imgHinhAnh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(String.valueOf(sanPhamMoi.getDONGIA()))) + " VNĐ");
        Integer[] sl = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,sl);
        spnSL.setAdapter(adapterspin);
        //Setup spinner spnVoucher
        if(listVoucher != null){
            listVoucher.removeAll(listVoucher);
        }
        listVoucher.add("NONE");
        Cursor cursor = databaseHelper.GetData("Select VOUCHER.MAVOUCHER " +
                "From VOUCHER_DETAIL, VOUCHER " +
                "Where VOUCHER_DETAIL.MAVOUCHER = VOUCHER.MAVOUCHER " +
                "And VOUCHER.HSD > '"+ LocalDate.now().toString() +"' " +
                "And VOUCHER_DETAIL.MASP = '"+ sanPhamMoi.getMASP() + "' ");
        while (cursor.moveToNext()){
            listVoucher.add(cursor.getString(0));
        }
        ArrayAdapter<String> adapterVoucher = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listVoucher );
        spnVoucher.setAdapter(adapterVoucher);

        //nếu hệ thống chưa được đăng nhập, voucher sẽ không khả dụng
        if (!statusLogin.isLogin()){
            spnVoucher.setVisibility(View.GONE);
            tvVoucher.setText("ĐĂNG NHẬP TẠI ĐÂY ĐỂ SỬ DỤNG VOUCHER.");
            tvVoucher.setTypeface(null, Typeface.BOLD);
            tvVoucher.setTextColor(Color.parseColor("#FF0000"));
            tvVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), DangNhapActivity.class);
                    startActivity(i);
                }
            });
        }
    }
    private void intView(){
        tensp = (TextView) findViewById(R.id.tvTenSp);
        giasp = (TextView) findViewById(R.id.tvGia);
        btnthem = (Button) findViewById(R.id.btnthemvaoGioHang);
        spnSL = (Spinner) findViewById(R.id.spnSL);
        spnVoucher = (Spinner) findViewById(R.id.spnVoucher);
        imgHinhAnh = (ImageView) findViewById(R.id.imgchitiet);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        badge = (NotificationBadge) findViewById(R.id.menu_sl);
        qlGioHang = new QLGioHang(this);
        FrameLayout frameLayoutgiohang = findViewById(R.id.framegiohang);
        tvVoucher = (TextView) findViewById(R.id.tvVoucher);
        listVoucher = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this, "DBVeMayBay.sqlite", null, 1);
        statusLogin = (StatusLogin) getApplication();
        updateBadge();

//        frameLayoutgiohang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
//                startActivity(giohang);
//            }
//        });

    }

    private void actionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //cập nhật lại giỏ hàng sau khi xóa sp khỏi giỏ hàng

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
        Cursor cursor = databaseHelper.GetData("Select* from SANPHAM Where MASP = '" +sanPhamMoi.getMASP()+"'");
        cursor.moveToFirst();
        tensp.setText(cursor.getString(1));

        Glide.with(getApplicationContext()).load(cursor.getInt(7)).into(imgHinhAnh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: " + decimalFormat.format(Double.parseDouble(String.valueOf(cursor.getDouble(6)))) + " VNĐ");
        //Setup spinner spnVoucher
        if (listVoucher != null)
            listVoucher.removeAll(listVoucher);
        listVoucher.add("NONE");
        Cursor cursor1 = databaseHelper.GetData("Select VOUCHER_DETAIL.MAVOUCHER " +
                "From VOUCHER_DETAIL, VOUCHER " +
                "Where VOUCHER_DETAIL.MAVOUCHER = VOUCHER.MAVOUCHER " +
                "And VOUCHER.HSD > '"+ LocalDate.now().toString() +"' " +
                "And VOUCHER_DETAIL.MASP = '"+ cursor.getString(0) + "' ");
        while (cursor1.moveToNext()){
            listVoucher.add(cursor1.getString(0));
        }
        ArrayAdapter<String> adapterVoucher = new ArrayAdapter<>(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listVoucher );
        spnVoucher.setAdapter(adapterVoucher);
        //nếu hệ thống chưa được đăng nhập, voucher sẽ không khả dụng
        if (!statusLogin.isLogin()){
            spnVoucher.setVisibility(View.GONE);
            tvVoucher.setText("ĐĂNG NHẬP TẠI ĐÂY ĐỂ SỬ DỤNG VOUCHER.");
            tvVoucher.setTypeface(null, Typeface.BOLD);
            tvVoucher.setTextColor(Color.parseColor("#FF0000"));
            tvVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), DangNhapActivity.class);
                    startActivity(i);
                }
            });
        }
    }
    private void updateBadge(){
        Cursor cursor = databaseHelper.GetData("Select* from CARTLIST where IDCUS = '" + statusLogin.getUser() + "'");
        Integer count = 0;
        while (cursor.moveToNext()){
            count++;
        }
        badge.setText(count.toString());
    }
}