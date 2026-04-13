/*
 * @ (#) DangDieuChe.java   1.0 9/24/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.entity;

import java.util.Objects;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/24/2025
 * version: 1.0
 */
public class DangDieuChe {
    private final int MaDDC;
    private String TenDDC;
    private boolean deleteAt;

    public DangDieuChe() {
        MaDDC = 0;
        TenDDC = "";
        deleteAt = false;
    }

    public DangDieuChe(String tenDDC) {
        MaDDC = 0;
        setTenDDC(tenDDC);
    }
    public DangDieuChe(int maDDC, String tenDDC) {
        MaDDC = maDDC;
        setTenDDC(tenDDC);
    }


    public DangDieuChe(int maDDC, String tenDDC, boolean deleteAt) {
        MaDDC = maDDC;
        setTenDDC(tenDDC);
        this.deleteAt = deleteAt;
    }

    public int getMaDDC() {
        return MaDDC;
    }

    public String getTenDDC() {
        return TenDDC;
    }

    public void setTenDDC(String tenDDC) {
        if (tenDDC == null || tenDDC.isEmpty()) {
            throw new IllegalArgumentException("Tên dạng điều chế không được để trống");
        }
        TenDDC = tenDDC;
    }

    public boolean isDeleteAt() {
        return deleteAt;
    }
    public void setDeleteAt(boolean deleteAt) {
        this.deleteAt = deleteAt;
    }

    @Override
    public String toString() {
        return TenDDC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DangDieuChe that = (DangDieuChe) o;
        return MaDDC == that.MaDDC;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MaDDC);
    }
}
