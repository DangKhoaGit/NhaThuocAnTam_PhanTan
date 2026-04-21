package com.antam.app.dao;

import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.LoaiKhuyenMai;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_KhuyenMai_DAO {
    // Backward-compatible entry point; real query lives in DAO implementation.
    static List<KhuyenMai> getAllKhuyenMaiConHieuLuc() {
        return new com.antam.app.dao.impl.KhuyenMai_DAO().fetchAllKhuyenMaiConHieuLuc();
    }

    List<KhuyenMai> fetchAllKhuyenMaiConHieuLuc();

    ArrayList<KhuyenMai> getAllKhuyenMaiChuaXoa();

    ArrayList<KhuyenMai> getAllKhuyenMaiDaXoa();

    boolean khoiPhucKhuyenMai(String maKM);

    ArrayList<KhuyenMai> getAllKhuyenMai();

    KhuyenMai getKhuyenMaiTheoMa(String maKM);

    boolean themKhuyenMai(String maKM, String tenKM, LoaiKhuyenMai loaiKM, double so, LocalDate ngayBatDau, LocalDate ngayKetThuc, int soLuongToiDa);

    boolean capNhatKhuyenMai(KhuyenMai km);

    boolean xoaKhuyenMai(String maKM);
}
