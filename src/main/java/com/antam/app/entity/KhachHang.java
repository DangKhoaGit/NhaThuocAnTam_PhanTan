/*
 * @ (#) KhachHang.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.time.LocalDate;
import java.util.Objects;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class KhachHang {
    // Các field có trong database
    private final String MaKH;
    private String tenKH;
    private String soDienThoai;
    private boolean deleteAt;

    // Các field tính toán cho thống kê (không lưu trong DB)
    private double tongChiTieu;
    private int soDonHang;
    private LocalDate ngayMuaGanNhat;

    public KhachHang() {
        MaKH = "";
        tenKH = "";
        soDienThoai = "";
        deleteAt = false;
        tongChiTieu = 0;
        soDonHang = 0;
        ngayMuaGanNhat = null;
    }

    public KhachHang(String maKH) {
        MaKH = maKH;
        tenKH = "";
        soDienThoai = "";
        deleteAt = false;
        tongChiTieu = 0;
        soDonHang = 0;
        ngayMuaGanNhat = null;
    }

    public KhachHang(String maKH, String tenKH, String soDienThoai, boolean deleteAt) {
        MaKH = maKH;
        setTenKH(tenKH);
        setSoDienThoai(soDienThoai);
        this.deleteAt = deleteAt;
        this.tongChiTieu = 0;
        this.soDonHang = 0;
        this.ngayMuaGanNhat = null;
    }

    public String getMaKH() {
        return MaKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        if (tenKH == null || tenKH.isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
        }
        this.tenKH = tenKH;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        } else if (!soDienThoai.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        this.soDienThoai = soDienThoai;
    }

    public boolean isDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(boolean deleteAt) {
        this.deleteAt = deleteAt;
    }

    // Getter và setter cho các field thống kê
    public double getTongChiTieu() {
        return tongChiTieu;
    }

    public void setTongChiTieu(double tongChiTieu) {
        this.tongChiTieu = tongChiTieu;
    }

    public int getSoDonHang() {
        return soDonHang;
    }

    public void setSoDonHang(int soDonHang) {
        this.soDonHang = soDonHang;
    }

    public LocalDate getNgayMuaGanNhat() {
        return ngayMuaGanNhat;
    }

    public void setNgayMuaGanNhat(LocalDate ngayMuaGanNhat) {
        this.ngayMuaGanNhat = ngayMuaGanNhat;
    }

    // Phương thức tiện ích để lấy loại khách hàng
    public String getLoaiKhachHang() {
        // VIP nếu tổng chi tiêu > 5.000.000 hoặc số đơn hàng > 20
        if (tongChiTieu > 5_000_000 || soDonHang > 20) {
            return "VIP";
        } else {
            return "Thường";
        }
    }

    @Override
    public String toString() {
        return tenKH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KhachHang khachHang = (KhachHang) o;
        return Objects.equals(MaKH, khachHang.MaKH);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MaKH);
    }
}
