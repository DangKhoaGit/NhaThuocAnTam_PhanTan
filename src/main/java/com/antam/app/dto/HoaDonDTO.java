/*
 * @ (#) HoaDon.java   1.0 9/25/2025
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

public class HoaDonDTO implements Serializable {
    private final String MaHD;
    private LocalDate ngayTao;
    private double tongTien;
    private boolean deleteAt;

    private NhanVienDTO maNV;

    private KhachHangDTO maKH;

    private KhuyenMaiDTO maKM;


    public HoaDonDTO(String maHD) {
        MaHD = maHD;
        ngayTao = LocalDate.now();
        maNV = new NhanVienDTO();
        maKH = new KhachHangDTO();
        maKM = null;
        tongTien = 0;
    }


    public HoaDonDTO(String maHD, LocalDate now, NhanVienDTO nhanVienDTO, KhachHangDTO kh, KhuyenMaiDTO km, double tongTien, boolean b) {
        MaHD = maHD;
        ngayTao = now;
        maNV = nhanVienDTO;
        maKH = kh;
        maKM = km;
        this.tongTien = tongTien;
    }
}
