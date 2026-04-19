/*
 * @ (#) ChiTietHoaDon.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

@Builder

@IdClass(ChiTietHoaDon.ChiTietHoaDonId.class)
@Entity
@Table(name = "ChiTietHoaDon")
public class ChiTietHoaDon {
    @Id
    @ManyToOne
    @JoinColumn(name = "MaHD")
    private HoaDon MaHD;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaLoThuoc")
    private LoThuoc maLoThuoc;

    @Id
    @Column(name = "TinhTrang")
    private String tinhTrang;

    private int soLuong;

    @ManyToOne
    @JoinColumn(name = "MaDVT")
    private DonViTinh maDVT;

    @Column(name = "ThanhTien")
    private double thanhTien;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ToString
    @EqualsAndHashCode

    @Builder
    public static class ChiTietHoaDonId implements Serializable {
        private String MaHD;
        private int maLoThuoc;
        private String tinhTrang;
    }
}