/*
 * @ (#) Thuoc.java   1.0 9/25/2025
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

public class ThuocDTO implements Serializable {

    private final String maThuoc;
    private String TenThuoc;
    private String hamLuong;
    private double giaBan;
    private double giaGoc;
    private float thue;
    private boolean deleteAt;

    private DangDieuCheDTO dangDieuCheDTO;

    private DonViTinhDTO maDVTCoSo;

    private KeDTO maKeDTO;


    public ThuocDTO(String maThuoc) {
        this.maThuoc = maThuoc;
        this.TenThuoc = "";
        this.hamLuong = "";
        this.giaBan = 0;
        this.giaGoc = 0;
        this.thue = 0;
        this.dangDieuCheDTO = new DangDieuCheDTO();
        this.maDVTCoSo = new DonViTinhDTO();
        this.maKeDTO = new KeDTO();
    }

    public ThuocDTO(String maThuoc, String tenThuoc, String hamLuong, Double giaBan, Double giaGoc, float v, boolean b, DangDieuCheDTO dangDieuCheDTO, DonViTinhDTO donViCoSo, KeDTO keDTO) {
        this.maThuoc = maThuoc;
        this.TenThuoc = tenThuoc;
        this.hamLuong = hamLuong;
        this.giaBan = giaBan;
        this.giaGoc = giaGoc;
        this.thue = v;
        this.dangDieuCheDTO = dangDieuCheDTO;
        this.maDVTCoSo = donViCoSo;
        this.maKeDTO = keDTO;
    }
}
