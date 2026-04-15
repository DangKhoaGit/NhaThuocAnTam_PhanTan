/*
 * @ (#) DangDieuChe.java   1.0 9/24/2025
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
 * @date: 9/24/2025
 * version: 1.0
 */

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder

@Entity
@Table(name = "DangDieuChe")
public class DangDieuChe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int MaDDC;
    private String TenDDC;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean DeleteAt = false;



    public DangDieuChe(String tenDDC) {
        MaDDC = 0;
        setTenDDC(tenDDC);
    }
    public DangDieuChe(int maDDC, String tenDDC) {
        MaDDC = maDDC;
        setTenDDC(tenDDC);
    }

    @OneToMany(mappedBy = "dangDieuChe")
    @JsonIgnore
    private List<Thuoc> thuocList;

    public DangDieuChe(int maDDC, String tenDDC, Boolean deleteAt) {
        MaDDC = maDDC;
        setTenDDC(tenDDC);
        setDeleteAt(deleteAt);
    }
}
