/*
 * @ (#) PhieuNhap.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder

public class PhieuNhapDTO implements Serializable {
    private final String MaPhieuNhap;
    private String nhaCungCap;
    private LocalDate ngayNhap;
    private String diaChi;
    private String lyDo;
    private double tongTien;

    private NhanVienDTO maNV;
    private boolean deleteAt;


    public PhieuNhapDTO(String maPN) {
        MaPhieuNhap = maPN;
        nhaCungCap = "";
        ngayNhap = LocalDate.now();
        diaChi = "";
        lyDo = "";
        maNV = new NhanVienDTO();
    }

    public PhieuNhapDTO(String maPhieuNhap, String nhaCungCap, LocalDate ngayNhap, String diaChi, String lyDo, NhanVienDTO maNV, double tongTien, boolean deleteAt) {
        MaPhieuNhap = maPhieuNhap;
        this.nhaCungCap = nhaCungCap;
        this.ngayNhap = ngayNhap;
        this.diaChi = diaChi;
        this.lyDo = lyDo;
        this.maNV = maNV;
        this.tongTien = tongTien;
    }
}
