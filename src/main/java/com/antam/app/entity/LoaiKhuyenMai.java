/*
 * @ (#) LoaiKhuyenMai.java   1.0 9/25/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.util.Objects;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/25/2025
 * version: 1.0
 */
public class LoaiKhuyenMai {
    private final int MaLKM;
    private String TenLKM;

    public LoaiKhuyenMai() {
        MaLKM = 0;
        TenLKM = "";
    }
    public LoaiKhuyenMai(int maLKM, String tenLKM) {
        MaLKM = maLKM;
        TenLKM = tenLKM;
    }
    public int getMaLKM() {
        return MaLKM;
    }
    public String getTenLKM() {
        return TenLKM;
    }
    public void setTenLKM(String tenLKM) {
        TenLKM = tenLKM;
    }

    @Override
    public String toString() {
        return TenLKM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoaiKhuyenMai that = (LoaiKhuyenMai) o;
        return MaLKM == that.MaLKM;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MaLKM);
    }
}
