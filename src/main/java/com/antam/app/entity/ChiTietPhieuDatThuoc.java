/*
 * @ (#) ChiTietPhieuDatThuoc.java   1.0 9/25/2025
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
public class ChiTietPhieuDatThuoc {
    private PhieuDatThuoc maPhieu;
    private LoThuoc maThuoc;
    private int soLuong;
    private DonViTinh donViTinh;
    private double thanhTien;
    private boolean isThanhToan;

    public ChiTietPhieuDatThuoc() {
        this.maPhieu = new PhieuDatThuoc();
        this.maThuoc = new LoThuoc();
        this.soLuong = 0;
        this.donViTinh = new DonViTinh();
        this.thanhTien = 0;
    }

    public ChiTietPhieuDatThuoc(PhieuDatThuoc maPhieu, LoThuoc maThuoc, int soLuong, DonViTinh donViTinh) {
        this.maPhieu = maPhieu;
        this.maThuoc = maThuoc;
        setSoLuong(soLuong);
        this.donViTinh = donViTinh;
        setThanhTien();
    }

    //Constructor cho lúc khởi tạo mẫu chi tiết phiếu đặt
    public ChiTietPhieuDatThuoc(LoThuoc thuoc, int soLuong, DonViTinh dvt){
        this.maPhieu = new PhieuDatThuoc();
        this.maThuoc = thuoc;
        this.soLuong = soLuong;
        this.donViTinh = dvt;
        this.thanhTien = soLuong * thuoc.getMaThuoc().getGiaBan() * (1 + thuoc.getMaThuoc().getThue());
    }

    public ChiTietPhieuDatThuoc(PhieuDatThuoc maPhieu, LoThuoc maThuoc, int soLuong, DonViTinh donViTinh, double thanhTien) {
        this.maPhieu = maPhieu;
        this.maThuoc = maThuoc;
        setSoLuong(soLuong);
        this.donViTinh = donViTinh;
        this.thanhTien = thanhTien;
    }

    public PhieuDatThuoc getMaPhieu() {
        return maPhieu;
    }
    public void setMaPhieu(PhieuDatThuoc maPhieu) {
        this.maPhieu = maPhieu;
    }
    public LoThuoc getChiTietThuoc() {
        return maThuoc;
    }
    public void setChiTietThuoc(LoThuoc ctt) {
        this.maThuoc = ctt;
    }
    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
        setThanhTien();
    }
    public DonViTinh getDonViTinh() {
        return donViTinh;
    }
    public void setDonViTinh(DonViTinh donViTinh) {
        this.donViTinh = donViTinh;
    }
    public double getThanhTien() {
        return thanhTien;
    }
    public void setThanhTien() {
        this.thanhTien = tinhThanhTien();
    }
    public double tinhThanhTien() {
        double giaBan = maThuoc.getMaThuoc().getGiaBan();
        float thue = maThuoc.getMaThuoc().getThue();
        return soLuong * giaBan * (1 + thue);
    }

    @Override
    public String toString() {
        return "ChiTietPhieuDatThuoc{" +
                "maPhieu=" + maPhieu +
                ", soDangKy=" + maThuoc +
                ", soLuong=" + soLuong +
                ", donViTinh=" + donViTinh +
                '}';
    }

    public boolean isThanhToan() {
        return isThanhToan;
    }

    public void setThanhToan(boolean thanhToan) {
        isThanhToan = thanhToan;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ChiTietPhieuDatThuoc that = (ChiTietPhieuDatThuoc) o;
//
//        return this.maThuoc.getMaThuoc().equals(that.maThuoc.getMaThuoc())
//                && this.donViTinh.getMaDVT() == that.donViTinh.getMaDVT();
//    }
//
//    @Override
//    public int hashCode() {
//        int result = maThuoc.getMaThuoc().hashCode();
//        result = 31 * result + donViTinh.getMaDVT();  // maDVT là int → dùng trực tiếp
//        return result;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietPhieuDatThuoc that = (ChiTietPhieuDatThuoc) o;
        return Objects.equals(maThuoc, that.maThuoc) && Objects.equals(donViTinh, that.donViTinh);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maThuoc, donViTinh);
    }
}