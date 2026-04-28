package com.antam.app.service;

import com.antam.app.dto.ChiTietPhieuDatThuocDTO;
import jakarta.persistence.EntityManager;

import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_ChiTietPhieuDat_Service {
    boolean themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuocDTO ct);

    /**
     * Lấy danh sách chi tiết phiếu đặt thuốc theo mã phiếu đặt thuốc
     *
     * @param maPDT - mã phiếu đặt thuốc
     * @return danh sách chi tiết phiếu đặt thuốc
     */
    List<ChiTietPhieuDatThuocDTO> getChiTietTheoPhieu(String maPDT);

    /**
     * Thanh toán chi tiết phiếu đặt thuốc với mã phiếu đặt thuốc
     *
     * @param maPDT - mã phiếu đặt thuốc
     * @return true nếu thành công, false nếu thất bại
     */
    boolean thanhToanChiTietVoiMa(String maPDT);

    boolean huyChiTietPhieu(String maPDT);

    boolean khoiPhucChiTietPhieu(String maPDT);

    @FunctionalInterface
    public interface TransactionFunction<T> {
        T apply(EntityManager em);
    }
}

