/*
 * @ (#) LoThuoc.java   1.0 10/6/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

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
 * @date: 10/6/2025
 * version: 1.0
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder

public class LoThuocDTO {
    @Id
    @Column(name = "MaLoThuoc")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int MaLoThuoc;

    private List<ChiTietPhieuNhapDTO> maCTPN;

    private ThuocDTO maThuocDTO;

    private List<ChiTietHoaDonDTO> chiTietHoaDons;

    private int soLuong;

    private LocalDate hanSuDung;

    private LocalDate ngaySanXuat;

    private List<ChiTietPhieuDatThuocDTO> chiTietPhieuDatThuocList;

    /**
     * Constructor for LoThuoc Chưa chuẩn cần được sửa lại
     * được sử dụng trong ThemPhieuNhapFormController
     * @param i
     * @param pn
     * @param value
     * @param value1
     * @param value2
     * @param value3
     */
    public LoThuocDTO(int i, PhieuNhapDTO pn, ThuocDTO value, Integer value1, LocalDate value2, LocalDate value3, int maLoThuoc) {
        this.maThuocDTO = value;
        MaLoThuoc = maLoThuoc;
        setSoLuong(value1);
        setHanSuDung(value2);
        setNgaySanXuat(value3);
    }

    /**
     * Constructor for LoThuoc Chưa chuẩn cần được sửa lại
     * được sử dụng trong ThemPhieuNhapFormController
     * @param i
     * @param pn
     * @param value
     * @param value1
     * @param value2
     * @param value3
     */
    public LoThuocDTO(int i, PhieuNhapDTO pn, ThuocDTO value, Integer value1, LocalDate value2, LocalDate value3) {
        this.maThuocDTO = value;
        MaLoThuoc = i;
        setSoLuong(value1);
        setNgaySanXuat(value3);
        setHanSuDung(value2);

    }


    public int getSoLuong() {
        return soLuong;
    }
    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm");
        }
        this.soLuong = soLuong;
    }
    public LocalDate getHanSuDung() {
        return hanSuDung;
    }
    public void setHanSuDung(LocalDate hanSuDung) {
        if (hanSuDung.isBefore(ngaySanXuat)) {
            throw new IllegalArgumentException("Hạn sử dụng không được trước ngày sản xuất");
        }
        this.hanSuDung = hanSuDung;
    }
    public LocalDate getNgaySanXuat() {
        return ngaySanXuat;
    }
    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        if (LocalDate.now().isBefore(ngaySanXuat)) {
            throw new IllegalArgumentException("Ngày sản xuất không được sau ngày hiện tại");
        }
        this.ngaySanXuat = ngaySanXuat;
    }

}
