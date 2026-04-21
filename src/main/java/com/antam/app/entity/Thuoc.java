/*
 * @ (#) Thuoc.java   1.0 9/25/2025
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
@Table(name = "Thuoc")
public class Thuoc {

    @Id
    @Column(name = "MaThuoc")
    private String maThuoc;
    @Column(name = "TenThuoc")
    private String TenThuoc;
    @Column(name = "HamLuong")
    private String hamLuong;
    @Column(name = "GiaBan")
    private double giaBan;
    @Column(name = "GiaGoc")
    private double giaGoc;
    @Column(name = "Thue")
    private float thue;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE",name = "DeleteAt")
    private boolean deleteAt;

    @ManyToOne
    @JoinColumn(name = "DangDieuChe")
    private DangDieuChe dangDieuChe;

    @ManyToOne
    @JoinColumn(name = "MaDVTCoso")
    private DonViTinh maDVTCoSo;

    @ManyToOne
    @JoinColumn(name = "MaKe")
    private Ke maKe;

    @OneToMany(mappedBy = "maThuoc")
    @JsonIgnore
    private List<LoThuoc> loThuocList;

    public Thuoc(String maThuoc) {
        this.maThuoc = maThuoc;
        this.TenThuoc = "";
        this.hamLuong = "";
        this.giaBan = 0;
        this.giaGoc = 0;
        this.thue = 0;
        this.deleteAt = false;
        this.dangDieuChe = new DangDieuChe();
        this.maDVTCoSo = new DonViTinh();
        this.maKe = new Ke();
    }

    public Thuoc(String maThuoc, String tenThuoc, String hamLuong, Double giaBan, Double giaGoc, float v, boolean b, DangDieuChe dangDieuChe, DonViTinh donViCoSo, Ke ke) {
        this.maThuoc = maThuoc;
        this.TenThuoc = tenThuoc;
        this.hamLuong = hamLuong;
        this.giaBan = giaBan;
        this.giaGoc = giaGoc;
        this.thue = v;
        this.deleteAt = b;
        this.dangDieuChe = dangDieuChe;
        this.maDVTCoSo = donViCoSo;
        this.maKe = ke;
    }
}
