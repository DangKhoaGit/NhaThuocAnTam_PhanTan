/*
 * @ (#) ChiTietThuoc.java   1.0 10/6/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.time.LocalDate;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/6/2025
 * version: 1.0
 */
public class LoThuoc {
    private final int maCTT;
    private PhieuNhap maPN;
    private Thuoc maThuoc;
    private int soLuong;
    private LocalDate hanSuDung;
    private LocalDate ngaySanXuat;

    public LoThuoc() {
        this.maCTT = 0;
        this.maPN = new PhieuNhap();
        this.maThuoc = new Thuoc();
        this.soLuong = 0;
        this.hanSuDung = LocalDate.now();
        this.ngaySanXuat = LocalDate.now();
    }

    public LoThuoc(int maCTT) {
        this.maCTT = maCTT;
        this.maPN = new PhieuNhap();
        this.maThuoc = new Thuoc();
        this.soLuong = 0;
        this.hanSuDung = LocalDate.now();
        this.ngaySanXuat = LocalDate.now();
    }

    public LoThuoc(int maCTT, PhieuNhap maPN, Thuoc maThuoc, int soLuong, LocalDate hanSuDung, LocalDate ngaySanXuat) {
        this.maCTT = maCTT;
        this.maPN = maPN;
        this.maThuoc = maThuoc;
        setSoLuong(soLuong);
        setNgaySanXuat(ngaySanXuat);
        setHanSuDung(hanSuDung);
    }

    public int getMaCTT() {
        return maCTT;
    }

    public PhieuNhap getMaPN() {
        return maPN;
    }
    public void setMaPN(PhieuNhap maPN) {
        this.maPN = maPN;
    }
    public Thuoc getMaThuoc() {
        return maThuoc;
    }
    public void setMaThuoc(Thuoc maThuoc) {
        this.maThuoc = maThuoc;
    }
    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
    }
    public LocalDate getHanSuDung() {
        return hanSuDung;
    }
    public void setHanSuDung(LocalDate hanSuDung) {
        if (hanSuDung.isBefore(ngaySanXuat)) {
            throw new IllegalArgumentException("Hạn sử dụng không được trước ngày sản xuất");
        }
        this.hanSuDung = hanSuDung;
    }
    public LocalDate getNgaySanXuat() {
        return ngaySanXuat;
    }
    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        if (LocalDate.now().isBefore(ngaySanXuat)) {
            throw new IllegalArgumentException("Ngày sản xuất không được sau ngày hiện tại");
        }
        this.ngaySanXuat = ngaySanXuat;
    }

    @Override
    public String toString() {
        return "ChiTietThuoc{" +
                "maCTT=" + maCTT +
                ", maPN=" + (maPN != null ? maPN.getMaPhieuNhap() : "null") +
                ", maThuoc=" + (maThuoc != null ? maThuoc.getTenThuoc() : "null") +
                ", soLuong=" + soLuong +
                ", hanSuDung=" + hanSuDung +
                ", ngaySanXuat=" + ngaySanXuat +
                '}';
    }

}
