package com.antam.app.entity;

import java.time.LocalDate;

/**
 * Entity class cho thống kê doanh thu
 */
public class ThongKeDoanhThu {
    private LocalDate ngay;
    private int soDonHang;
    private double doanhThu;
    private double donHangTB;
    private int khachHangMoi;
    private String nhanVienBan;

    public ThongKeDoanhThu() {
    }

    public ThongKeDoanhThu(LocalDate ngay, int soDonHang, double doanhThu, double donHangTB, int khachHangMoi, String nhanVienBan) {
        this.ngay = ngay;
        this.soDonHang = soDonHang;
        this.doanhThu = doanhThu;
        this.donHangTB = donHangTB;
        this.khachHangMoi = khachHangMoi;
        this.nhanVienBan = nhanVienBan;
    }

    public LocalDate getNgay() {
        return ngay;
    }

    public void setNgay(LocalDate ngay) {
        this.ngay = ngay;
    }

    public int getSoDonHang() {
        return soDonHang;
    }

    public void setSoDonHang(int soDonHang) {
        this.soDonHang = soDonHang;
    }

    public double getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(double doanhThu) {
        this.doanhThu = doanhThu;
    }

    public double getDonHangTB() {
        return donHangTB;
    }

    public void setDonHangTB(double donHangTB) {
        this.donHangTB = donHangTB;
    }

    public int getKhachHangMoi() {
        return khachHangMoi;
    }

    public void setKhachHangMoi(int khachHangMoi) {
        this.khachHangMoi = khachHangMoi;
    }

    public String getNhanVienBan() {
        return nhanVienBan;
    }

    public void setNhanVienBan(String nhanVienBan) {
        this.nhanVienBan = nhanVienBan;
    }
}

