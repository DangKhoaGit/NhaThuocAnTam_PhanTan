package com.antam.app.service;

import com.antam.app.dto.KeDTO;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_Ke_Service {
    /* Duy - Xoá kệ */
    boolean xoaKe(String maKe);

    /* Duy - Khôi phục kệ */
    boolean khoiPhucKe(String maKe);

    /* Duy - Sửa kệ */
    boolean suaKe(KeDTO keDTO);

    /* Duy - Thêm kệ */
    boolean themKe(KeDTO keDTO);

    /* Duy - Tạo mã kệ tự động */
    String taoMaKeTuDong();

    /* Duy - Lấy tất cả kệ */
    ArrayList<KeDTO> getTatCaKeThuoc();

    ArrayList<KeDTO> getTatCaKeHoatDong();

    KeDTO getKeTheoMa(String ma);

    KeDTO getKeTheoName(String name);

    boolean isTenKeTrung(String tenKe, String maKeHienTai);
}
