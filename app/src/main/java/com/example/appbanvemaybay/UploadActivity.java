package com.example.appbanvemaybay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

public class UploadActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText uploadEmail,uploadSDT,uploadFullName,uploadAddress;

    Button buttonSave;
    Uri uri;
    DatabaseHelper db;

    StatusLogin statusLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        anhxa();
        actionBar();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuaDuLieu();
            }
        });
    }
    private void SuaDuLieu(){
        if (!uploadFullName.getText().toString().isEmpty()
                && !uploadSDT.getText().toString().isEmpty() && !uploadEmail.getText().toString().isEmpty()
                && !uploadAddress.getText().toString().isEmpty()){
            Account account = new Account();
            account.setTAIKHOAN(statusLogin.getUser().toString());
            account.setTEN(uploadFullName.getText().toString());
            String sdt = uploadSDT.getText().toString();
            if (sdt.length() != 10){
                Toast.makeText(getApplicationContext(), "Số điện thoại không đủ 10 chữ số.", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean check = sdt.chars().allMatch( Character::isDigit );
            if (!check) {
                Toast.makeText(getApplicationContext(), "Số điện thoại sai định dạng.", Toast.LENGTH_SHORT).show();
                return;
            }
            account.setSDT(uploadSDT.getText().toString());
            String gmail = uploadEmail.getText().toString();
            boolean checkGmail = false;
            for (int i = 0 ; i < gmail.length(); i++){
                if (gmail.charAt(i) == '@'){
                    checkGmail = true;
                    break;
                }
            }
            if (!checkGmail){
                Toast.makeText(getApplicationContext(), "Gmail không định dạng", Toast.LENGTH_SHORT).show();
                return;
            }

            account.setGMAIL(uploadEmail.getText().toString());
            account.setDIACHI(uploadAddress.getText().toString());
            db.updateUser(account);
            Intent i = new Intent(UploadActivity.this,ProifileActivity.class);
            startActivity(i);
        }
        else {
            Toast.makeText(this,"Empty",Toast.LENGTH_SHORT).show();
        }
    }

    private void anhxa(){

        toolbar = findViewById(R.id.toolbarProfile);
        uploadFullName = findViewById(R.id.uploadFullName);
        uploadEmail = findViewById(R.id.uploadEmail);
        uploadSDT = findViewById(R.id.uploadUserSDT);
        uploadAddress = findViewById(R.id.uploadAddress);
        buttonSave = findViewById(R.id.btnSave);
        statusLogin = (StatusLogin) getApplication();
        db = new DatabaseHelper(this, "DBVeMayBay.sqlite", null, 1);



    }
    //
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