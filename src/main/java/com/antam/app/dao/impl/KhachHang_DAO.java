/*
 * @ (#) KhachHang_DAO.java   1.0 1/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_KhachHang_DAO;
import com.antam.app.entity.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * @description: DAO implementation cho KhachHang - xử lý trực tiếp SQL/JDBC
 * @author: Tran Tuan Hung
 * @date: 1/10/25
 * @version: 2.0 (refactored according to n-layer architecture)
 */
public class KhachHang_DAO implements I_KhachHang_DAO {

    // ========== CRUD METHODS ==========
    /**
     * Thêm khách hàng mới vào CSDL
     * @param kh KhachHang entity
     * @return true nếu thành công, false nếu thất bại
     */
    @Override
    public boolean insertKhachHang(KhachHang kh) {
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "INSERT INTO KhachHang (MaKH, TenKH, SoDienThoai, DeleteAt) VALUES (?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, kh.getMaKH());
                ps.setString(2, kh.getTenKH());
                ps.setString(3, kh.getSoDienThoai());
                ps.setBoolean(4, kh.isDeleteAt());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thêm khách hàng vào DB", e);
        }
    }

    /**
     * Cập nhật thông tin khách hàng trong CSDL
     * @param kh KhachHang entity
     * @return true nếu thành công, false nếu thất bại
     */
    @Override
    public boolean updateKhachHang(KhachHang kh) {
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "UPDATE KhachHang SET TenKH = ?, SoDienThoai = ? WHERE MaKH = ? AND DeleteAt = 0";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, kh.getTenKH());
                ps.setString(2, kh.getSoDienThoai());
                ps.setString(3, kh.getMaKH());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật khách hàng", e);
        }
    }

    /**
     * Tìm khách hàng theo mã
     * @param maKH mã khách hàng
     * @return KhachHang entity nếu tìm thấy, null nếu không
     */
    @Override
    public KhachHang getKhachHangTheoMa(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE MaKH = ? AND DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, maKH);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy khách hàng theo mã", e);
        }
        return null;
    }

    /**
     * Tìm khách hàng theo số điện thoại
     * @param soDienThoai số điện thoại
     * @return KhachHang entity nếu tìm thấy, null nếu không
     */
    @Override
    public KhachHang getKhachHangTheoSoDienThoai(String soDienThoai) {
        String sql = "SELECT * FROM KhachHang WHERE SoDienThoai = ? AND DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, soDienThoai);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy khách hàng theo số điện thoại", e);
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả khách hàng (không bao gồm thống kê)
     * @return danh sách KhachHang
     */
    @Override
    public ArrayList<KhachHang> getAllKhachHang() {
        ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE DeleteAt = 0";

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    KhachHang kh = mapResultSetToEntity(rs);
                    dsKhachHang.add(kh);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy danh sách khách hàng", e);
        }
        return dsKhachHang;
    }

    /**
     * Lấy danh sách tất cả khách hàng với thông tin thống kê
     * @return danh sách KhachHang có thông tin chi tiêu, số đơn hàng, ngày mua gần nhất
     */
    @Override
    public List<KhachHang> getAllKhachHangWithStats() {
        List<KhachHang> dsKhachHang = new ArrayList<>();

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = """
                    SELECT kh.MaKH, kh.TenKH, kh.SoDienThoai, kh.DeleteAt,
                           COALESCE(SUM(hd.TongTien), 0) as TongChiTieu,
                           COUNT(hd.MaHD) as SoDonHang,
                           MAX(hd.NgayTao) as NgayMuaGanNhat
                    FROM KhachHang kh
                    LEFT JOIN HoaDon hd ON kh.MaKH = hd.MaKH AND hd.deleteAt = 0
                    WHERE kh.DeleteAt = 0
                    GROUP BY kh.MaKH, kh.TenKH, kh.SoDienThoai, kh.DeleteAt
                    ORDER BY kh.TenKH
                    """;

            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    KhachHang kh = mapResultSetToEntityWithStats(rs);
                    dsKhachHang.add(kh);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy danh sách khách hàng với thống kê", e);
        }
        return dsKhachHang;
    }

    /**
     * Tìm khách hàng theo tên (LIKE search)
     * @param tenKH tên khách hàng để tìm
     * @return danh sách KhachHang phù hợp với thống kê
     */
    @Override
    public List<KhachHang> searchKhachHangByName(String tenKH) {
        List<KhachHang> dsKhachHang = new ArrayList<>();

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = """
                    SELECT kh.MaKH, kh.TenKH, kh.SoDienThoai, kh.DeleteAt,
                           COALESCE(SUM(hd.TongTien), 0) as TongChiTieu,
                           COUNT(hd.MaHD) as SoDonHang,
                           MAX(hd.NgayTao) as NgayMuaGanNhat
                    FROM KhachHang kh
                    LEFT JOIN HoaDon hd ON kh.MaKH = hd.MaKH AND hd.deleteAt = 0
                    WHERE kh.DeleteAt = 0 AND kh.TenKH LIKE ?
                    GROUP BY kh.MaKH, kh.TenKH, kh.SoDienThoai, kh.DeleteAt
                    ORDER BY kh.TenKH
                    """;

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, "%" + tenKH + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        KhachHang kh = mapResultSetToEntityWithStats(rs);
                        dsKhachHang.add(kh);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi tìm kiếm khách hàng", e);
        }
        return dsKhachHang;
    }

    /**
     * Lấy mã khách hàng tiếp theo
     * @return số hash tối đa của mã khách hàng
     */
    @Override
    public int getMaxHash() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            // MariaDB syntax: LENGTH thay vì LEN, UNSIGNED thay vì INT
            String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(MaKH, 3) AS UNSIGNED)), 0) FROM KhachHang WHERE DeleteAt = 0";

            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy mã khách hàng tối đa", e);
        }
        return 0;
    }

    // ========== STATISTICS METHODS ==========
    /**
     * Lấy tổng số khách hàng
     * @return số lượng khách hàng
     */
    @Override
    public int getTongKhachHang() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = "SELECT COUNT(*) FROM KhachHang WHERE DeleteAt = 0";

            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy tổng số khách hàng", e);
        }
        return 0;
    }

    /**
     * Lấy tổng số khách hàng VIP (chi tiêu >= 1.000.000)
     * @return số lượng khách hàng VIP
     */
    @Override
    public int getTongKhachHangVIP() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = """
                    SELECT COUNT(*) FROM (
                        SELECT kh.MaKH
                        FROM KhachHang kh
                        LEFT JOIN HoaDon hd ON kh.MaKH = hd.MaKH AND hd.deleteAt = 0
                        WHERE kh.DeleteAt = 0
                        GROUP BY kh.MaKH
                        HAVING COALESCE(SUM(hd.TongTien), 0) >= 1000000
                    ) as VipCustomers
                    """;

            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy tổng số khách hàng VIP", e);
        }
        return 0;
    }

    /**
     * Lấy tổng số đơn hàng
     * @return tổng số đơn hàng
     */
    @Override
    public int getTongDonHang() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = "SELECT COUNT(*) FROM HoaDon WHERE deleteAt = 0";

            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy tổng số đơn hàng", e);
        }
        return 0;
    }

    /**
     * Lấy tổng doanh thu
     * @return tổng doanh thu
     */
    @Override
    public double getTongDoanhThu() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = "SELECT COALESCE(SUM(TongTien), 0) FROM HoaDon WHERE deleteAt = 0";

            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy tổng doanh thu", e);
        }
        return 0;
    }

    // ========== HELPER METHODS ==========
    /**
     * Map ResultSet sang KhachHang entity (cơ bản)
     */
    private KhachHang mapResultSetToEntity(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang(rs.getString("MaKH"));
        kh.setTenKH(rs.getString("TenKH"));
        kh.setSoDienThoai(rs.getString("SoDienThoai"));
        kh.setDeleteAt(rs.getBoolean("DeleteAt"));
        return kh;
    }

    /**
     * Map ResultSet sang KhachHang entity (với thống kê)
     */
    private KhachHang mapResultSetToEntityWithStats(ResultSet rs) throws SQLException {
        KhachHang kh = mapResultSetToEntity(rs);
        
        // Set thông tin thống kê
        kh.setTongChiTieu(rs.getDouble("TongChiTieu"));
        kh.setSoDonHang(rs.getInt("SoDonHang"));
        
        Date ngayMuaGanNhat = rs.getDate("NgayMuaGanNhat");
        if (ngayMuaGanNhat != null) {
            kh.setNgayMuaGanNhat(ngayMuaGanNhat.toLocalDate());
        }
        
        return kh;
    }
}
