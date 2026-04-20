package com.antam.app.dao;

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
import com.antam.app.dao.impl.PhieuDat_DAO;
import com.antam.app.entity.ChiTietPhieuDatThuoc;
import com.antam.app.entity.KhachHang;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.NhanVien;
import com.antam.app.entity.PhieuDatThuoc;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_PhieuDat_DAO {
    /**
     * Lấy toàn bộ danh sách phiếu đặt thuốc với thông tin bao gồm
     * mã, ngày tạo, đã thanh toán hay chưa, mã khách, mã nhân viên tạo
     * thành tiền của phiếu đặt
     *
     * @return Array[PhieuDatThuoc]
     */
    List<PhieuDatThuoc> getAllPhieuDatThuocFromDBS();

    List<PhieuDatThuoc> getAllPhieuDatThuocDaXoa();

    boolean themPhieuDatThuocVaoDBS(PhieuDatThuoc i);

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

    void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuoc ctPDT);

    boolean capNhatThanhToanPhieuDat(String maPDT);

    PhieuDatThuoc getPhieuDatByMaFromDBS(String maPDT);

    boolean khoiPhucPhieuDat(String maPhieu);
}
