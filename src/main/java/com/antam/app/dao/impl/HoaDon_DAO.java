/*
 * @ (#) HoaDon_DAO.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_HoaDon_DAO;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.KhachHang;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.NhanVien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * @description: Implementation của I_HoaDon_DAO
 * Theo chuẩn luồng dữ liệu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Đọc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa, Tran Tuan Hung
 * @date: 19/04/2026
 * @version: 1.0
 */
public class HoaDon_DAO implements I_HoaDon_DAO {

    private final NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    private final KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();

    /**
     * Lấy tất cả hóa đơn từ database
     * DB→DAO→Entity→Service→DTO
     */
    @Override
    public ArrayList<HoaDon> getAllHoaDon() {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
                while (rs.next()) {
                    HoaDon hd = mapResultSetToEntity(rs);
                    dsHoaDon.add(hd);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy tất cả hóa đơn", e);
        }
        return dsHoaDon;
    }

    /**
     * Lấy hóa đơn theo mã hóa đơn
     */
    @Override
    public HoaDon getHoaDonTheoMa(String maHD) {
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHD);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy hóa đơn theo mã", e);
        }
        return null;
    }

    /**
     * Lấy danh sách hóa đơn theo mã khách hàng
     */
    @Override
    public ArrayList<HoaDon> getHoaDonByMaKH(String maKH) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MaKH = ? AND DeleteAt = 0 ORDER BY NgayTao DESC";

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maKH);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        HoaDon hd = mapResultSetToEntity(rs);
                        dsHoaDon.add(hd);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy hóa đơn theo khách hàng", e);
        }
        return dsHoaDon;
    }

    /**
     * Tìm kiếm hóa đơn theo mã hóa đơn (LIKE)
     */
    @Override
    public ArrayList<HoaDon> searchHoaDonByMaHd(String maHd) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        if (maHd == null || maHd.trim().isEmpty()) {
            return dsHoaDon;
        }

        String sql = "SELECT * FROM HoaDon WHERE MaHD LIKE ? AND DeleteAt = 0";

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + maHd + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        HoaDon hd = mapResultSetToEntity(rs);
                        dsHoaDon.add(hd);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm hóa đơn theo mã", e);
        }
        return dsHoaDon;
    }

    /**
     * Tìm kiếm hóa đơn theo trạng thái
     */
    @Override
    public ArrayList<HoaDon> searchHoaDonByStatus(String status) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql;

        if ("Tất cả".equals(status)) {
            sql = "SELECT * FROM HoaDon";
        } else if ("Hoạt động".equals(status)) {
            sql = "SELECT * FROM HoaDon WHERE DeleteAt = 0";
        } else if ("Đã huỷ".equals(status)) {
            sql = "SELECT * FROM HoaDon WHERE DeleteAt = 1";
        } else {
            sql = "SELECT * FROM HoaDon";
        }

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
                while (rs.next()) {
                    HoaDon hd = mapResultSetToEntity(rs);
                    dsHoaDon.add(hd);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm hóa đơn theo trạng thái", e);
        }
        return dsHoaDon;
    }

    /**
     * Tìm kiếm hóa đơn theo mã nhân viên
     */
    @Override
    public ArrayList<HoaDon> searchHoaDonByMaNV(String maNV) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MaNV = ?";

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maNV);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        HoaDon hd = mapResultSetToEntity(rs);
                        dsHoaDon.add(hd);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm hóa đơn theo nhân viên", e);
        }
        return dsHoaDon;
    }

    /**
     * Thêm hóa đơn vào database
     * UI→DTO→Service→Entity→DAO→DB
     */
    @Override
    public boolean insertHoaDon(HoaDon hoaDon) {
        String sql = "INSERT INTO HoaDon (MaHD, NgayTao, MaNV, MaKH, MaKM, TongTien, DeleteAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, hoaDon.getMaHD());
                ps.setDate(2, Date.valueOf(hoaDon.getNgayTao()));
                ps.setString(3, hoaDon.getMaNV() != null ? hoaDon.getMaNV().getMaNV() : null);
                ps.setString(4, hoaDon.getMaKH() != null ? hoaDon.getMaKH().getMaKH() : null);
                if (hoaDon.getMaKM() != null) {
                    ps.setString(5, hoaDon.getMaKM().getMaKM());
                } else {
                    ps.setNull(5, Types.VARCHAR);
                }
                ps.setDouble(6, hoaDon.getTongTien());
                ps.setBoolean(7, hoaDon.isDeleteAt());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm hóa đơn", e);
        }
    }

    /**
     * Cập nhật hóa đơn trong database
     */
    @Override
    public boolean updateHoaDon(HoaDon hoaDon) {
        String sql = "UPDATE HoaDon SET NgayTao = ?, MaNV = ?, MaKH = ?, MaKM = ?, TongTien = ?, DeleteAt = ? WHERE MaHD = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(hoaDon.getNgayTao()));
                ps.setString(2, hoaDon.getMaNV() != null ? hoaDon.getMaNV().getMaNV() : null);
                ps.setString(3, hoaDon.getMaKH() != null ? hoaDon.getMaKH().getMaKH() : null);
                if (hoaDon.getMaKM() != null) {
                    ps.setString(4, hoaDon.getMaKM().getMaKM());
                } else {
                    ps.setNull(4, Types.VARCHAR);
                }
                ps.setDouble(5, hoaDon.getTongTien());
                ps.setBoolean(6, hoaDon.isDeleteAt());
                ps.setString(7, hoaDon.getMaHD());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật hóa đơn", e);
        }
    }

    /**
     * Cập nhật tổng tiền của hóa đơn
     */
    @Override
    public boolean CapNhatTongTienHoaDon(String maHD, double tongTien) {
        String sql = "UPDATE HoaDon SET TongTien = ? WHERE MaHD = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDouble(1, tongTien);
                ps.setString(2, maHD);
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật tổng tiền hóa đơn", e);
        }
    }

    /**
     * Xóa mềm hóa đơn (set DeleteAt = 1)
     */
    @Override
    public boolean xoaMemHoaDon(String maHD) {
        String sql = "UPDATE HoaDon SET DeleteAt = 1 WHERE MaHD = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maHD);
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa mềm hóa đơn", e);
        }
    }

    /**
     * Đếm số hóa đơn đã sử dụng khuyến mãi với mã cho trước
     */
    @Override
    public int soHoaDonDaCoKhuyenMaiVoiMa(String maKM) {
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE MaKM = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maKM);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đếm hóa đơn có khuyến mãi", e);
        }
        return 0;
    }

    /**
     * Lấy mã hóa đơn lớn nhất (để tạo mã tiếp theo)
     */
    @Override
    public String getMaxHash() {
        String sql = "SELECT MAX(MaHD) FROM HoaDon";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
                if (rs.next() && rs.getString(1) != null) {
                    return rs.getString(1);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy mã hóa đơn lớn nhất", e);
        }
        return "HD000";
    }

    /**
     * Helper method: Ánh xạ ResultSet thành Entity
     * Gọi các DAO khác để load relationship
     */
    private HoaDon mapResultSetToEntity(ResultSet rs) throws SQLException {
        String maHD = rs.getString("MaHD");
        LocalDate ngayTao = rs.getDate("NgayTao").toLocalDate();
        String maNV = rs.getString("MaNV");
        String maKH = rs.getString("MaKH");
        String maKM = rs.getString("MaKM");
        double tongTien = rs.getDouble("TongTien");
        boolean deleteAt = rs.getBoolean("DeleteAt");

        // Load relationship từ các DAO khác
        NhanVien nhanVien = null;
        if (maNV != null) {
            nhanVien = nhanVienDAO.findNhanVienVoiMa(maNV);
        }
        if (nhanVien == null) {
            nhanVien = new NhanVien(maNV);
        }

        KhachHang khachHang = null;
        if (maKH != null) {
            khachHang = khachHangDAO.getKhachHangTheoMa(maKH);
        }
        if (khachHang == null) {
            khachHang = new KhachHang(maKH);
        }

        KhuyenMai khuyenMai = null;
        if (maKM != null && !maKM.trim().isEmpty()) {
            khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(maKM);
        }

        return new HoaDon(maHD, ngayTao, nhanVien, khachHang, khuyenMai, tongTien, deleteAt);
    }
}
