/*
 * @ (#) Ke.java   1.0 9/25/2025
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
@Table(name = "KeThuoc")
public class Ke {
    @Id
    @Column(name = "MaKe")
    private final String MaKe;
    @Column(name = "TenKe")
    private String tenKe;
    @Column(name = "LoaiKe")
    private String loaiKe;
    @Column(name = "DeleteAt", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleteAt = false;

    @OneToMany(mappedBy = "maKe")
    @JsonIgnore
    private List<Thuoc> thuocList;

    public Ke(String maKe) {
        MaKe = maKe;
        tenKe = "";
        loaiKe = "";
        deleteAt = false;
    }

    public Ke(String maKe,String tenKe, String loaiKe) {
        MaKe = maKe;
        tenKe = tenKe;
        loaiKe = loaiKe;
        deleteAt = false;
    }

    public Ke(String maKe, String tenKe, String loaiKe, boolean deleteAt) {
        MaKe = maKe;
        this.tenKe = tenKe;
        this.loaiKe = loaiKe;
        this.deleteAt = deleteAt;
    }
}
