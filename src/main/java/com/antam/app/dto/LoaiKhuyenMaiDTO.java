/*
 * @ (#) LoaiKhuyenMai.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Builder

public class LoaiKhuyenMaiDTO implements Serializable {
    private final int MaLKM;
    private String TenLKM;

    private KhuyenMaiDTO khuyenMaiDTO;

    public LoaiKhuyenMaiDTO(int maLoaiKM, String tenLoaiKM) {
        MaLKM = maLoaiKM;
        TenLKM = tenLoaiKM;
    }

    public String getDisplayText() {
        if (MaLKM <= 0) {
            return TenLKM == null ? "" : TenLKM;
        }
        return MaLKM + " - " + (TenLKM == null ? "" : TenLKM);
    }

    @Override
    public String toString() {
        return getDisplayText();
    }
}
