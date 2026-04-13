/*
 * @ (#) ChiTietHoaDon.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import jakarta.persistence.*;
import lombok.*;

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

//@Entity
@Table(name = "ChiTietHoaDon")
public class ChiTietHoaDon {

    private HoaDon MaHD;
    private LoThuoc maCTT;
    private int soLuong;
    private DonViTinh maDVT;
    private String tinhTrang;
    private double thanhTien;

}