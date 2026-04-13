package com.antam.app.dao;

import com.antam.app.entity.ChiTietPhieuNhap;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_ChiTietPhieuNhap_DAO {
    /* Duy - Lấy danh sách chi tiết phiếu nhập theo mã phiếu nhập */
    ArrayList<ChiTietPhieuNhap> getDanhSachChiTietPhieuNhapTheoMaPN(String maPN);

    /* Duy- Thêm chi tiết phiếu nhập */
    boolean themChiTietPhieuNhap(ChiTietPhieuNhap ctpn);
}
