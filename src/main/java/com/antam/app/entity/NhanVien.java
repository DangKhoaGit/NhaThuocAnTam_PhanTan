/*
 * @ (#) NhanVien.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.util.Objects;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class NhanVien {
    private final String MaNV;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private double luongCoBan;
    private String taiKhoan;
    private String matKhau;
    private boolean isQuanLy;
    private boolean deleteAt;

    public NhanVien() {
        MaNV = "";
        hoTen = "";
        soDienThoai = "";
        email = "";
        diaChi = "";
        luongCoBan = 0;
        taiKhoan = "";
        matKhau = "";
        isQuanLy = false;
        deleteAt = false;
    }

    public NhanVien(String MaNhanVien) {
        this.MaNV = MaNhanVien;
        hoTen = "";
        soDienThoai = "";
        email = "";
        diaChi = "";
        luongCoBan = 0;
        taiKhoan = "";
        matKhau = "";
        isQuanLy = false;
        deleteAt = false;
    }

    public NhanVien(String MaNhanVien, String HoTen) {
        this.MaNV = MaNhanVien;
        this.hoTen = HoTen;
        soDienThoai = "";
        email = "";
        diaChi = "";
        luongCoBan = 0;
        taiKhoan = "";
        matKhau = "";
        isQuanLy = false;
        deleteAt = false;
    }

    public NhanVien(String maNV, String hoTen, String soDienThoai, String email, String diaChi, double luongCoBan, String taiKhoan, String matKhau, boolean deleteAt, boolean isQuanLy) {
        MaNV = maNV;
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setEmail(email);
        setDiaChi(diaChi);
        setLuongCoBan(luongCoBan);
        setTaiKhoan(taiKhoan);
        setMatKhau(matKhau);
        this.deleteAt = deleteAt;
        this.isQuanLy = isQuanLy;
    }
    public NhanVien(String maNV, String hoTen, String soDienThoai, String email, String diaChi, double luongCoBan, String taiKhoan, String matKhau, boolean isQuanLy) {
        MaNV = maNV;
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setEmail(email);
        setDiaChi(diaChi);
        setLuongCoBan(luongCoBan);
        setTaiKhoan(taiKhoan);
        setMatKhau(matKhau);
        this.deleteAt = false;
        this.isQuanLy = isQuanLy;
    }

    /**
     * Sử dụng riêng cho đặt thuốc
     * @param hoten
     * @param ql
     */
    public NhanVien(String hoten, boolean ql){
        this.MaNV = "";
        hoTen = hoten;
        soDienThoai = "";
        email = "";
        diaChi = "";
        luongCoBan = 0;
        taiKhoan = "";
        matKhau = "";
        isQuanLy = ql;
        deleteAt = false;
    }
    public String getMaNV() {
        return MaNV;
    }
    public String getHoTen() {
        return hoTen;
    }
    public void setHoTen(String hoTen) {
        if (hoTen == null || hoTen.isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống");
        }
        this.hoTen = hoTen;
    }
    public String getSoDienThoai() {
        return soDienThoai;
    }
    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        } else if (!soDienThoai.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }

        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        this.email = email;
    }
    public String getDiaChi() {
        return diaChi;
    }
    public void setDiaChi(String diaChi) {
        if (diaChi == null || diaChi.isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống");
        }
        this.diaChi = diaChi;
    }
    public double getLuongCoBan() {
        return luongCoBan;
    }
    public void setLuongCoBan(double luongCoBan) {
        if (luongCoBan <= 0) {
            throw new IllegalArgumentException("Lương cơ bản không được âm");
        }
        this.luongCoBan = luongCoBan;
    }
    public String getTaiKhoan() {
        return taiKhoan;
    }
    public void setTaiKhoan(String taiKhoan) {
        if (taiKhoan == null || taiKhoan.isEmpty()) {
            throw new IllegalArgumentException("Tài khoản không được để trống");
        }
        this.taiKhoan = taiKhoan;
    }
    public String getMatKhau() {
        return matKhau;
    }
    public void setMatKhau(String matKhau) {
        if (matKhau == null || matKhau.isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        this.matKhau = matKhau;
    }
    public boolean isQuanLy() {
        return isQuanLy;
    }
    public void setQuanLy(boolean quanLy) {
        isQuanLy = quanLy;
    }
    public boolean isDeleteAt() {
        return deleteAt;
    }
    public void setDeleteAt(boolean deleteAt) {
        this.deleteAt = deleteAt;
    }

    @Override
    public String toString() {
        return hoTen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NhanVien nhanVien = (NhanVien) o;
        return Objects.equals(MaNV, nhanVien.MaNV);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MaNV);
    }
}
