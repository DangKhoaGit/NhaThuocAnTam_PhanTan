/*
 * @ (#) Ke.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */

@Data
@NoArgsConstructor(force = true)
@Builder

public class KeDTO implements Serializable {
    private final String MaKe;
    private String tenKe;
    private String loaiKe;
    private boolean deleteAt;


    public KeDTO(String maKe) {
        MaKe = maKe;
        this.tenKe = "";
        this.loaiKe = "";
    }

    public KeDTO(String maKe, String tenKe, String loaiKe) {
        MaKe = maKe;
        this.tenKe = tenKe;
        this.loaiKe = loaiKe;
    }

    public KeDTO(String maKe, String tenKe, String loaiKe, boolean deleteAt) {
        MaKe = maKe;
        this.tenKe = tenKe;
        this.loaiKe = loaiKe;
        this.deleteAt = deleteAt;
    }

    @Override
    public String toString() {
        if (MaKe == null || MaKe.isBlank()) {
            return tenKe == null ? "" : tenKe;
        }
        return MaKe + " - " + (tenKe == null ? "" : tenKe);
    }
}
