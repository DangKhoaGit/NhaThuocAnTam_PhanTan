/*
 * @ (#) NhanVien.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
@AllArgsConstructor
@Builder

@Entity
@Table(name = "NhanVien")
public class NhanVien {
    @Id
    private final String MaNV;
    @Column(name = "HoTen")
    private String hoTen;
    @Column(name = "SoDienThoai")
    private String soDienThoai;
    @Column(name = "Email")
    private String email;
    @Column(name = "DiaChi")
    private String diaChi;
    @Column(name = "LuongCoBan")
    private double luongCoBan;
    @Column(name = "TaiKhoan")
    private String taiKhoan;
    @Column(name = "MatKhau")
    private String matKhau;
    @Column(name = "IsQuanLi")
    private boolean isQuanLy;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleteAt = false;


    @OneToMany(mappedBy = "maNV")
    @JsonIgnore
    private List<PhieuNhap> phieuNhapList;


    @OneToMany(mappedBy = "maNV")
    @JsonIgnore
    private List<HoaDon> hoaDonList;

    @OneToMany(mappedBy = "nhanVien")
    @JsonIgnore
    private List<PhieuDatThuoc> phieuDatThuocList;

    public NhanVien(String MaNhanVien) {
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

    public NhanVien(String MaNhanVien, String HoTen) {
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

    public NhanVien(String maNV, String hoTen, String soDienThoai, String email, String diaChi, double luongCoBan, String taiKhoan, String matKhau, boolean isQuanLy) {
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
    public NhanVien(String hoten, boolean ql){
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


    public NhanVien(String maNV, String hoTen, String soDT, String email, String diaChi, double luongCb, String taiKhoan, String matKhau, boolean deleteAt, boolean isQL) {
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
