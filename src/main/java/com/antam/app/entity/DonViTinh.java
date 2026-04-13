/*
 * @ (#) DonViTinh.java   1.0 9/25/2025
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
public class DonViTinh {
    private final int MaDVT;
    private String TenDVT;
    private boolean isDelete;

    public DonViTinh() {
        MaDVT = 0;
        TenDVT = "";
    }
    public DonViTinh(int maDVT) {
        MaDVT = maDVT;
        TenDVT = "";
    }
    public DonViTinh(int maDVT, String tenDVT,boolean isDelete) {
        MaDVT = maDVT;
        setTenDVT(tenDVT);
        this.isDelete = isDelete;
    }


    public int getMaDVT() {
        return MaDVT;
    }
    public String getTenDVT() {
        return TenDVT;
    }
    public void setTenDVT(String tenDVT) {
        if (tenDVT == null || tenDVT.isEmpty()) {
            throw new IllegalArgumentException("Tên đơn vị tính không được để trống");
        }
        TenDVT = tenDVT;
    }

    @Override
    public String toString() {
        return TenDVT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DonViTinh donViTinh = (DonViTinh) o;
        return MaDVT == donViTinh.MaDVT;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(MaDVT);
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
