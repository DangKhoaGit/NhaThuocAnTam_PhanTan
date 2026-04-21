/*
 * @ (#) ChiTietPhieuDatThuoc.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

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
@Builder

public class ChiTietPhieuDatThuocDTO implements Serializable {
    private PhieuDatThuocDTO maPhieu;
    private LoThuocDTO maThuoc;
    private int soLuong;
    private DonViTinhDTO donViTinhDTO;

    private double thanhTien;

    private boolean isThanhToan = false;


    public ChiTietPhieuDatThuocDTO(PhieuDatThuocDTO maPhieu, LoThuocDTO maThuoc, int soLuong, DonViTinhDTO donViTinhDTO) {
        this.maPhieu = maPhieu;
        this.maThuoc = maThuoc;
        setSoLuong(soLuong);
        this.donViTinhDTO = donViTinhDTO;
        setThanhTien();
    }

    //Constructor cho lúc khởi tạo mẫu chi tiết phiếu đặt
    public ChiTietPhieuDatThuocDTO(LoThuocDTO thuoc, int soLuong, DonViTinhDTO dvt){
        this.maPhieu = new PhieuDatThuocDTO();
        this.maThuoc = thuoc;
        this.soLuong = soLuong;
        this.donViTinhDTO = dvt;
        this.thanhTien = soLuong * thuoc.getMaThuocDTO().getGiaBan() * (1 + thuoc.getMaThuocDTO().getThue());
    }

    public ChiTietPhieuDatThuocDTO(PhieuDatThuocDTO maPhieu, LoThuocDTO maThuoc, int soLuong, DonViTinhDTO donViTinhDTO, double thanhTien) {
        this.maPhieu = maPhieu;
        this.maThuoc = maThuoc;
        setSoLuong(soLuong);
        this.donViTinhDTO = donViTinhDTO;
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
        double giaBan = maThuoc.getMaThuocDTO().getGiaBan();
        float thue = maThuoc.getMaThuocDTO().getThue();
        return soLuong * giaBan * (1 + thue);
    }

    @Override
    public String toString() {
        return "ChiTietPhieuDatThuoc{" +
                "maPhieu=" + maPhieu +
                ", maThuoc=" + maThuoc +
                ", soLuong=" + soLuong +
                ", donViTinh=" + donViTinhDTO +
                '}';
    }

}