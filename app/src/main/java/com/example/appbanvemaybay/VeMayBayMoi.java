package com.example.appbanvemaybay;

import java.io.Serializable;

public class VeMayBayMoi implements Serializable {
    String MASP, TENSP, PHANLOAI, NOIDEN, NOIVE;
    Integer SOLUONG, HINHANH;
    Long DONGIA;

    public VeMayBayMoi(){ }
    public VeMayBayMoi(String MASP, String TENSP, String PHANLOAI, Integer SOLUONG, String NOIDEN, String NOIVE, Long DONGIA, Integer HINHANH) {
        this.MASP = MASP;
        this.TENSP = TENSP;
        this.PHANLOAI = PHANLOAI;
        this.NOIDEN = NOIDEN;
        this.NOIVE = NOIVE;
        this.SOLUONG = SOLUONG;
        this.HINHANH = HINHANH;
        this.DONGIA = DONGIA;

    }


    public String getMASP() {
        return MASP;
    }

    public void setMASP(String MASP) {
        this.MASP = MASP;
    }

    public String getTENSP() {
        return TENSP;
    }

    public void setTENSP(String TENSP) {
        this.TENSP = TENSP;
    }

    public String getPHANLOAI() {
        return PHANLOAI;
    }

    public void setPHANLOAI(String PHANLOAI) {
        this.PHANLOAI = PHANLOAI;
    }

    public String getNOIDEN() {
        return NOIDEN;
    }

    public void setNOIDEN(String NOIDEN) {
        this.NOIDEN = NOIDEN;
    }

    public String getNOIVE() {
        return NOIVE;
    }

    public void setNOIVE(String NOIVE) {
        this.NOIVE = NOIVE;
    }

    public Integer getSOLUONG() {
        return SOLUONG;
    }

    public void setSOLUONG(Integer SOLUONG) {
        this.SOLUONG = SOLUONG;
    }

    public Integer getHINHANH() {
        return HINHANH;
    }

    public void setHINHANH(Integer HINHANH) {
        this.HINHANH = HINHANH;
    }

    public Long getDONGIA() {
        return DONGIA;
    }

    public void setDONGIA(Long DONGIA) {
        this.DONGIA = DONGIA;
    }
}
