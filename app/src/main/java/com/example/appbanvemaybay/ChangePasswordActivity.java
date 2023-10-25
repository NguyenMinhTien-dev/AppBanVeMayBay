package com.example.appbanvemaybay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText oldPass,newPass;
    Button btnsave;
    DatabaseHelper databaseHelper;
    StatusLogin statusLogin;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        anhxa();
        actionBar();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoiMatKhau();
            }
        });
    }
    private void anhxa() {
        toolbar = findViewById(R.id.toolbarPassword);
        oldPass = findViewById(R.id.OldPassword);
        newPass = findViewById(R.id.NewPassword);
        btnsave = findViewById(R.id.btnSave);
        databaseHelper = new DatabaseHelper(this, "DBVeMayBay.sqlite", null, 1);
        statusLogin = (StatusLogin) getApplication();
    }

    private  void DoiMatKhau(){

        String oldpass = oldPass.getText().toString();
        String newpass = newPass.getText().toString();
        if (oldpass.isEmpty() || newpass.isEmpty())
        {
            Toast.makeText(ChangePasswordActivity.this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newpass.equals(oldpass)){
            Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới không được giống với mật khẩu cũ", Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor cursor = databaseHelper.GetData("Select* from ACCOUNT where TAIKHOAN = '" + statusLogin.getUser() + "'");
        cursor.moveToFirst();
        Toast.makeText(this, cursor.getString(1) + " và " + statusLogin.getUser(), Toast.LENGTH_SHORT).show();
        if (!oldpass.equals(cursor.getString(1))) {
            //Thông báo nhập sai mật khẩu
            Toast.makeText(this, "Mật khẩu không khớp. Vui lòng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        long checkpass = databaseHelper.changePassword(statusLogin.getUser(), oldpass, newpass);
        if (checkpass == 1){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("THÔNG BÁO");
            builder.setMessage("Cập nhật mật khẩu thành công, yêu cầu đăng nhập lại.");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Intent i = new Intent(getApplicationContext(), DangNhapActivity.class);
                    startActivity(i);
                }
            });
            builder.create().show();
        }
    }

    private void actionBar(){
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}