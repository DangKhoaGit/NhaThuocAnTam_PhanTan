/*
 * @ (#) KhachHang.java   1.0 9/25/2025
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
@Table(name = "KhachHang")
public class KhachHang {
    // Các field có trong database
    @Id
    private final String MaKH;
    private String tenKH;
    private String soDienThoai;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleteAt = false;

    // Các field tính toán cho thống kê (không lưu trong DB)

    @Column(columnDefinition = "INT DEFAULT 0")
    private double tongChiTieu =0;
    @Column(columnDefinition = "INT DEFAULT 0")
    private int soDonHang = 0;

    private LocalDate ngayMuaGanNhat;

    @OneToMany(mappedBy = "maKH")
    @JsonIgnore
    private List<HoaDon> hoaDonList;

    @OneToMany(mappedBy = "khachHang")
    @JsonIgnore
    private List<PhieuDatThuoc> phieuDatList;

    public KhachHang(String maKH) {
        MaKH = maKH;
        tenKH = "";
        soDienThoai = "";
        deleteAt = false;
        tongChiTieu = 0;
        soDonHang = 0;
        ngayMuaGanNhat = null;
    }

    public KhachHang(String newMaKH, String tenKH, String soDienThoai, boolean b) {
        MaKH = newMaKH;
        setTenKH(tenKH);
        setSoDienThoai(soDienThoai);
        deleteAt = b;
        tongChiTieu = 0;
        soDonHang = 0;
        ngayMuaGanNhat = null;
    }

    public void setTenKH(String tenKH) {
        if (tenKH == null || tenKH.isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống");
        }
        this.tenKH = tenKH;
    }


    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        } else if (!soDienThoai.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        this.soDienThoai = soDienThoai;
    }

    // Phương thức tiện ích để lấy loại khách hàng
    public String getLoaiKhachHang() {
        // VIP nếu tổng chi tiêu > 5.000.000 hoặc số đơn hàng > 20
        if (tongChiTieu > 5_000_000 || soDonHang > 20) {
            return "VIP";
        } else {
            return "Thường";
        }
    }

}
