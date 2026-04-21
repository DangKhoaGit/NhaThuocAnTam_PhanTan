/*
 * @ (#) DonViTinh.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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

public class DonViTinhDTO implements Serializable {
    private final int MaDVT;
    private String TenDVT;
    private boolean isDelete = false;

    public DonViTinhDTO(int maDVT) {
        MaDVT = maDVT;
        TenDVT = "";
    }

    private List<ThuocDTO> thuocList;

    private List<ChiTietPhieuNhapDTO> chiTietPhieuNhapList;

    private List<ChiTietHoaDonDTO> chiTietHoaDonList;

    private List<ChiTietPhieuDatThuocDTO> chiTietPhieuDatThuocList;

    public DonViTinhDTO(int maInt, String ten, boolean b) {
        	this.MaDVT = maInt;
        	this.TenDVT = ten;
        	this.isDelete = b;
    }
}
