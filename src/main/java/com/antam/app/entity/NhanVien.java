/*
 * @ (#) NhanVien.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
@ToString
@Entity
@Table(name = "NhanVien")
public class NhanVien {
    @Id
    @ToString.Include
    private String MaNV;
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
    private boolean quanLi;

    // Bắt buộc phải có tên cột cụ thể để tránh mapping nhầm theo thứ tự
    @Column(name = "DeleteAt", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleteAt;


    @OneToMany(mappedBy = "maNV")
    @JsonIgnore
    @ToString.Exclude
    private List<PhieuNhap> phieuNhapList;


    @OneToMany(mappedBy = "maNV")
    @JsonIgnore
    @ToString.Exclude
    private List<HoaDon> hoaDonList;

    @OneToMany(mappedBy = "nhanVien")
    @JsonIgnore
    @ToString.Exclude
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
        quanLi = false;
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
        quanLi = false;
        deleteAt = false;
    }

    public NhanVien(String maNV, String hoTen, String soDienThoai, String email, String diaChi, double luongCoBan, String taiKhoan, String matKhau, boolean isQuanLi) {
        MaNV = maNV;
        setHoTen(hoTen);
        setSoDienThoai(soDienThoai);
        setEmail(email);
        setDiaChi(diaChi);
        setLuongCoBan(luongCoBan);
        setTaiKhoan(taiKhoan);
        setMatKhau(matKhau);
        this.deleteAt = false;
        this.quanLi = isQuanLi;
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
        quanLi = ql;
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
        this.quanLi = isQL;
    }
}
