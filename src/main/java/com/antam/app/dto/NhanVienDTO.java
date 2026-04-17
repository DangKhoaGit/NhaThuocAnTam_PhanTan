/*
 * @ (#) NhanVien.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
@Data
@NoArgsConstructor(force = true)
@Builder

public class NhanVienDTO {
    private final String MaNV;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private double luongCoBan;
    private String taiKhoan;
    private String matKhau;
    private boolean isQuanLy;
    private boolean deleteAt = false;


    public NhanVienDTO(String MaNhanVien) {
        this.MaNV = MaNhanVien;
        hoTen = "";
        soDienThoai = "";
        email = "";
        diaChi = "";
        luongCoBan = 0;
        taiKhoan = "";
        matKhau = "";
        isQuanLy = false;
        deleteAt = false;
    }

    public NhanVienDTO(String MaNhanVien, String HoTen) {
        this.MaNV = MaNhanVien;
        this.hoTen = HoTen;
        soDienThoai = "";
        email = "";
        diaChi = "";
        luongCoBan = 0;
        taiKhoan = "";
        matKhau = "";
        isQuanLy = false;
        deleteAt = false;
    }

    public NhanVienDTO(String maNV, String hoTen, String soDienThoai, String email, String diaChi, double luongCoBan, String taiKhoan, String matKhau, boolean isQuanLy) {
        MaNV = maNV;
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setEmail(email);
        setDiaChi(diaChi);
        setLuongCoBan(luongCoBan);
        setTaiKhoan(taiKhoan);
        setMatKhau(matKhau);
        this.deleteAt = false;
        this.isQuanLy = isQuanLy;
    }

    /**
     * Sử dụng riêng cho đặt thuốc
     * @param hoten
     * @param ql
     */
    public NhanVienDTO(String hoten, boolean ql){
        this.MaNV = "";
        hoTen = hoten;
        soDienThoai = "";
        email = "";
        diaChi = "";
        luongCoBan = 0;
        taiKhoan = "";
        matKhau = "";
        isQuanLy = ql;
        deleteAt = false;
    }


    public NhanVienDTO(String maNV, String hoTen, String soDT, String email, String diaChi, double luongCb, String taiKhoan, String matKhau, boolean deleteAt, boolean isQL) {
        MaNV = maNV;
        setHoTen(hoTen);
        setSoDienThoai(soDT);
        setEmail(email);
        setDiaChi(diaChi);
        setLuongCoBan(luongCb);
        setTaiKhoan(taiKhoan);
        setMatKhau(matKhau);
        this.deleteAt = deleteAt;
        this.isQuanLy = isQL;
    }
}
