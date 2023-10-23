package com.example.appbanvemaybay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class DangKyActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button mRegisterBack, mRegisterSubmit;
    CheckBox mRegisterAcceptTerm;
    EditText mregisterAddress, mregisterGmail, mregisterPhonenumber, mregisterName, mregisterPassword, mregisterUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        db = new DatabaseHelper(this, "DBVeMayBay.sqlite", null, 1);
        mRegisterBack = (Button) findViewById(R.id.RegisterBack);
        mRegisterSubmit = (Button) findViewById(R.id.RegisterSubmit);
        mRegisterAcceptTerm = (CheckBox) findViewById(R.id.RegisterAcceptTerm)  ;
        mregisterAddress = (EditText) findViewById(R.id.registerAddress);
        mregisterGmail = (EditText) findViewById(R.id.registerGmail);
        mregisterPhonenumber = (EditText) findViewById(R.id.registerPhonenumber);
        mregisterName = (EditText) findViewById(R.id.registerName);
        mregisterPassword = (EditText) findViewById(R.id.registerPassword);
        mregisterUser = (EditText) findViewById(R.id.registerUser);
        mRegisterBack.setOnClickListener(onClick_RegisterBack);
        mRegisterSubmit.setOnClickListener(onClick_mRegisterSubmit);
    }
    public View.OnClickListener onClick_mRegisterSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String taikhoan = mregisterUser.getText().toString();
            String matkhau = mregisterPassword.getText().toString();
            String hoten = mregisterName.getText().toString();
            String sdt = mregisterPhonenumber.getText().toString();
            String gmail = mregisterGmail.getText().toString();
            String diachi = mregisterAddress.getText().toString();

            //region Kiểm tra chuỗi sdt có phải số không
            String[] token = sdt.split("\\.");
            int checkLength = 0;
            for (int i = 0; i < token.length; i++){
                checkLength += token[i].length();
            }
            if (checkLength != 10){
                Toast.makeText(getApplicationContext(), "Phone number must have 10 numbers.", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean is_Number = true;
            for (int i = 0; i < token.length; i++){
                if (!isNumber(token[i])){
                    is_Number = isNumber(sdt);
                    break;
                }
            }
            if (sdt.length() == 0 || is_Number == false){
                Toast.makeText(getApplicationContext(), "Phone number is invalid. Please try again!", Toast.LENGTH_LONG).show();
                return;
            } else {
                mregisterPhonenumber.setText(toPhoneNumber(token));
            }
            //endregion
            //Kiểm tra gmail
            if (!gmail.equals("")){
                boolean check = false;
                for (int i = 0; i < gmail.length(); i++){
                    if (gmail.charAt(i) == '2'){
                        check = true;
                    }
                }
                if (!check){
                    Toast.makeText(getApplicationContext(), "Gmail phải có '@gmail.com', sai định dạng!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            //Kiểm tra đã nhập đủ thông tin cần thiết chưa
            if (taikhoan.length() == 0 || matkhau.length() == 0 || hoten.length() == 0){
                Toast.makeText(DangKyActivity.this, "Username, Password, Name are required", Toast.LENGTH_LONG).show();
                return;
            }
            if (mRegisterAcceptTerm.isChecked() == false){
                Toast.makeText(getApplicationContext()  , "You must accepted the Terms and Conditions once.", Toast.LENGTH_LONG).show();
                return;
            }

            //Thông báo đăng kí thành công
            if (db.AddAccount(taikhoan, matkhau, "customer", hoten, sdt, gmail, diachi) == true){
                AlertDialog.Builder builder = new AlertDialog.Builder(DangKyActivity.this);
                builder.setMessage("Create Account Success.");
                builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("user", mregisterUser.getText().toString());
                        bundle.putString("pass", mregisterPassword.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                // Create the AlertDialog object
                builder.create().show();
                return;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(DangKyActivity.this);
                builder.setMessage("Username already exists.");
                builder.setPositiveButton("Forgot Password?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(DangKyActivity.this, ForgotPassword.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("New Username", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                // Create the AlertDialog object
                builder.create().show();
                return;
            }
        }
    };
    public View.OnClickListener onClick_RegisterBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(getApplicationContext(), DangNhapActivity.class);
            startActivity(i);
        }
    };

    //region Hàm xử lí chuỗi số điện thoại nhập vào
    private boolean isNumber(String x){
        return x.chars().allMatch( Character::isDigit );
    }
    private String toPhoneNumber(String[] x){
        String originalString = "";
        for (int i = 0 ; i < x.length; i++)
            originalString += x[i];
        String phoneNumber = new String();
        for (int i = 0; i < originalString.length(); i++){
            phoneNumber += originalString.charAt(i);
            if (i == 1 || i == 5)
                phoneNumber += ".";
        }
        return phoneNumber;
    }
}