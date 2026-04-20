package com.antam.app.service;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.impl.LoThuoc_Service;
import com.antam.app.service.impl.DonViTinh_Service;
import com.antam.app.dto.ChiTietPhieuDatThuocDTO;
import com.antam.app.dto.LoThuocDTO;
import com.antam.app.dto.DonViTinhDTO;
import com.antam.app.dto.PhieuDatThuocDTO;
import jakarta.persistence.EntityManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_ChiTietPhieuDat_Service {
    void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuocDTO ct);

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

