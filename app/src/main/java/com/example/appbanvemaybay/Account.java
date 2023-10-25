package com.example.appbanvemaybay;

public class Account {
    private String TAIKHOAN;
    private String MATKHAU;
    private String QUYENHAN;
    private String TEN;
    private String SDT;
    private String GMAIL;
    private String DIACHI;

    public Account(String TAIKHOAN, String MATKHAU, String QUYENHAN, String TEN, String SDT, String GMAIL, String DIACHI) {
        this.TAIKHOAN = TAIKHOAN;
        this.MATKHAU = MATKHAU;
        this.QUYENHAN = QUYENHAN;
        this.TEN = TEN;
        this.SDT = SDT;
        this.GMAIL = GMAIL;
        this.DIACHI = DIACHI;
    }
    public  Account(){}


    public String getTAIKHOAN() {
        return TAIKHOAN;
    }

    public void setTAIKHOAN(String TAIKHOAN) {
        this.TAIKHOAN = TAIKHOAN;
    }

    public String getMATKHAU() {
        return MATKHAU;
    }

    public void setMATKHAU(String MATKHAU) {
        this.MATKHAU = MATKHAU;
    }

    public String getQUYENHAN() {return QUYENHAN;}

    public void setQUYENHAN(String QUYENHAN) {
        this.QUYENHAN = QUYENHAN;
    }
    public String getTEN() {
        return TEN;
    }

    public void setTEN(String TEN) {
        this.TEN = TEN;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getGMAIL() {
        return GMAIL;
    }

    public void setGMAIL(String GMAIL) {
        this.GMAIL = GMAIL;
    }

    public String getDIACHI() {
        return DIACHI;
    }

    public void setDIACHI(String DIACHI) {
        this.DIACHI = DIACHI;
    }
}
