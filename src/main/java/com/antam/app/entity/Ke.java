/*
 * @ (#) Ke.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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
    @Column(name = "DeleteAt")
    private boolean deleteAt;

    public Ke(String maKe) {
        MaKe = maKe;
        tenKe = "";
        loaiKe = "";
        deleteAt = false;
    }
}
