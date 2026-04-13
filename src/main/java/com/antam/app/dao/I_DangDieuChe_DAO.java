package com.antam.app.dao;

import com.antam.app.entity.DangDieuChe;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_DangDieuChe_DAO {
    /* Duy - Khôi phục dạng điều chế */
    boolean khoiPhucDangDieuChe(int maDDC);

    /* Duy - Cap nhat delete at dang dieu che */
    boolean xoaDangDieuChe(int maDDC);

    /* Duy - Sửa dang điều chế */
    boolean suaDangDieuChe(DangDieuChe ddc);

    /* Duy - Thêm dạng điều chế */
    boolean themDDC(DangDieuChe ddc);

    /* Duy - Lấy mã dạng điều chế tự động */
    String taoMaDDCTuDong();

    /* Duy - Lấy tất cả dạng điều chế */
    ArrayList<DangDieuChe> getTatCaDangDieuChe();

    ArrayList<DangDieuChe> getDangDieuCheHoatDong();

    DangDieuChe getDDCTheoName(String name);
}
