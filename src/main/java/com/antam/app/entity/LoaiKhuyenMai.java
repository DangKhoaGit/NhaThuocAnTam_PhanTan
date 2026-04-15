/*
 * @ (#) LoaiKhuyenMai.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "LoaiKhuyenMai")
public class LoaiKhuyenMai {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private final int MaLKM;
    @Column(name = "TenLKM")
    private String TenLKM;

    @OneToOne(mappedBy = "loaiKhuyenMai")
    private KhuyenMai khuyenMai;

    public LoaiKhuyenMai(int maLoaiKM, String tenLoaiKM) {
        MaLKM = maLoaiKM;
        TenLKM = tenLoaiKM;
    }
}
