package com.example.appbanvemaybay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class VeMayBayActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView lvManHinhChinh;
    SearchView searchView;
    DrawerLayout drawerLayout;

    MenuAdapter adapter = new MenuAdapter(this);

    EditText EdtSearch;
    List<VeMayBayMoi> mangSpMoi = new ArrayList<VeMayBayMoi>();
    VeMayBayAdapter spAdapter;
    GridView gvCateList;
    CategoryAdapter categoryAdapter;
    DatabaseHelper db;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<Category> cateListContent;

    @Override
    protected void onResume() {
        super.onResume();
        if (cateListContent != null){
            cateListContent.removeAll(cateListContent);
        }
        if (mangSpMoi != null){
            mangSpMoi.removeAll(mangSpMoi);
        }
        anhxa();
        intData();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ve_may_bay);
        db = new DatabaseHelper(this, "DBVeMayBay.sqlite", null, 1);
        anhxa();
        actionBar();
        actionMenu();
        intData();
        SearchItem();
        sqLiteDatabase = db.getWritableDatabase();

        //Phân loại sản phẩm
        gvCateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                  //lấy ra Cate được click
                                                  Category cate = (Category) parent.getItemAtPosition(position);
                                                  if (cate.getName() == "ALL") {
                                                      Cursor listSanPham = db.GetData(
                                                              "Select* from SANPHAM order by TENSP ASC"
                                                      );
                                                      //Xóa List<SanPhamMoi> cũ đi
                                                      mangSpMoi.removeAll(mangSpMoi);
                                                      //Tạo List<SanPhamMoi> mới
                                                      while (listSanPham.moveToNext()) {
                                                          mangSpMoi.add(new VeMayBayMoi(
                                                                  listSanPham.getString(0),
                                                                  listSanPham.getString(1),
                                                                  listSanPham.getString(2),
                                                                  listSanPham.getInt(3),
                                                                  listSanPham.getString(4),
                                                                  listSanPham.getString(5),
                                                                  listSanPham.getLong(6),
                                                                  listSanPham.getInt(7)
                                                          ));
                                                      }
                                                      //Cập nhật vào Adapter
                                                      spAdapter.notifyDataSetChanged();
                                                  } else {
                                                      //Lọc danh sách sản phẩm có chứa PHANLOAI theo NAME của cate vừa lấy
                                                      //Lệnh SQL
                                                      //Select*
                                                      //From SANPHAM
                                                      //Where PHANLOAI = 'cate.getName()';
                                                      Cursor listSanPham = db.GetData(
                                                              "Select* " +
                                                                      "from SANPHAM " +
                                                                      "where PHANLOAI = '" + cate.getName() + "' " +
                                                                      "order by TENSP ASC;"
                                                      );
                                                      //Xóa List<SanPhamMoi> cũ đi
                                                      mangSpMoi.removeAll(mangSpMoi);
                                                      //Tạo List<SanPhamMoi> mới
                                                      while (listSanPham.moveToNext()) {
                                                          mangSpMoi.add(new VeMayBayMoi(
                                                                  listSanPham.getString(0),
                                                                  listSanPham.getString(1),
                                                                  listSanPham.getString(2),
                                                                  listSanPham.getInt(3),
                                                                  listSanPham.getString(4),
                                                                  listSanPham.getString(5),
                                                                  listSanPham.getLong(6),
                                                                  listSanPham.getInt(7)
                                                          ));
                                                      }
                                                      //Cập nhật vào Adapter
                                                      spAdapter.notifyDataSetChanged();
                                                  }
                                              }
                                          }
        );
    }
    public void intData () {

        Cursor listSanPham = db.GetData(
                "Select* from SANPHAM order by TENSP ASC"
        );
        //Lấy danh sách sản phẩm hiện có trong database
        while (listSanPham.moveToNext()) {
            mangSpMoi.add(new VeMayBayMoi(
                    listSanPham.getString(0),
                    listSanPham.getString(1),
                    listSanPham.getString(2),
                    listSanPham.getInt(3),
                    listSanPham.getString(4),
                    listSanPham.getString(5),
                    listSanPham.getLong(6),
                    listSanPham.getInt(7)
            ));
        }
        recyclerViewManHinhChinh = (RecyclerView) findViewById(R.id.listnewProduct);
        spAdapter = new VeMayBayAdapter(this, mangSpMoi);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false);
        recyclerViewManHinhChinh.setAdapter(spAdapter);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
    }

    private void actionMenu(){

        lvManHinhChinh.setAdapter(adapter);
        //chức năng của từng item trong actionmenu
        lvManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        drawerLayout.closeDrawer(GravityCompat.START);
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

    private void actionBar () {
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
    // Lấy danh sách sản phẩm từ database
    private List<VeMayBayMoi> getAllProducts() {
        List<VeMayBayMoi> products = new ArrayList<>();
        // Thực hiện câu lệnh SQL để lấy dữ liệu từ database
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM products", null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("MASP"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("TENSP"));
            @SuppressLint("Range") int price = cursor.getInt(cursor.getColumnIndex("DONGIA"));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("NOIDEN"));
            @SuppressLint("Range") String start = cursor.getString(cursor.getColumnIndex("NOIVE"));
            VeMayBayMoi product = new VeMayBayMoi(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getLong(6),
                    cursor.getInt(7)
            );
            products.add(product);
        }
        cursor.close();
        return products;
    }
    private void SearchItem(){
        // Khởi tạo SearchManager
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        // Khởi tạo SearchableInfo từ ComponentName của Activity và searchable configuration trong manifest
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(new ComponentName(this, VeMayBayActivity.class));
        // Thiết lập SearchableInfo cho SearchView
        searchView.setSearchableInfo(searchableInfo);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Xử lý yêu cầu tìm kiếm khi người dùng nhấn nút tìm kiếm
                List<VeMayBayMoi> filteredProducts = getFilteredProducts(query);
                showFilteredProducts(filteredProducts);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý yêu cầu tìm kiếm khi người dùng nhập văn bản vào thanh tìm kiếm
                if(newText.isEmpty()){
                    // Nếu newText rỗng, hiển thị tất cả sản phẩm
                    HienLaiSanPham();
                }
                return true;
            }
        });
        // Thiết lập cursor adapter để hiển thị danh sách gợi ý

    }
    public void HienLaiSanPham() {
        // Nếu đang hiển thị danh sách sản phẩm tìm kiếm, xóa danh sách đó và hiển thị lại tất cả sản phẩm
        mangSpMoi.clear();
        spAdapter.notifyDataSetChanged();
        intData();
    }
    private void showFilteredProducts(List<VeMayBayMoi> filteredProducts) {
        // Xóa dữ liệu cũ trong danh sách sản phẩm
        mangSpMoi.removeAll(mangSpMoi);

        // Thêm sản phẩm mới vào danh sách
        for (VeMayBayMoi product : filteredProducts) {

            mangSpMoi.add(new VeMayBayMoi(
                    product.getMASP(),
                    product.getTENSP(),
                    product.getPHANLOAI(),
                    product.getSOLUONG(),
                    product.getNOIDEN(),
                    product.getNOIVE(),
                    product.getDONGIA(),
                    product.getHINHANH()

            ));
        }
        // Cập nhật adapter
        spAdapter.notifyDataSetChanged();
    }
    public List<VeMayBayMoi> getFilteredProducts(String searchText) {

        List<VeMayBayMoi> filteredProducts = new ArrayList<>();
        for (VeMayBayMoi product : mangSpMoi) {
            if (product.getTENSP().toLowerCase().contains(searchText.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }
    private void anhxa () {
        toolbar = (Toolbar) findViewById(R.id.toolbarManhinhChinh);
        recyclerViewManHinhChinh = (RecyclerView) findViewById(R.id.listnewProduct);
        lvManHinhChinh = (ListView) findViewById(R.id.listManHinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        gvCateList = (GridView) findViewById(R.id.gvCateList);
        EdtSearch = (EditText) findViewById(R.id.edtSearch);
        searchView = findViewById(R.id.SVItem);
        //tắt tự động focus vào SearchView
        searchView.clearFocus();
        //Lấy danh sách các Category hiện có
        Cursor listCate = db.GetData(
                "Select* from [CATEGORY]"
        );
        cateListContent = new ArrayList<Category>();
        cateListContent.add(new Category("ALL", "Tất cả"));
        while (listCate.moveToNext()) {
            cateListContent.add(new Category(listCate.getString(0), listCate.getString(1)));
        }
        //Tạo listview danh sách Category
        categoryAdapter = new CategoryAdapter(this, R.layout.activity_category_view, cateListContent);
        gvCateList.setAdapter(categoryAdapter);
    }
}