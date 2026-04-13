/*
 * @ (#) PhieuNhap.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class PhieuNhap {
    private final String MaPhieuNhap;
    private String nhaCungCap;
    private LocalDate ngayNhap;
    private String diaChi;
    private String lyDo;
    private double tongTien;
    private NhanVien maNV;
    private boolean deleteAt;

    public PhieuNhap() {
        MaPhieuNhap = "";
        nhaCungCap = "";
        ngayNhap = LocalDate.now();
        diaChi = "";
        lyDo = "";
        maNV = new NhanVien();
        deleteAt = false;
    }

    public PhieuNhap(String maPN) {
        MaPhieuNhap = maPN;
        nhaCungCap = "";
        ngayNhap = LocalDate.now();
        diaChi = "";
        lyDo = "";
        maNV = new NhanVien();
        deleteAt = false;
    }

    public PhieuNhap(String maPhieuNhap, String nhaCungCap, LocalDate ngayNhap, String diaChi, String lyDo, NhanVien maNV, double tongTien, boolean deleteAt) {
        MaPhieuNhap = maPhieuNhap;
        setNhaCungCap(nhaCungCap);
        setNgayNhap(ngayNhap);
        setDiaChi(diaChi);
        setLyDo(lyDo);
        setTongTien(tongTien);
        this.maNV = maNV;
        this.deleteAt = deleteAt;
    }
    public String getMaPhieuNhap() {
        return MaPhieuNhap;
    }
    public String getNhaCungCap() {
        return nhaCungCap;
    }
    public void setNhaCungCap(String nhaCungCap) {
        if (nhaCungCap == null || nhaCungCap.isEmpty()) {
            throw new IllegalArgumentException("Nhà cung cấp không được để trống");
        }
        this.nhaCungCap = nhaCungCap;
    }
    public LocalDate getNgayNhap() {
        return ngayNhap;
    }
    public void setNgayNhap(LocalDate ngayNhap) {
        if (ngayNhap.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày nhập không được sau ngày hiện tại");
        }
        this.ngayNhap = ngayNhap;
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
    public String getLyDo() {
        return lyDo;
    }
    public void setLyDo(String lyDo) {
        if (lyDo == null || lyDo.isEmpty()) {
            throw new IllegalArgumentException("Lý do không được để trống");
        }
        this.lyDo = lyDo;
    }
    public NhanVien getMaNV() {
        return maNV;
    }
    public void setMaNV(NhanVien maNV) {
        this.maNV = maNV;
    }

    public double getTongTien() {
        return tongTien;
    }
    public void setTongTien(double tongTien) {
        if (tongTien < 0) {
            throw new IllegalArgumentException("Tổng tiền không được âm");
        }
        this.tongTien = tongTien;
    }
    public boolean isDeleteAt() {
        return deleteAt;
    }
    public void setDeleteAt(boolean deleteAt) {
        this.deleteAt = deleteAt;
    }

    @Override
    public String toString() {
        return "PhieuNhap{" +
                "MaPhieuNhap='" + MaPhieuNhap + '\'' +
                ", nhaCungCap='" + nhaCungCap + '\'' +
                ", ngayNhap=" + ngayNhap +
                ", diaChi='" + diaChi + '\'' +
                ", lyDo='" + lyDo + '\'' +
                ", maNV=" + maNV +
                ", deleteAt=" + deleteAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhieuNhap phieuNhap = (PhieuNhap) o;
        return Objects.equals(MaPhieuNhap, phieuNhap.MaPhieuNhap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MaPhieuNhap);
    }
}
