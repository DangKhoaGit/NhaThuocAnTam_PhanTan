package com.antam.app.dao;

import com.antam.app.entity.PhieuNhap;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_PhieuNhap_DAO {
    /* Duy- Huỷ phiếu nhập */
    boolean huyPhieuNhap(String maPhieuNhap);

    /* Duy- Cập nhật phiếu nhập */
    boolean suaPhieuNhap(PhieuNhap pn);

    /* Duy- Cập nhật trạng thái phiếu nhập */
    boolean suaTrangThaiPhieuNhap(String maPhieuNhap);

    /* Duy - Tạo mã phiêu nhập tự động */
    String taoMaPhieuNhapTuDong();

    /* Duy - Kiểm tra phiếu nhập tồn tại */
    boolean tonTaiMaPhieuNhap(String maPhieuNhap);

    /* Duy- Thêm phiếu nhập */
    boolean themPhieuNhap(PhieuNhap pn);

    /* Duy - Lấy danh sách phiếu nhập */
    ArrayList<PhieuNhap> getDanhSachPhieuNhap();

    /* Duy - Lấy danh sách phiếu nhập theo trạng thái */
    ArrayList<PhieuNhap> getDanhSachPhieuNhapTheoTrangThai(Boolean isDeleted);
}
