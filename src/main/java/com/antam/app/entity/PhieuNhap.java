/*
 * @ (#) PhieuNhap.java   1.0 9/25/2025
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
@Table(name = "PhieuNhap")
public class PhieuNhap {
    @Id
    private final String MaPhieuNhap;
    @Column(name = "NhaCungCap")
    private String nhaCungCap;
    @Column(name = "NgayNhap")
    private LocalDate ngayNhap;
    @Column(name = "DiaChi")
    private String diaChi;
    @Column(name = "LyDo")
    private String lyDo;
    @Column(name = "TongTien")
    private double tongTien;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleteAt = false;

    @ManyToOne
    @JoinColumn(name = "MaNV")
    private NhanVien maNV;

    @OneToMany(mappedBy = "MaPN")
    @JsonIgnore
    private List<ChiTietPhieuNhap> chiTietPhieuNhapList;

    public PhieuNhap(String maPN) {
        MaPhieuNhap = maPN;
        nhaCungCap = "";
        ngayNhap = LocalDate.now();
        diaChi = "";
        lyDo = "";
        maNV = new NhanVien();
        deleteAt = false;
    }

    public PhieuNhap(String maPhieuNhap, String nhaCungCap, LocalDate ngayNhap, String diaChi, String lyDo, NhanVien maNV, double tongTien, boolean deleteAt) {
        MaPhieuNhap = maPhieuNhap;
        this.nhaCungCap = nhaCungCap;
        this.ngayNhap = ngayNhap;
        this.diaChi = diaChi;
        this.lyDo = lyDo;
        this.maNV = maNV;
        this.tongTien = tongTien;
        this.deleteAt = deleteAt;
    }
}
