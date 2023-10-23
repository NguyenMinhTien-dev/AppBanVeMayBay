package com.example.appbanvemaybay;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DangNhapActivity extends AppCompatActivity {
    DatabaseHelper db; //Khởi tạo database
    StatusLogin status;
    EditText medtUser, medtPassword;
    Button mbtnLogin;
    TextView mtvRegister, mtvForgotpass, tvContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        AnhXa();

        db = new DatabaseHelper(this, "DBVeMayBay.sqlite", null, 1);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            medtUser.setText(bundle.getString("user"));
            medtPassword.setText(bundle.getString("pass"));
        }

        XuLyOnCLick();
    }

    public View.OnClickListener onClick_tvRegister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(DangNhapActivity.this, DangKyActivity.class);
            startActivity(i);
        }
    };

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Unavailable", Toast.LENGTH_SHORT).show();
    }

    public View.OnClickListener onClick_ShoppingWithoutLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    };

    public View.OnClickListener onClick_btnLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String taikhoan = medtUser.getText().toString();
            String matkhau = medtPassword.getText().toString();
            //Kiểm tra nhập tài khoản chưa
            if (taikhoan.length() == 0){
                Toast.makeText(getApplicationContext(), "Nhập tài khoản", Toast.LENGTH_LONG).show();
                medtUser.requestFocus();
                return;
            }
            //Kiểm tra nhập mật khẩu chưa
            if (matkhau.length() == 0){
                Toast.makeText(getApplicationContext(), "Nhập mật khẩu", Toast.LENGTH_LONG).show();
                medtPassword.requestFocus();
                return;
            }
            //Lấy dữ liệu account
            Cursor listAccount = db.GetData(
                    "Select*" +
                            "From ACCOUNT"
            );

            String role = ""; //Lưu lại role của tài khoản khi tìm thấy
            String user = ""; //Lưu lại user của tài khoản khi tìm thấy
            String name = ""; //Lưu lại name của tài khoản khi tìm thấy
            while (listAccount.moveToNext()){
                if (taikhoan.equals(listAccount.getString(0)) && matkhau.equals(listAccount.getString(1))){
                    //Nếu tài khoản được tìm thấy có role là admin thì vô trang admin
                    //ngược lại thì vào trang chủ của app
                    if (listAccount.getString(2) .equals("admin")){
                        Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(i);
                        return;
                    } else {
                        status.setLogin(true);
                        status.setName(listAccount.getString(3));
                        status.setUser(listAccount.getString(0));
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        return;
                    }
                }
            }
            Toast.makeText(getApplicationContext(), "Sai mật khẩu hoặc tài khoản", Toast.LENGTH_SHORT).show();
        }
    };
    public void AnhXa(){
        medtUser = (EditText) findViewById(R.id.edtUser);
        medtPassword = (EditText) findViewById(R.id.edtPassword);
        mtvForgotpass = (TextView) findViewById(R.id.tvForgotpass);
        mtvRegister = (TextView) findViewById(R.id.tvRegister);
        mbtnLogin = (Button) findViewById(R.id.btnLogin);
        tvContact = (TextView) findViewById(R.id.tvContact);
        status = (StatusLogin) getApplication();
        status.setUser("");
        status.setName("");
        status.setLogin(false);
    }
    public void XuLyOnCLick(){
        mtvRegister.setOnClickListener(onClick_tvRegister); //Hàm onClick đổi sang Activity Đăng kí
        mbtnLogin.setOnClickListener(onClick_btnLogin); //Hàm onClick xử lí đăng nhập
        tvContact.setOnClickListener(onClick_ShoppingWithoutLogin);
    }
}
