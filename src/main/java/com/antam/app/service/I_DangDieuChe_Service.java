package com.antam.app.service;

import com.antam.app.dto.DangDieuCheDTO;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_DangDieuChe_Service {
    /* Duy - Khôi phục dạng điều chế */
    boolean khoiPhucDangDieuChe(int maDDC);

    /* Duy - Cap nhat delete at dang dieu che */
    boolean xoaDangDieuChe(int maDDC);

    /* Duy - Sửa dang điều chế */
    boolean suaDangDieuChe(DangDieuCheDTO ddc);

    /* Duy - Thêm dạng điều chế */
    boolean themDDC(DangDieuCheDTO ddc);

    /* Duy - Lấy mã dạng điều chế tự động */
    String taoMaDDCTuDong();

    /* Duy - Lấy tất cả dạng điều chế */
    ArrayList<DangDieuCheDTO> getTatCaDangDieuChe();

    ArrayList<DangDieuCheDTO> getDangDieuCheHoatDong();

    DangDieuCheDTO getDDCTheoName(String name);
}
