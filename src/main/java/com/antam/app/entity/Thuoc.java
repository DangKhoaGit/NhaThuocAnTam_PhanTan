/*
 * @ (#) Thuoc.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.time.LocalDate;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class Thuoc {
    private final String maThuoc;
    private String TenThuoc;
    private String hamLuong;
    private double giaBan;
    private double giaGoc;
    private float thue;
    private boolean deleteAt;
    private DangDieuChe dangDieuChe;
    private DonViTinh maDVTCoSo;
    private Ke maKe;

    public Thuoc() {
        this.maThuoc = "";
        this.TenThuoc = "";
        this.hamLuong = "";
        this.giaBan = 0;
        this.giaGoc = 0;
        this.thue = 0;
        this.deleteAt = false;
        this.dangDieuChe = new DangDieuChe();
        this.maDVTCoSo = new DonViTinh();
        this.maKe = new Ke();
    }

    public Thuoc(String maThuoc) {
        this.maThuoc = maThuoc;
        this.TenThuoc = "";
        this.hamLuong = "";
        this.giaBan = 0;
        this.giaGoc = 0;
        this.thue = 0;
        this.deleteAt = false;
        this.dangDieuChe = new DangDieuChe();
        this.maDVTCoSo = new DonViTinh();
        this.maKe = new Ke();
    }
    public Thuoc(String maThuoc, String tenThuoc, String hamLuong, double giaBan, double giaGoc, float thue, boolean deleteAt, DangDieuChe dangDieuChe, DonViTinh maDVTCoSo, Ke maKe) {
        this.maThuoc = maThuoc;
        setTenThuoc(tenThuoc);
        setHamLuong(hamLuong);
        setGiaBan(giaBan);
        setGiaGoc(giaGoc);
        setThue(thue);
        this.deleteAt = deleteAt;
        this.dangDieuChe = dangDieuChe;
        this.maDVTCoSo = maDVTCoSo;
        this.maKe = maKe;
    }
    public String getMaThuoc() {
        return maThuoc;
    }
    public String getTenThuoc() {
        return TenThuoc;
    }
    public void setTenThuoc(String tenThuoc) {
        if (tenThuoc == null || tenThuoc.isEmpty()) {
            throw new IllegalArgumentException("Tên thuốc không được để trống");
        }
        TenThuoc = tenThuoc;
    }
    public String getHamLuong() {
        return hamLuong;
    }
    public void setHamLuong(String hamLuong) {
        if (hamLuong == null || hamLuong.isEmpty()) {
            throw new IllegalArgumentException("Hàm lượng không được để trống");
        }
        this.hamLuong = hamLuong;
    }
    public double getGiaBan() {
        return giaBan;
    }
    public void setGiaBan(double giaBan) {
        if (giaBan <= 0) {
            throw new IllegalArgumentException("Giá bán không được âm");
        }
        this.giaBan = giaBan;
    }
    public double getGiaGoc() {
        return giaGoc;
    }
    public void setGiaGoc(double giaGoc) {
        if (giaGoc <= 0) {
            throw new IllegalArgumentException("Giá gốc không được âm");
        }
        this.giaGoc = giaGoc;
    }
    public float getThue() {
        return thue;
    }
    public void setThue(float thue) {
        if (thue < 0) {
            throw new IllegalArgumentException("Thuế không được âm");
        }
        this.thue = thue;
    }
    public DangDieuChe getDangDieuChe() {
        return dangDieuChe;
    }
    public void setDangDieuChe(DangDieuChe dangDieuChe) {
        this.dangDieuChe = dangDieuChe;
    }
    public DonViTinh getMaDVTCoSo() {
        return maDVTCoSo;
    }
    public void setMaDVTCoSo(DonViTinh maDVTCoSo) {
        this.maDVTCoSo = maDVTCoSo;
    }
    public Ke getMaKe() {
        return maKe;
    }
    public void setMaKe(Ke maKe) {
        this.maKe = maKe;
    }
    public boolean isDeleteAt() {
        return deleteAt;
    }
    public void setDeleteAt(boolean deleteAt) {
        this.deleteAt = deleteAt;
    }
    @Override
    public String toString() {
        return getTenThuoc();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thuoc thuoc = (Thuoc) o;
        return maThuoc.equals(thuoc.maThuoc);
    }
}
