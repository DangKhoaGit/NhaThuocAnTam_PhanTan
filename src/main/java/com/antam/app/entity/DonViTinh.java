/*
 * @ (#) DonViTinh.java   1.0 9/25/2025
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
@Table(name = "DonViTinh")
public class DonViTinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int MaDVT;
    private String TenDVT;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDelete = false;

    public DonViTinh(int maDVT) {
        MaDVT = maDVT;
        TenDVT = "";
    }

    @OneToMany(mappedBy = "maDVTCoSo")
    @JsonIgnore
    private List<Thuoc> thuocList;

    @OneToMany(mappedBy = "maDVT")
    @JsonIgnore
    private List<ChiTietPhieuNhap> chiTietPhieuNhapList;

    @OneToMany(mappedBy = "maDVT")
    @JsonIgnore
    private List<ChiTietHoaDon> chiTietHoaDonList;

    @OneToMany(mappedBy = "donViTinh")
    @JsonIgnore
    private List<ChiTietPhieuDatThuoc> chiTietPhieuDatThuocList;

    public DonViTinh(int maInt, String ten, boolean b) {
        	this.MaDVT = maInt;
        	this.TenDVT = ten;
        	this.isDelete = b;
    }
}
