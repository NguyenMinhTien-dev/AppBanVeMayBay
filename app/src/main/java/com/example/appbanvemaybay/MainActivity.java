package com.example.appbanvemaybay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    StatusLogin status;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private List<Photo> mListPhoto;
    private Timer mTimer;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    TextView tvHello;
    ImageView imgToProfile;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ListView lvManHinhChinh;
    MenuAdapter adapter = new MenuAdapter(this);
    List<VeMayBayMoi> vemaybay = new ArrayList<VeMayBayMoi>();
    VeMayBayAdapter VMBAdapter;
    RecyclerView recyclerVemaybay;

    @Override
    protected void onResume() {
        super.onResume();
        AllProduct();
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this, "DBVeMayBay.sqlite", null, 1);

        toolbar = (Toolbar) findViewById(R.id.toolbarManhinhChinh);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        recyclerViewManHinhChinh = (RecyclerView) findViewById(R.id.listnewProduct);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        lvManHinhChinh = (ListView) findViewById(R.id.listManHinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        status = (StatusLogin) getApplication();
        tvHello = findViewById(R.id.tvHello);
        imgToProfile = findViewById(R.id.imgToProfile);
        if (status.isLogin() == false){
            //khi chưa đnăg nhập, ẩn thứ cần ẩn
            tvHello.setVisibility(View.INVISIBLE);
            imgToProfile.setVisibility(View.INVISIBLE);
        } else {
            //Khi ddanwg nhap roi, hien thu can hien
            tvHello.setText("Hello, " + status.getUser());
            imgToProfile.setVisibility(View.VISIBLE);
            tvHello.setVisibility(View.VISIBLE);
        }

        viewPager = findViewById(R.id.viewpager);
        circleIndicator = findViewById(R.id.circle_indicator);
        mListPhoto = getListPhoto();
        photoAdapter = new PhotoAdapter(this,mListPhoto);
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideImages();

        imgToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AboutCustomer.class);
                startActivity(i);
            }
        });

        actionBar();
        actionMenu();
    }
    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.hinhchuyendong1));
        list.add(new Photo(R.drawable.hinhchuyendong2));
        list.add(new Photo(R.drawable.hinhchuyendong3));
        list.add(new Photo(R.drawable.hinhchuyendong4));
        list.add(new Photo(R.drawable.hinhchuyendong5));
        return list;
    }
    private void autoSlideImages(){
        if(mListPhoto == null || mListPhoto.isEmpty() || viewPager == null){
            return;
        }
        if(mTimer == null){
            mTimer =  new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mListPhoto.size()-1;
                        if ( currentItem < totalItem) {
                            currentItem ++;
                            viewPager.setCurrentItem(currentItem);
                        }
                        else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500,3000);
    }

    private void actionMenu(){

        lvManHinhChinh.setAdapter(adapter);
        //chức năng của từng item trong actionmenu
        lvManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent sanpham = new Intent(getApplicationContext(), VeMayBayActivity.class);
                        startActivity(sanpham);
                        break;

                    case 2:
                        Intent gioithieu = new Intent(getApplicationContext(), GioiThieuActivity.class);
                        startActivity(gioithieu);
                        break;

                    case 3:
                        Intent dangxuat = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangxuat);
                        break;
                }
            }
        });
    }

    private void actionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void AllProduct(){
        Cursor listVeMayBay = db.GetData(
                "SELECT* FROM SANPHAM"
        );
        if (vemaybay != null){
            vemaybay.removeAll(vemaybay);
        }
        int quantitySP = 8;
        while (listVeMayBay.moveToNext() && quantitySP > 0){
            vemaybay.add(new VeMayBayMoi(
                    listVeMayBay.getString(0),
                    listVeMayBay.getString(1),
                    listVeMayBay.getString(2),
                    listVeMayBay.getInt(3),
                    listVeMayBay.getString(4),
                    listVeMayBay.getString(5),
                    listVeMayBay.getLong(6),
                    listVeMayBay.getInt(7)
            ));
            quantitySP--;
        }
        VMBAdapter = new VeMayBayAdapter( this, vemaybay);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewManHinhChinh.setAdapter(VMBAdapter);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
    }
}