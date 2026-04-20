package com.antam.app.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dto.ChiTietPhieuDatThuocDTO;
import com.antam.app.dto.KhachHangDTO;
import com.antam.app.dto.KhuyenMaiDTO;
import com.antam.app.dto.NhanVienDTO;
import com.antam.app.dto.PhieuDatThuocDTO;
import com.antam.app.service.impl.PhieuDat_Service;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_PhieuDat_Service {
    /**
     * Lấy toàn bộ danh sách phiếu đặt thuốc với thông tin bao gồm
     * mã, ngày tạo, đã thanh toán hay chưa, mã khách, mã nhân viên tạo
     * thành tiền của phiếu đặt
     *
     * @return Array[PhieuDatThuoc]
     */
    List<PhieuDatThuocDTO> getAllPhieuDatThuocFromDBS();

    ArrayList<PhieuDatThuocDTO> getAllPhieuDatThuocDaXoa();

    boolean themPhieuDatThuocVaoDBS(PhieuDatThuocDTO i);

    /**
     * Xoá phiếu đặt thuốc trong DBS bằng cách tắt trạng thái hoạt động.
     *
     * @param maPDT mã phiếu đặt thuốc
     * @return true nếu cập nhật thành công. false nếu không thể cập nhật.
     */
    boolean xoaPhieuDatThuocTrongDBS(String maPDT);
    /**
     * Lấy mã hash lớn nhẩt trong database
     *
     * @return String - mã phiếu đặt thuốc mới nhất.
     * null nếu không có gì trong dbs.
     */
    String getMaxHash();

    void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuocDTO ctPDT);

    boolean capNhatThanhToanPhieuDat(String maPDT);

    PhieuDatThuocDTO getPhieuDatByMaFromDBS(String maPDT);

    boolean khoiPhucPhieuDat(String maPhieu);
}
