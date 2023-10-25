package com.example.appbanvemaybay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.os.Bundle;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class VeMayBayActivity extends AppCompatActivity {
    List<VeMayBayMoi> mangSpMoi = new ArrayList<VeMayBayMoi>();
    VeMayBayAdapter spAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ve_may_bay);
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
}