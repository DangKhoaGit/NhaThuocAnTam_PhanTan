package com.antam.app.dao;

import com.antam.app.entity.ChiTietPhieuDatThuoc;

import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_ChiTietPhieuDat_DAO {
    boolean themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuoc ct);

    /**
     * Lấy danh sách chi tiết phiếu đặt thuốc theo mã phiếu đặt thuốc
     *
     * @param maPDT - mã phiếu đặt thuốc
     * @return danh sách chi tiết phiếu đặt thuốc
     */
    List<ChiTietPhieuDatThuoc> getChiTietTheoPhieu(String maPDT);

    /**
     * Thanh toán chi tiết phiếu đặt thuốc với mã phiếu đặt thuốc
     *
     * @param maPDT - mã phiếu đặt thuốc
     * @return true nếu thành công, false nếu thất bại
     */
    boolean thanhToanChiTietVoiMa(String maPDT);

    boolean huyChiTietPhieu(String maPDT);

    boolean khoiPhucChiTietPhieu(String maPDT);
}
