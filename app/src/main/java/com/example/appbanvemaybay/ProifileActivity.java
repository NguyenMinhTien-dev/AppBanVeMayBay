package com.example.appbanvemaybay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class ProifileActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    StatusLogin statusLogin;

    Toolbar toolbar;
    TextView userName,fullName,phoneNumber,email,diachi,btnEdit,btnChangeps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proifile);
        anhxa();
        actionBar();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfile = new Intent(ProifileActivity.this, UploadActivity.class);
                startActivity(editProfile);
            }
        });
//
        btnChangeps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProifileActivity.this,ChangePasswordActivity.class);
                startActivity(i);
            }
        });


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getUser();

        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(statusLogin.getUser())) {
                //userName.setText(cursor.getString(0));
                fullName.setText("" + cursor.getString(3));
                phoneNumber.setText("" + cursor.getString(4));
                email.setText("" + cursor.getString(5));
                diachi.setText("" + cursor.getString(6));
            }
        }
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

    private void anhxa(){
        toolbar = findViewById(R.id.toolbarProfile);
        userName = findViewById(R.id.tvUsername);
        fullName = findViewById(R.id.tvFullname);
        phoneNumber = findViewById(R.id.tvPhone);
        email = findViewById(R.id.tvEmail);
        diachi = findViewById(R.id.tvAddress);
        btnEdit = findViewById(R.id.btnEdit);
        btnChangeps = findViewById(R.id.btnChangePass);
        databaseHelper = new DatabaseHelper(this, "DBVeMayBay.sqlite", null, 1);
        statusLogin = (StatusLogin) getApplication();
        //Thực hiện truy xuất bảng ACCOUNT
        Cursor cursor = databaseHelper.GetData("Select* From ACCOUNT");
//        //Sau khi thực hiện truy vấn, dữ liệu trả về là 1 cursor, cursor hiểu đơn giản là
//        //một mảng lưu tất cả dữ liệu từ table sau khi truy xuất
//
//        //Thực hiện dò từng account trong cursor, tìm ra account có username = statusLogin.getUser()
//
        while(cursor.moveToNext()){
            if (cursor.getString(0).equals(statusLogin.getUser())){
                //Sau khi tìm thấy dữ liệu trùng khớp, lập tức setText cho các TextView dựa vào số cột như đã chỉ
                userName.setText(cursor.getString(0));
                fullName.setText(cursor.getString(3)); //Tên trùng với cột thứ 3 trong bảng ACCOUNT (tính từ 0)
                phoneNumber.setText(cursor.getString(4));
                email.setText(cursor.getString(5));
                diachi.setText(cursor.getString(6));
            }
        }

    }
}