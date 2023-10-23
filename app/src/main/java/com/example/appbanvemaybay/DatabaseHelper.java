package com.example.appbanvemaybay;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context) {
        super(context, "DBVeMayBay.sqlite", null, 1);
    }

    //Truy vấn không trả kết quả
    //Truy vấn không trả kết quả là truy vấn thêm, xóa, sửa trên database
    public void WriteQuery(String content){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(content);
    }
    //Truy vấn trả kết quả (Select)
    public Cursor GetData(String content){
        SQLiteDatabase db =  getReadableDatabase();
        return db.rawQuery(content, null);
    }
    //Hàm AddRole, Thêm dữ liệu vào bảng "ROLE"

    @Override
    public void onCreate(SQLiteDatabase db) {
        //region Tạo bảng ROLE: Quyền hạn
        db.execSQL("CREATE TABLE IF NOT EXISTS [ROLE] (" +
                "QUYENHAN VARCHAR PRIMARY KEY NOT NULL," +
                "NOIDUNG Text NOT NULL)");
        //Thêm dữ liệu vào bảng [ROLE]
        String s = "Insert into [ROLE] values " +
                "('admin', 'Quản trị viên');";
        db.execSQL("Insert into [ROLE] values" +
                "('admin', 'Quản trị viên')," +
                "('customer', 'Khách hàng')");
        //endregion

        //region Tạo bảng ACCOUNT: chứa các tài khoản
        db.execSQL("CREATE TABLE IF NOT EXISTS ACCOUNT (\n" +
                "\tTAIKHOAN VARCHAR PRIMARY KEY NOT NULL,\n" +
                "\tMATKHAU VARCHAR NOT NULL,\n" +
                "\tQUYENHAN VARCHAR NOT NULL, \n" +
                "\tTEN VARCHAR,\n" +
                "\tSDT VARCHAR,\n" +
                "\tGMAIL VARCHAR,\n" +
                "\tDIACHI VARCHAR,\n" +
                "\tFOREIGN KEY (QUYENHAN) REFERENCES [ROLE](QUYENHAN)\n" +
                ");");
        //Thêm tài khoản admin và khách hàng mẫu để test
        db.execSQL("Insert into ACCOUNT values " +
                "('123', '123', 'admin', 'Nguyen Van A', '0924939352', 'voquinamit1@gmail.com', 'thailan'), " +
                "('1234', '1234', 'customer', 'Nguyen Thi B', '0334379439', '', '119');");
        //endregion

        //region Tạo bảng CATEGORY: Phân loại sản phẩm
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS [CATEGORY] (" +
                        "NAME VARCHAR PRIMARY KEY NOT NULL, " +
                        "NOIDUNG VARCHAR);"
        );
        //Thêm một số CATEGORY
        db.execSQL("Insert into [CATEGORY] values " +
                "('Vietnam Airlines', 'Vietnam Airlines'), " +
                "('Vietjet Air', 'Vietjet Air'), " +
                "('Bamboo Airways', 'Bamboo Airways')");
        //endregion

        //region Tạo bảng SẢN PHẨM: Lưu trữ sản phẩm (hoa)
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS SANPHAM (\n" +
                        "MASP VARCHAR PRIMARY KEY NOT NULL,\n" +
                        "TENSP VARCHAR NOT NULL, \n" +
                        "PHANLOAI VARCHAR NOT NULL, \n" +
                        "SOLUONG INTEGER NOT NULL,\n" +
                        "NOIDEN VARCHAR NOT NULL,\n" +
                        "NOIVE VARCHAR, \n" +
                        "DONGIA REAL CHECK(DONGIA > 0) NOT NULL,\n" +
                        "HINHANH INTEGER NOT NULL,\n" +
                        "FOREIGN KEY (PHANLOAI) REFERENCES [CATEGORY](NAME)" +
                        ");"
        );

        db.execSQL("Insert into SANPHAM values \n" +
                "('VNAL001', 'Vé Máy Bay Khứ Hồi Từ Hà Nội Đi Chu Lai', 'Vietnam Airlines', 5, 'Hà Nội -> Chu Lai - 07h00 - 15/11/20223', 'Chu Lai -> Hà Nội - 15h00 - 18/11/2023 ', 9500000, "+R.drawable.vietnamairline+"), \n " +
                "('VJA001', 'Vé Máy Bay Từ Đã Nẵng Đi Cần Thơ', 'Vietjet Air', 4, 'Đà Nẵng -> Cần Thơ - 13h15 - 15/12/2023', '', 2338000, "+R.drawable.vietjetair+"), \n" +
                "('BBA001', 'Vé Máy Bay Khứ Hồi Từ Hồ Chí Minh Đi Hà Nội', 'Bamboo Airways', 1, 'Hồ Chí Minh -> Hà Nội - 05h55 - 20/09/2023', 'Hà Nội -> Hồ Chí Minh - 10h35 - 24/09/2023', 2049000, "+R.drawable.bamboo+"), \n" +
                "('VNAL002', 'Vé Máy Bay Từ Buôn Ma Thuột Đi Huế', 'Vietnam Airlines', 3, 'Buôn Ma Thuột -> Huế 20/09/2023', ' ', 2292000, "+R.drawable.vietnamairline+"),  \n" +
                "('BBA002', 'Vé Máy Bay Từ Đà Nẵng Đi Đà Lạt', 'Bamboo Airways', 1, 'Đã Nẵng -> Đà Lạt - 08h05 - 03/10/2023', ' ', 1110000, "+R.drawable.bamboo+"),  \n" +
                "('VJA002', 'Vé Máy May Khứ Hồi Từ Pleiku Đi Vinh', 'Vietjet Air', 3, 'Pleiku -> Vinh - 18h00 - 01/10/2023', 'Vinh -> Pleiku - 04h00 - 05/10/2023', 6500000, "+R.drawable.vietjetair+"),  \n" +
                "('VNAL003', 'Vé Máy Bay Khứ Hồi Từ Quy Nhơn Đi Hải Phòng', 'Vietnam Airlines', 6, 'Quy Nhơn -> Hải Phòng 27/09/2023', 'Hải Phòng -> Quy Nhơn 30/09/2023', 7855000, "+R.drawable.vietnamairline+"),  \n" +
                "('VJA003', 'Vé Máy Bay Từ Hà Nội Đi Phú Quốc', 'Vietjet Air', 2, 'Hà Nội -> Phú Quốc - 14h45 - 21/12/2023', '', 3838000, "+R.drawable.vietjetair+"),  \n" +
                "('BBA003', 'Vé Máy Bay Khứ Hồi Từ Buôn Ma Thuột đi Hồ Chí Minh', 'Bamboo Airways', 3, 'Buồn Ma Thuột -> Hồ Chí Minh - 18h10 -17/10/2023', 'Hồ Chí Minh -> Buôn Ma Thuột - 07h00 - 18/10/2023', 1080000, "+R.drawable.bamboo+"),  \n" +
                "('VNAL004', 'Vé Máy Bay Từ Thanh Hóa Đi Cà Mau', 'Vietnam Airlines', 2, 'Thanh Hóa -> Cà Mau 23/09/2023', ' ', 3902000, "+R.drawable.vietnamairline+"),  \n" +
                "('VJA004', 'Vé Máy Bay Từ Đà Lạt Đi Vinh', 'Vietjet Air', 0, 'Đà Lạt -> Vinh - 10h15 - 07/11/2023', '', 2176000, "+R.drawable.vietjetair+"),  \n" +
                "('BBA004', 'Vé Máy Bay Từ Cần Thơ Đi Phú Quốc', 'Bamboo Airways', 10, 'Cần Thơ -> Phú Quốc - 12h35 - 02/12/2023', '', 1626000, "+R.drawable.bamboo+");");

        //region Tạo bảng BILL: Lưu trữ các hóa đơn của người mua
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS BILL (\n" +
                        "   ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "   DATEORDER date NOT NULL,\n" +
                        "   TAIKHOANCUS VARCHAR NOT NULL,\n" +
                        "   NAMECUS VARCHAR NOT NULL,\n" +
                        "   ADDRESSDELIVERRY VARCHAR NOT NULL,\n" +
                        "   SDT VARCHAR not null);"
        );
        //endregion

        //region Tạo bảng Bill_Detail: Chi tiết hóa đơn
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS BILLDETAIL (\n" +
                        "    ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "    MASP VARCHAR NOT NULL,\n" +
                        "    IDORDER   INTEGER not NULL,\n" +
                        "    IDVoucher VARCHAR not null, \n" +
                        "    QUANTITY  INTEGER check(QUANTITY > 0) not NULL,\n" +
                        "    UNITPRICE Real check(UNITPRICE > 0) not NULL,\n" +
                        "    TOTALPRICE Real check (TOTALPRICE > 0) not Null,\n" +
                        "    FOREIGN KEY (MASP) REFERENCES SANPHAM(MASP),\n" +
                        "    FOREIGN KEY (IDORDER) REFERENCES BILL(ID), \n" +
                        "    FOREIGN KEY (IDVoucher) REFERENCES VOUCHER(MAVOUCHER)" +
                        ");"
        );

        //endregion

        //region Tạo bảng VOUCHER: Lưu trữ các voucher hiện có
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS VOUCHER(\n" +
                        "\tMAVOUCHER VARCHAR PRIMARY KEY not null,\n" +
                        "\tNOIDUNG TEXT," +
                        "\tHSD date," +
                        "\tGIAM INTEGER DEFAULT(1) Check(GIAM >= 0)\n" +
                        ");"
        );
        int year = LocalDate.now().getYear();
        db.execSQL("Insert into VOUCHER values \n" +
                "('SALET5', 'Sale tháng 5', '2023-05-31' , 10.0/100), \n" +
                "('SALENEW', 'Sale mới', '2023-05-31' , 30.0/100), \n" +
                "('TVBTRAN', 'Sale báo', '2023-02-31' , 15.0/100), \n" +
                "('TVBTRAN19T2', 'Vẫn là báo sale', '2023-04-31' , 20.0/100)");

        //endregion

        //region Tạo bảng VOUCHER DETAIL: Chi tiết voucher sử dụng cho một hoặc nhiều sản phẩm cụ thể
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS VOUCHER_DETAIL(\n" +
                        "MAVOUCHER VARCHAR," +
                        "MASP VARCHAR NOT NULL," +
                        "FOREIGN KEY (MAVOUCHER) REFERENCES VOUCHER(MAVOUCHER)," +
                        "  FOREIGN KEY (MASP) REFERENCES SANPHAM(MASP)" +
                        ");"
        );
        db.execSQL("Insert into VOUCHER_DETAIL values " +
                "('SALET5', 'CB001'), " +
                "('SALET5', 'CB002'), " +
                "('SALET5', 'CB003') ");
        //endregion
        //region Tạo bảng CARTLIST: Lưu trữ giỏ hàng của người dùng, tự động cập nhật khi người dùng đăng nhập lại
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS CARTLIST (\n" +
                        "\tIDCARTLIST   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "\tIDCUS        VARCHAR,\n" +
                        "\tIDSANPHAM    VARCHAR NOT NULL,\n" +
                        "\tIDVoucher    VARCHAR,\n" +
                        "\tSOLUONG      INTEGER CHECK(SOLUONG > 0) NOT NULL," +
                        "\tDONGIA       REAL,\n" +
                        "\tFOREIGN KEY (IDCUS) REFERENCES ACCOUNT(TAIKHOAN),\n" +
                        "\tFOREIGN KEY (IDSANPHAM) REFERENCES SANPHAM(MASP), \n" +
                        "\tFOREIGN KEY (IDVoucher) REFERENCES VOUCHER(MAVOUCHER)\n" +
                        ")"
        );
        //endregion
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //Thêm nội dung vào db
    private boolean CheckExists(String PrimaryColumn, String TableName){
        Cursor i = this.GetData("Select* from " + TableName);
        while (i.moveToNext()){
            if (PrimaryColumn.equals(i.getString(0))){
                return false;
            }
        }
        return true;
    }
    public boolean AddAccount(String taikhoan, String matkhau, String quyenhan, String ten, String sdt, String gmail,String diachi){
        boolean check = CheckExists(taikhoan, "Account");
        if (check){
            this.WriteQuery("Insert into ACCOUNT Values" +
                    "('" + taikhoan + "', '" + matkhau + "', '" + quyenhan + "', '" + ten + "', '" + sdt + "', '" + gmail + "','" + diachi + "');");
        }
        return check;
    }
    public boolean AddRole(String role, String content){
        boolean check = CheckExists(role, "[ROLE]");
        if (check){
            this.WriteQuery("Insert into [ROLE] Values" +
                    "('" + role + "', '" + content + "');");
        }
        return check;
    }
    public boolean AddProduct(String MASP, String TENSP, String PHANLOAI, Integer SOLUONG, String NOINHAP, String NOIDUNG, double DONGIA, int HINHANH){
        boolean check = CheckExists(MASP, "SANPHAM");
        if (check){
            LocalDate currentDate = LocalDate.now(); //định dạng ngày sẽ là "YYYY/MM/dd"
            this.WriteQuery("Insert into SANPHAM Values" +
                    "('" + MASP + "', '" + TENSP + "', '" + PHANLOAI + "', '" + SOLUONG + "', '" + NOINHAP + "', '" + NOIDUNG + "', '" + DONGIA + "', '" + HINHANH + "', '" + currentDate + "');");
        }
        return check;
    }
    public boolean AddCategory(String NAME, String CONTENT){
        boolean check = CheckExists(NAME, "[CATEGORY]");
        if (check){
            this.WriteQuery("Insert into [CATEGORY] Values" +
                    "('" + NAME + "', '" + CONTENT + "');");
        }
        return check;
    }
    public boolean AddBill(String TAIKHOANCUS, String ADDRESSDELIVERRY) {
        try {
            Calendar c = Calendar.getInstance();
            String DATEORDER = Integer.toString(c.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(c.get(Calendar.MONTH) + 1) + "/" + Integer.toString(c.get(Calendar.YEAR));
            SQLiteDatabase db = getReadableDatabase();
            this.WriteQuery("Insert into [CATEGORY] (DATEORDER, TAIKHOANCUS, ADDRESSDELIVERRY) Values" +
                    "('" + DATEORDER + "', '" + TAIKHOANCUS + "', '" + ADDRESSDELIVERRY + "');");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean AddVoucher(String MAVOUCHER, String NOIDUNG, String HSD, double GIAM){
        boolean check = CheckExists(MAVOUCHER, "VOUCHER");
        if (check){
            this.WriteQuery("Insert into VOUCHER values" +
                    "('"+ MAVOUCHER +"', '"+ NOIDUNG +"', '"+ HSD +"', "+ GIAM +");");
        }
        return check;
    }
    public boolean AddVoucherProduct(String MAVOUCHER, String MASP){
        try{
            Cursor cursor = this.GetData(
                    "Select*" +
                            " From VOUCHER_DETAIL" +
                            " Where MAVOUCHER = '" + MAVOUCHER + "'" +
                            " and MASP = '" + MASP + "'"
            );
            if (!cursor.moveToFirst()){
                this.WriteQuery("Insert into VOUCHER_DETAIL values" +
                        "('" + MAVOUCHER + "', '" + MASP + "')");
                return true;
            }
            return false;
        }
        catch (Exception e){
            return false;
        }
    }
    public long addCartList(GioHang gioHang) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IDCUS", gioHang.getIdCus());
        values.put("IDSANPHAM", gioHang.getIdSanPham());
        values.put("IDVoucher", gioHang.getIdVoucher());
        values.put("SOLUONG", gioHang.getSoLuong());
        values.put("DONGIA", gioHang.getDonGia());
        return db.insert("CARTLIST", "IDCUS", values);
    }
    public long updateCartList(GioHang gioHang) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IDCARTLIST", gioHang.getIdCartList());
        values.put("IDCUS", gioHang.getIdCus());
        values.put("IDSANPHAM", gioHang.getIdSanPham());
        values.put("IDVoucher", gioHang.getIdVoucher());
        values.put("SOLUONG", gioHang.getSoLuong());
        values.put("DONGIA", gioHang.getDonGia());

        long kq = db.update("CARTLIST", values, "IDCARTLIST=?",  new String[]{gioHang.getIdCartList().toString()});
        db.close();
        if(kq <= 0){
            return -1;//Thêm thất bại
        }
        return 1;//Thêm thành công
    }

    public void updateUser(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//            values.put("TAIKHOAN",account.getTAIKHOAN());
//            values.put("MATKHAU",account.getMATKHAU());
//            values.put("QUYENHAN",account.getQUYENHAN());
        values.put("TEN", account.getTEN());
        values.put("SDT", account.getSDT());
        values.put("GMAIL", account.getGMAIL());
        values.put("DIACHI",account.getDIACHI());

        long check = db.update("ACCOUNT",values,"TAIKHOAN=?",new String[]{account.getTAIKHOAN().toString()});
        //long check = db.insert("ACCOUNT",null,values);
//            if (check != -1){
//                Toast.makeText(context,"Saved", Toast.LENGTH_SHORT).show();
//                db.close();
//
//            }else {
//                Toast.makeText(context, "Chỉnh sửa thất bại", Toast.LENGTH_SHORT).show();
//            }
    }

    public Cursor getUser(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from ACCOUNT",null);
        return cursor;
    }

    public long changePassword(String user, String oldPass, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MATKHAU", newPass);
        return db.update("ACCOUNT", values, "TAIKHOAN=?", new String[]{user});
    }
}
