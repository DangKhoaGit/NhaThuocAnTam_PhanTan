/*
 * @ (#) PhienNguoiDung.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dto;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class PhienNguoiDungDTO {
    private static NhanVienDTO maNV;

    // Phương thức để lưu tên đăng nhập
    public static void setMaNV(NhanVienDTO maNhanVienDTO) {
        maNV = maNhanVienDTO;
    }

    // Phương thức để lấy tên đăng nhập
    public static NhanVienDTO getMaNV() {
        return maNV;
    }
}
