/*
 * @ (#) LoThuoc.java   1.0 10/6/2025
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
 * @date: 10/6/2025
 * version: 1.0
 */
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder

@Entity
@Table(name = "LoThuoc")
public class LoThuoc {
    @Id
    @Column(name = "MaLoThuoc")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int MaLoThuoc;

    @OneToMany(mappedBy = "loThuoc")
    @JsonIgnore
    private List<ChiTietPhieuNhap> maCTPN;

    @JoinColumn(name = "MaThuoc")
    @ManyToOne()
    private Thuoc maThuoc;

    @OneToMany(mappedBy = "maLoThuoc")
    @JsonIgnore
    private List<ChiTietHoaDon> chiTietHoaDons;

    @Column(name = "TonKho")
    private int soLuong;

    @Column(name = "HanSuDung")
    private LocalDate hanSuDung;

    @Column(name = "NgaySanXuat")
    private LocalDate ngaySanXuat;

    @OneToMany(mappedBy = "maThuoc")
    @JsonIgnore
    private List<ChiTietPhieuDatThuoc> chiTietPhieuDatThuocList;

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
    public LoThuoc(int i, PhieuNhap pn, Thuoc value, Integer value1, LocalDate value2, LocalDate value3, int maLoThuoc) {
        this.maThuoc = value;
        MaLoThuoc = maLoThuoc;
        setSoLuong(value1);
        setNgaySanXuat(value3);
        setHanSuDung(value2);
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
    public LoThuoc(int i, PhieuNhap pn, Thuoc value, Integer value1, LocalDate value2, LocalDate value3) {
        this.maThuoc = value;
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
        if (hanSuDung != null && ngaySanXuat != null && hanSuDung.isBefore(ngaySanXuat)) {
            throw new IllegalArgumentException("Hạn sử dụng không được trước ngày sản xuất");
        }
        this.hanSuDung = hanSuDung;
    }
    public LocalDate getNgaySanXuat() {
        return ngaySanXuat;
    }
    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        if (ngaySanXuat != null && LocalDate.now().isBefore(ngaySanXuat)) {
            throw new IllegalArgumentException("Ngày sản xuất không được sau ngày hiện tại");
        }
        this.ngaySanXuat = ngaySanXuat;

        if (this.hanSuDung != null && this.ngaySanXuat != null && this.hanSuDung.isBefore(this.ngaySanXuat)) {
            throw new IllegalArgumentException("Hạn sử dụng không được trước ngày sản xuất");
        }
    }

}
