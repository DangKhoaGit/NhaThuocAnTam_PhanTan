package com.antam.app.service;

import com.antam.app.dto.ChiTietPhieuNhapDTO;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_ChiTietPhieuNhap_Service {
    /* Duy - Lấy danh sách chi tiết phiếu nhập theo mã phiếu nhập */
    ArrayList<ChiTietPhieuNhapDTO> getDanhSachChiTietPhieuNhapTheoMaPN(String maPN);

    /* Duy- Thêm chi tiết phiếu nhập */
    boolean themChiTietPhieuNhap(ChiTietPhieuNhapDTO ctpn);
}
