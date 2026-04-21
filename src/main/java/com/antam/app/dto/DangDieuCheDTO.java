/*
 * @ (#) DangDieuChe.java   1.0 9/24/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

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

public class DangDieuCheDTO {
    private final int MaDDC;
    private String TenDDC;
    private boolean deleteAt;


    public DangDieuCheDTO(String tenDDC) {
        MaDDC = 0;
        setTenDDC(tenDDC);
    }
    public DangDieuCheDTO(int maDDC, String tenDDC) {
        MaDDC = maDDC;
        setTenDDC(tenDDC);
    }

    private List<ThuocDTO> thuocList;

    public DangDieuCheDTO(int maDDC, String tenDDC, Boolean deleteAt) {
        MaDDC = maDDC;
        setTenDDC(tenDDC);
        this.deleteAt = deleteAt;
    }

    public String getDisplayText() {
        if (MaDDC <= 0 || "Tất cả".equalsIgnoreCase(TenDDC)) {
            return "Tất cả";
        }
        return MaDDC + " - " + TenDDC;
    }
}
