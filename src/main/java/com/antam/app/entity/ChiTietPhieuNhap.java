/*
 * @ (#) ChiTietPhieuNhap.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import jakarta.persistence.*;
import lombok.*;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "ChiTietPhieuNhap")
@IdClass(ChiTietPhieuNhap.ChiTietPhieuNhapId.class)
public class ChiTietPhieuNhap {

    @Column(name = "SoLuong")
    private int soLuong;
    @Column(name = "GiaNhap")
    private double giaNhap;
    @Column(name = "ThanhTien")
    private double thanhTien;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaPN")
    private PhieuNhap MaPN;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaLoThuoc")
    private LoThuoc loThuoc;

    @ManyToOne()
    @JoinColumn(name = "MaDVT")
    private DonViTinh maDVT;


    public ChiTietPhieuNhap(PhieuNhap maPN, LoThuoc maLoThuoc, DonViTinh maDVT, int soLuong, double giaNhap) {
        this.MaPN = maPN;
        this.loThuoc = maLoThuoc;
        this.maDVT = maDVT;
        setSoLuong(soLuong);
        setGiaNhap(giaNhap);
        setThanhTien();
    }


    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
        setThanhTien();
    }


    public void setThanhTien() {
        this.thanhTien = thanhTien();
    }


    public double thanhTien() {
        float thue = loThuoc.getMaThuoc().getThue();
        return soLuong * giaNhap * (1 + thue);
    }
    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "MaPN=" + MaPN +
                ", MaThuoc=" + loThuoc.getMaThuoc() +
                ", maDVT=" + maDVT +
                ", soLuong=" + soLuong +
                ", giaNhap=" + giaNhap +
                '}';
    }

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @ToString
    public static class ChiTietPhieuNhapId implements java.io.Serializable {
        private PhieuNhap MaPN;
        private LoThuoc loThuoc;
    }
}
