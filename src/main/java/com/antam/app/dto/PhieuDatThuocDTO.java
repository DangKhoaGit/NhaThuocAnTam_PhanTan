/*
 * @ (#) PhieuDatThuoc.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@ToString

@Builder

public class PhieuDatThuocDTO implements Serializable {
    private final String maPhieu;
    private LocalDate ngayTao;
    private boolean isThanhToan;
    private double tongTien;

    private NhanVienDTO nhanVienDTO;
    private KhachHangDTO khachHang;
    private KhuyenMaiDTO khuyenMaiDTO;


    public PhieuDatThuocDTO(String maPhieu, LocalDate ngayTao, boolean isThanhToan, NhanVienDTO nhanVienDTO, KhachHangDTO maKH, KhuyenMaiDTO maKM, double tongTien) {
        this.maPhieu = maPhieu;
        setNgayTao(ngayTao);
        this.isThanhToan = isThanhToan;
        this.nhanVienDTO = nhanVienDTO;
        this.khachHang = maKH;
        this.khuyenMaiDTO = maKM;
        this.tongTien = tongTien;
    }

    public PhieuDatThuocDTO(String maPhieu, LocalDate ngayTao, boolean isThanhToan, NhanVienDTO nhanVienDTO, KhachHangDTO maKH, KhuyenMaiDTO maKM, double tongTien, boolean daXoa) {
        this.maPhieu = maPhieu;
        setNgayTao(ngayTao);
        this.isThanhToan = isThanhToan;
        this.nhanVienDTO = nhanVienDTO;
        this.khachHang = maKH;
        this.khuyenMaiDTO = maKM;
        this.tongTien = tongTien;
    }


    public void setNgayTao(LocalDate ngayTao) {
        if (ngayTao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày tạo không được sau ngày hiện tại");
        }
        this.ngayTao = ngayTao;
    }

}
