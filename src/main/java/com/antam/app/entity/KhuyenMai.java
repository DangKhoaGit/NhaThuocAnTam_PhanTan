/*
 * @ (#) KhuyenMai.java   1.0 9/25/2025
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

@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {
    @Id
    private final String MaKM;
    private String TenKM;
    @Column(name = "NgayBatDau")
    private LocalDate NgayBatDau;
    @Column(name = "NgayKetThuc")
    private LocalDate NgayKetThuc;

    @Column(name = "So")
    private double so;
    @Column(name = "SoLuongToiDa")
    private int soLuongToiDa;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleteAt = false;

    @OneToOne
    @JoinColumn(name = "LoaiKhuyenMai")
    private LoaiKhuyenMai loaiKhuyenMai;

    @OneToMany(mappedBy = "maKM")
    @JsonIgnore
    private List<HoaDon> hoaDons;

    @OneToMany(mappedBy = "khuyenMai")
    @JsonIgnore
    private List<PhieuDatThuoc> phieuDatThuocs;

    public KhuyenMai(String s, String ten) {
        MaKM = "";
        TenKM = ten;
        NgayBatDau = LocalDate.now();
        NgayKetThuc = LocalDate.now();
        loaiKhuyenMai = new LoaiKhuyenMai();
        so = 0;
        soLuongToiDa = 0;
        deleteAt = false;
    }
    public KhuyenMai(String maKM) {
        MaKM = maKM;
        TenKM = "";
        NgayBatDau = LocalDate.now();
        NgayKetThuc = LocalDate.now();
        loaiKhuyenMai = new LoaiKhuyenMai();
        so = 0;
        soLuongToiDa = 0;
        deleteAt = false;
    }

    public KhuyenMai(String maKM, String tenKM, LocalDate ngayBatDau, LocalDate ngayKetThuc, LoaiKhuyenMai loai, double so, int soLuongToiDa, boolean deleteAt) {
        MaKM = maKM;
        TenKM = tenKM;
        setNgayBatDau(ngayBatDau);
        setNgayKetThuc(ngayKetThuc);
        this.loaiKhuyenMai = loai;
        setSo(so);
        setSoLuongToiDa(soLuongToiDa);
        this.deleteAt = deleteAt;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        if (ngayBatDau == null)
            throw new IllegalArgumentException("Ngày bắt đầu không được để trống");
        if (NgayKetThuc != null && ngayBatDau.isAfter(NgayKetThuc)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        NgayBatDau = ngayBatDau;
    }
    public LocalDate getNgayKetThuc() {
        return NgayKetThuc;
    }
    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        if (ngayKetThuc.isBefore(NgayBatDau)) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        NgayKetThuc = ngayKetThuc;
    }

    public double getSo() {
        return so;
    }
    public void setSo(double so) {
        if (so < 0) {
            throw new IllegalArgumentException("Giá trị khuyến mãi không được âm");
        }
        this.so = so;
    }

    public void setSoLuongToiDa(int soLuongToiDa) {
        if (soLuongToiDa < 0) {
            throw new IllegalArgumentException("Số lượng tối đa không được âm");
        }
        this.soLuongToiDa = soLuongToiDa;
    }

}
