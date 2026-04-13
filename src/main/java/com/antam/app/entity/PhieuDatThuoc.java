/*
 * @ (#) PhieuDatThuoc.java   1.0 9/25/2025
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
public class PhieuDatThuoc {
    private final String maPhieu;
    private LocalDate ngayTao;
    private boolean isThanhToan;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private KhuyenMai khuyenMai;
    private double tongTien;
    private boolean daXoa;

    public boolean isDaXoa() {
        return daXoa;
    }

    public void setDaXoa(boolean daXoa) {
        this.daXoa = daXoa;
    }

    public PhieuDatThuoc() {
        this.maPhieu = "";
        this.ngayTao = LocalDate.now();
        this.isThanhToan = false;
        this.nhanVien = new NhanVien();
        this.khachHang = new KhachHang();
        this.khuyenMai = new KhuyenMai("", "Không áp dụng");
    }

    public PhieuDatThuoc(String maPhieu, LocalDate ngayTao, boolean isThanhToan, NhanVien nhanVien, KhachHang maKH, KhuyenMai maKM, double tongTien) {
        this.maPhieu = maPhieu;
        setNgayTao(ngayTao);
        this.isThanhToan = isThanhToan;
        this.nhanVien = nhanVien;
        this.khachHang = maKH;
        this.khuyenMai = maKM;
        this.tongTien = tongTien;
    }

    public PhieuDatThuoc(String maPhieu, LocalDate ngayTao, boolean isThanhToan, NhanVien nhanVien, KhachHang maKH, KhuyenMai maKM, double tongTien,boolean daXoa) {
        this.maPhieu = maPhieu;
        setNgayTao(ngayTao);
        this.isThanhToan = isThanhToan;
        this.nhanVien = nhanVien;
        this.khachHang = maKH;
        this.khuyenMai = maKM;
        this.tongTien = tongTien;
        this.daXoa = daXoa;
    }

    public String getMaPhieu() {
        return maPhieu;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        if (ngayTao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày tạo không được sau ngày hiện tại");
        }
        this.ngayTao = ngayTao;
    }

    public boolean isThanhToan() {
        return isThanhToan;
    }

    public void setThanhToan(boolean isThanhToan) {
        this.isThanhToan = isThanhToan;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    @Override
    public String toString() {
        return "PhieuDatThuoc{" +
                "maPhieu='" + maPhieu + '\'' +
                ", ngayTao=" + ngayTao +
                ", isThanhToan=" + isThanhToan +
                ", nhanVien=" + nhanVien +
                ", khachHang=" + khachHang +
                ", khuyenMai=" + khuyenMai +
                ", tongTien=" + tongTien +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhieuDatThuoc that = (PhieuDatThuoc) o;
        return Objects.equals(maPhieu, that.maPhieu);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(maPhieu);
    }
}
