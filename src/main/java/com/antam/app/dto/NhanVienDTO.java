/*
 * @ (#) NhanVien.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
@Data
@NoArgsConstructor(force = true)
@Builder

public class NhanVienDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String MaNV;
    private String hoTen;
    private String soDienThoai;
    private String email;
    private String diaChi;
    private double luongCoBan;
    private String taiKhoan;
    private String matKhau;
    @JsonProperty("quanLi") // Đảm bảo luôn map đúng tên
    private boolean quanLi;

    @JsonProperty("deleteAt") // Đảm bảo luôn map đúng tên
    private boolean deleteAt;


    public NhanVienDTO(String MaNhanVien) {
        this.MaNV = MaNhanVien;
        hoTen = "";
        soDienThoai = "";
        email = "";
        diaChi = "";
        luongCoBan = 0;
        taiKhoan = "";
        matKhau = "";
        quanLi = false;
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
        quanLi = false;
        deleteAt = false;
    }

    public NhanVienDTO(String maNV, String hoTen, String soDienThoai, String email, String diaChi, double luongCoBan, String taiKhoan, String matKhau, boolean quanLi) {
        MaNV = maNV;
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setEmail(email);
        setDiaChi(diaChi);
        setLuongCoBan(luongCoBan);
        setTaiKhoan(taiKhoan);
        setMatKhau(matKhau);
        this.deleteAt = false;
        this.quanLi = quanLi;
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
        quanLi = ql;
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
        this.quanLi = isQL;
    }

    public NhanVienDTO(NhanVienDTO maNV) {
        this.MaNV = maNV.getMaNV();
        setHoTen(maNV.getHoTen());
        setSoDienThoai(maNV.getSoDienThoai());
        setEmail(maNV.getEmail());
        setDiaChi(maNV.getDiaChi());
        setLuongCoBan(maNV.getLuongCoBan());
        setTaiKhoan(maNV.getTaiKhoan());
        setMatKhau(maNV.getMatKhau());
        setDeleteAt(maNV.isDeleteAt());
        setQuanLi(maNV.isQuanLi());
    }
}
