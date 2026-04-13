package com.antam.app.dao;

import com.antam.app.entity.Ke;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_Ke_DAO {
    /* Duy - Xoá kệ */
    boolean xoaKe(String maKe);

    /* Duy - Khôi phục kệ */
    boolean khoiPhucKe(String maKe);

    /* Duy - Sửa kệ */
    boolean suaKe(Ke ke);

    /* Duy - Thêm kệ */
    boolean themKe(Ke ke);

    /* Duy - Tạo mã kệ tự động */
    String taoMaKeTuDong();

    /* Duy - Lấy tất cả kệ */
    ArrayList<Ke> getTatCaKeThuoc();

    ArrayList<Ke> getTatCaKeHoatDong();

    Ke getKeTheoMa(String ma);

    Ke getKeTheoName(String name);

    boolean isTenKeTrung(String tenKe, String maKeHienTai);
}
