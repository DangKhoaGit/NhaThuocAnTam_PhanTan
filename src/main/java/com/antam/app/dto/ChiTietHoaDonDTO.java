/*
 * @ (#) ChiTietHoaDon.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

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

public class ChiTietHoaDonDTO {

    private HoaDonDTO MaHD;

    private LoThuocDTO maLoThuocDTO;
    private int soLuong;
    private DonViTinhDTO maDVT;
    private String tinhTrang;
    private double thanhTien;

}