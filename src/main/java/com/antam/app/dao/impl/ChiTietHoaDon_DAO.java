/*
 * @ (#) ChiTietHoaDon_DAO.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_ChiTietHoaDon_DAO;
import com.antam.app.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * @description: Implementation của I_ChiTietHoaDon_DAO
 * Theo chuẩn luồng dữ liệu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Đọc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa, Tran Tuan Hung
 * @date: 19/04/2026
 * @version: 1.0
 */
public class ChiTietHoaDon_DAO implements I_ChiTietHoaDon_DAO {

    private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
    private final LoThuoc_DAO loThuocDAO = new LoThuoc_DAO();
    private final DonViTinh_DAO donViTinhDAO = new DonViTinh_DAO();

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn
     * DB→DAO→Entity→Service→DTO
     */
    @Override
    public List<ChiTietHoaDon> getChiTietByMaHD(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ?";

        try {
            Connection con = ensureConnection();

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHD);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ChiTietHoaDon cthd = mapResultSetToEntity(rs);
                        list.add(cthd);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy chi tiết hóa đơn theo mã", e);
        }
        return list;
    }

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn (ArrayList version)
     */
    @Override
    public ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHD(String maHD) {
        ArrayList<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ?";

        try {
            Connection con = ensureConnection();

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHD);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ChiTietHoaDon cthd = mapResultSetToEntity(rs);
                        list.add(cthd);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy chi tiết hóa đơn", e);
        }
        return list;
    }

    /**
     * Lấy tất cả chi tiết hóa đơn với trạng thái "Bán"
     */
    @Override
    public ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHDConBan(String maHD) {
        ArrayList<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD = ? AND TinhTrang = ?";

        try {
            Connection con = ensureConnection();

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHD);
                ps.setString(2, "Bán");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ChiTietHoaDon cthd = mapResultSetToEntity(rs);
                        list.add(cthd);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy chi tiết hóa đơn con bán", e);
        }
        return list;
    }

    /**
     * Xóa mềm chi tiết hóa đơn (update trạng thái)
     */
    @Override
    public boolean xoaMemChiTietHoaDon(String maHD, int maCTT, String tinhTrang) {
        String sql = "UPDATE ChiTietHoaDon SET TinhTrang = ? WHERE MaHD = ? AND MaLoThuoc = ?";

        try {
            Connection con = ensureConnection();

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tinhTrang);
                ps.setString(2, maHD);
                ps.setInt(3, maCTT);
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa mềm chi tiết hóa đơn", e);
        }
    }

    /**
     * Thêm chi tiết hóa đơn vào database
     * UI→DTO→Service→Entity→DAO→DB
     */
    @Override
    public boolean themChiTietHoaDon(ChiTietHoaDon cthd) {
        String sql = "INSERT INTO ChiTietHoaDon (MaHD, MaLoThuoc, SoLuong, MaDVT, TinhTrang, ThanhTien) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection con = ensureConnection();

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, cthd.getMaHD().getMaHD());
                ps.setInt(2, cthd.getMaLoThuoc().getMaLoThuoc());
                ps.setInt(3, cthd.getSoLuong());
                if (cthd.getMaDVT() != null) {
                    ps.setInt(4, cthd.getMaDVT().getMaDVT());
                } else {
                    ps.setNull(4, Types.INTEGER);
                }
                ps.setString(5, cthd.getTinhTrang());
                ps.setDouble(6, cthd.getThanhTien());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm chi tiết hóa đơn", e);
        }
    }

    /**
     * Thêm chi tiết hóa đơn với trạng thái "Trả Khi Đổi"
     * Giới hạn tối đa 2 dòng với trạng thái này
     */
    @Override
    public boolean themChiTietHoaDonTraKhiDoi(ChiTietHoaDon cthd) {
        // Kiểm tra số lượng dòng có trạng thái "Trả Khi Đổi"
        String checkSql = "SELECT COUNT(*) FROM ChiTietHoaDon WHERE MaHD = ? AND MaLoThuoc = ? AND TinhTrang = ?";
        try {
            Connection con = ensureConnection();

            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                checkPs.setString(1, cthd.getMaHD().getMaHD());
                checkPs.setInt(2, cthd.getMaLoThuoc().getMaLoThuoc());
                checkPs.setString(3, "Trả Khi Đổi");
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) >= 2) {
                        return false; // Đã đạt giới hạn
                    }
                }
            }

            // Nếu chưa đạt giới hạn, thêm chi tiết hóa đơn
            return themChiTietHoaDon(cthd);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm chi tiết hóa đơn trả khi đổi", e);
        }
    }

    /**
     * Cập nhật chi tiết hóa đơn
     */
    @Override
    public boolean updateChiTietHoaDon(ChiTietHoaDon cthd) {
        String sql = "UPDATE ChiTietHoaDon SET SoLuong = ?, MaDVT = ?, TinhTrang = ?, ThanhTien = ? " +
                     "WHERE MaHD = ? AND MaLoThuoc = ?";

        try {
            Connection con = ensureConnection();

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, cthd.getSoLuong());
                if (cthd.getMaDVT() != null) {
                    ps.setInt(2, cthd.getMaDVT().getMaDVT());
                } else {
                    ps.setNull(2, Types.INTEGER);
                }
                ps.setString(3, cthd.getTinhTrang());
                ps.setDouble(4, cthd.getThanhTien());
                ps.setString(5, cthd.getMaHD().getMaHD());
                ps.setInt(6, cthd.getMaLoThuoc().getMaLoThuoc());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật chi tiết hóa đơn", e);
        }
    }

    /**
     * Kiểm tra chi tiết hóa đơn đã tồn tại theo khóa chính kép (MaHD, MaLoThuoc, TinhTrang)
     */
    @Override
    public boolean tonTaiChiTietHoaDon(String maHD, int maLoThuoc) {
        String sql = "SELECT 1 FROM ChiTietHoaDon WHERE MaHD = ? AND MaLoThuoc = ? LIMIT 1";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHD);
                ps.setInt(2, maLoThuoc);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi kiểm tra tồn tại chi tiết hóa đơn", e);
        }
    }

    /**
     * Kiểm tra chi tiết hóa đơn đã tồn tại theo khóa chính kép (MaHD, MaLoThuoc, TinhTrang)
     */
    public boolean tonTaiChiTietHoaDonTheoTinhTrang(String maHD, int maLoThuoc, String tinhTrang) {
        String sql = "SELECT 1 FROM ChiTietHoaDon WHERE MaHD = ? AND MaLoThuoc = ? AND TinhTrang = ? LIMIT 1";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHD);
                ps.setInt(2, maLoThuoc);
                ps.setString(3, tinhTrang);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi kiểm tra tồn tại chi tiết hóa đơn theo trạng thái", e);
        }
    }

    /**
     * Xóa tất cả chi tiết hóa đơn theo mã hóa đơn
     */
    @Override
    public boolean deleteChiTietHoaDonByMaHD(String maHD) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE MaHD = ?";

        try {
            Connection con = ensureConnection();

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHD);
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa chi tiết hóa đơn", e);
        }
    }

    /**
     * Helper method: Ánh xạ ResultSet thành Entity
     */
    private ChiTietHoaDon mapResultSetToEntity(ResultSet rs) throws SQLException {
        String maHD = rs.getString("MaHD");
        int maLoThuoc = rs.getInt("MaLoThuoc");
        int soLuong = rs.getInt("SoLuong");
        int maDVT = rs.getInt("MaDVT");
        boolean maDVTNull = rs.wasNull();
        String tinhTrang = rs.getString("TinhTrang");
        double thanhTien = rs.getDouble("ThanhTien");

        HoaDon hd = hoaDonDAO.getHoaDonTheoMa(maHD);
        if (hd == null) {
            hd = new HoaDon(maHD);
        }

        LoThuoc lt = loThuocDAO.getChiTietThuoc(maLoThuoc);
        if (lt == null) {
            lt = new LoThuoc();
            lt.setMaLoThuoc(maLoThuoc);
        }

        DonViTinh dvt = null;
        if (!maDVTNull) {
            dvt = donViTinhDAO.getDVTTheoMa(maDVT);
            if (dvt == null) {
                dvt = new DonViTinh(maDVT);
            }
        }

        return ChiTietHoaDon.builder()
                .MaHD(hd)
                .maLoThuoc(lt)
                .soLuong(soLuong)
                .maDVT(dvt)
                .tinhTrang(tinhTrang)
                .thanhTien(thanhTien)
                .build();
    }

    private Connection ensureConnection() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        return con;
    }
}

