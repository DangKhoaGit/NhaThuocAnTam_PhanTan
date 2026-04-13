/*
 * @ (#) HoaDon.java   1.0 9/25/2025
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
public class HoaDon {
    private final String MaHD;
    private LocalDate ngayTao;
    private NhanVien maNV;
    private KhachHang maKH;
    private KhuyenMai maKM;
    private double tongTien;
    private boolean deleteAt;

    public HoaDon(String maHD) {
        MaHD = maHD;
        ngayTao = LocalDate.now();
        maNV = new NhanVien();
        maKH = new KhachHang();
        maKM = null;
        tongTien = 0;
        deleteAt = false;
    }

    public HoaDon() {
        MaHD = "";
        ngayTao = LocalDate.now();
        maNV = new NhanVien();
        maKH = new KhachHang();
        maKM = null;
        tongTien = 0;
        deleteAt = false;
    }

    public HoaDon(String maHD, LocalDate ngayTao, NhanVien maNV, KhachHang maKH, KhuyenMai maKM, double tongTien , boolean deleteAt) {
        MaHD = maHD;
        setNgayTao(ngayTao);
        this.maNV = maNV;
        this.maKH = maKH;
        this.maKM = maKM;
        this.tongTien = tongTien;
        this.deleteAt = deleteAt;
    }
    public String getMaHD() {
        return MaHD;
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
    public NhanVien getMaNV() {
        return maNV;
    }
    public void setMaNV(NhanVien maNV) {
        this.maNV = maNV;
    }
    public KhachHang getMaKH() {
        return maKH;
    }
    public void setMaKH(KhachHang maKH) {
        this.maKH = maKH;
    }
    public KhuyenMai getMaKM() {
        return maKM;
    }
    public void setMaKM(KhuyenMai maKM) {
        this.maKM = maKM;
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
        return "HoaDon{" +
                "MaHD='" + MaHD + '\'' +
                ", ngayTao=" + ngayTao +
                ", maNV=" + maNV +
                ", maKH=" + maKH +
                ", maKM=" + maKM +
                ", tongTien=" + tongTien +
                ", deleteAt=" + deleteAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(MaHD, hoaDon.MaHD);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MaHD);
    }
}
