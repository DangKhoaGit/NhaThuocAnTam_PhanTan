/*
 * @ (#) HoaDon.java   1.0 9/25/2025
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
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    private final String MaHD;
    @Column(name = "NgayTao")
    private LocalDate ngayTao;
    @Column(name = "TongTien")
    private double tongTien;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleteAt = false;

    @ManyToOne()
    @JoinColumn(name = "MaNV")
    private NhanVien maNV;

    @ManyToOne()
    @JoinColumn(name = "MaKH")
    private KhachHang maKH;

    @ManyToOne()
    @JoinColumn(name = "MaKM")
    private KhuyenMai maKM;

    @OneToMany(mappedBy = "MaHD")
    @JsonIgnore
    private List<ChiTietHoaDon> chiTietHoaDons;

    public HoaDon(String maHD) {
        MaHD = maHD;
        ngayTao = LocalDate.now();
        maNV = new NhanVien();
        maKH = new KhachHang();
        maKM = null;
        tongTien = 0;
        deleteAt = false;
    }


    public HoaDon(String maHD, LocalDate now, NhanVien nhanVien, KhachHang kh, KhuyenMai km, double tongTien, boolean b) {
        MaHD = maHD;
        ngayTao = now;
        maNV = nhanVien;
        maKH = kh;
        maKM = km;
        this.tongTien = tongTien;
        deleteAt = b;
    }
}
