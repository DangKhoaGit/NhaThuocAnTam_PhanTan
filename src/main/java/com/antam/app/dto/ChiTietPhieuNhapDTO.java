/*
 * @ (#) ChiTietPhieuNhap.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

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

public class ChiTietPhieuNhapDTO {

    private int soLuong;
    private double giaNhap;
    private double thanhTien;

    private PhieuNhapDTO MaPN;

    private LoThuocDTO loThuocDTO;

    private DonViTinhDTO maDVT;


    public ChiTietPhieuNhapDTO(PhieuNhapDTO maPN, LoThuocDTO maLoThuocDTO, DonViTinhDTO maDVT, int soLuong, double giaNhap) {
        this.MaPN = maPN;
        this.loThuocDTO = maLoThuocDTO;
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
        if (this.loThuocDTO == null || this.loThuocDTO.getMaThuocDTO() == null) {
            this.thanhTien = this.soLuong * this.giaNhap;
            return;
        }
        this.thanhTien = thanhTien();
    }


    public double thanhTien() {
        float thue = loThuocDTO.getMaThuocDTO().getThue();
        return soLuong * giaNhap * (1 + thue);
    }
    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "MaPN=" + MaPN +
                ", MaThuoc=" + loThuocDTO.getMaThuocDTO() +
                ", maDVT=" + maDVT +
                ", soLuong=" + soLuong +
                ", giaNhap=" + giaNhap +
                '}';
    }

}
