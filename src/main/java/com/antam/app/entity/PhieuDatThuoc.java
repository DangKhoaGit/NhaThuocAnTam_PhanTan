/*
 * @ (#) PhieuDatThuoc.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

@Entity
@Table(name = "PhieuDatThuoc")
public class PhieuDatThuoc {
    @Id
    @Column(name = "MaPDT")
    private final String maPhieu;
    @Column(name = "NgayTao")
    private LocalDate ngayTao;
    @Column(name = "IsThanhToan")
    private boolean isThanhToan;
    @Column(name = "TongTien")
    private double tongTien;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean daXoa = false;

    @ManyToOne
    @JoinColumn(name = "MaNV")
    private NhanVien nhanVien;
    @ManyToOne
    @JoinColumn(name = "MaKH")
    private KhachHang khachHang;
    @ManyToOne
    @JoinColumn(name = "MaKM")
    private KhuyenMai khuyenMai;

    @OneToMany(mappedBy = "maPhieu")
    @JsonIgnore
    private List<ChiTietPhieuDatThuoc> chiTietPhieuDatThuocList;

    public PhieuDatThuoc(String maPhieu, LocalDate ngayTao, boolean isThanhToan, NhanVien nhanVien, KhachHang maKH, KhuyenMai maKM, double tongTien) {
        this.maPhieu = maPhieu;
        setNgayTao(ngayTao);
        this.isThanhToan = isThanhToan;
        this.nhanVien = nhanVien;
        this.khachHang = maKH;
        this.khuyenMai = maKM;
        this.tongTien = tongTien;
    }

    public PhieuDatThuoc(String maPhieu, LocalDate ngayTao, boolean isThanhToan, NhanVien nhanVien, KhachHang maKH, KhuyenMai maKM, double tongTien,boolean daXoa) {
        this.maPhieu = maPhieu;
        setNgayTao(ngayTao);
        this.isThanhToan = isThanhToan;
        this.nhanVien = nhanVien;
        this.khachHang = maKH;
        this.khuyenMai = maKM;
        this.tongTien = tongTien;
        this.daXoa = daXoa;
    }


    public void setNgayTao(LocalDate ngayTao) {
        if (ngayTao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày tạo không được sau ngày hiện tại");
        }
        this.ngayTao = ngayTao;
    }

}
