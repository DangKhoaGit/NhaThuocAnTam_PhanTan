/*
 * @ (#) ChiTietPhieuDatThuoc.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Builder
@IdClass(ChiTietPhieuDatThuoc.ChiTietPhieuDatThuocId.class)
@Entity
@Table(name = "ChiTietPhieuDatThuoc")
public class ChiTietPhieuDatThuoc {
    @Id
    @ManyToOne
    @JoinColumn(name = "MaPDT")
    @EqualsAndHashCode.Include
    private PhieuDatThuoc maPhieu;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaLoThuoc")
    @EqualsAndHashCode.Include
    private LoThuoc maThuoc;

    private int soLuong;

    @ManyToOne
    @JoinColumn(name = "MaDVT")
    private DonViTinh donViTinh;

    private double thanhTien;

    @Column(name = "TinhTrang", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isThanhToan = false;


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

    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
        setThanhTien();
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
                ", maThuoc=" + maThuoc +
                ", soLuong=" + soLuong +
                ", donViTinh=" + donViTinh +
                '}';
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    @Builder
    public static class ChiTietPhieuDatThuocId implements Serializable {
        private String maPhieu;
        private int maThuoc;

    }
}