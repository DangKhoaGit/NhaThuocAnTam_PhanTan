package com.antam.app.service;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dto.KhachHangDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_KhachHang_Service {
    static ArrayList<KhachHangDTO> loadBanFromDB() {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM KhachHang WHERE DeleteAt = 0";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet re = state.executeQuery();

            while (re.next()) {
                String maKH = re.getString("MaKH");
                String tenKH = re.getString("TenKH");
                String soDienThoai = re.getString("SoDienThoai");
                boolean deleteAt = re.getBoolean("DeleteAt");
                list.add(new KhachHangDTO(maKH, tenKH, soDienThoai, deleteAt));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    static List<KhachHangDTO> loadKhachHangFromDB() {
        List<KhachHangDTO> dsKhachHang = new ArrayList<>();

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

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                KhachHangDTO kh = new KhachHangDTO(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDeleteAt(rs.getBoolean("DeleteAt"));

                // Set thêm thông tin thống kê
                kh.setTongChiTieu(rs.getDouble("TongChiTieu"));
                kh.setSoDonHang(rs.getInt("SoDonHang"));

                Date ngayMuaGanNhat = rs.getDate("NgayMuaGanNhat");
                if (ngayMuaGanNhat != null) {
                    kh.setNgayMuaGanNhat(ngayMuaGanNhat.toLocalDate());
                }

                dsKhachHang.add(kh);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsKhachHang;
    }

    static List<KhachHangDTO> searchKhachHangByName(String tenKH) {
        List<KhachHangDTO> dsKhachHang = new ArrayList<>();

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql =
                    """
                SELECT kh.MaKH, kh.TenKH, kh.
                    SoDienThoai, kh.DeleteAt,
                       COALESCE(SUM
                    (hd.TongTien), 0) 
                                          COUNT(hd.MaHD) as 
                                     MAX(hd.NgayTao)
                    as NgayMuaGanNhat
                FROM KhachH
                          LEFT JOIN HoaDon hd ON kh.MaKH = hd.MaKH AND hd.
                    deleteAt = 0
                WHERE kh.DeleteAt = 0 AND kh.
                    T
                               GROUP BY kh.MaKH, kh.TenKH, kh.SoDienThoai, kh.DeleteAt
                ORDER BY kh.TenKH
                """;

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + tenKH + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                KhachHangDTO kh = new KhachHangDTO(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDeleteAt(rs.getBoolean("DeleteAt"));

                // Set thêm thông tin thống kê
                kh.setTongChiTieu(rs.getDouble("TongChiTieu"));
                kh.setSoDonHang(rs.getInt("SoDonHang"));

                Date ngayMuaGanNhat = rs.getDate("NgayMuaGanNhat");
                if (ngayMuaGanNhat != null) {
                    kh.setNgayMuaGanNhat(ngayMuaGanNhat.toLocalDate());
                }

                dsKhachHang.add(kh);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsKhachHang;
    }

    static int getTongKhachHang() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = "SELECT COUNT(*) FROM KhachHang WHERE DeleteAt = 0";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static int getTongKhachHangVIP() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().
                    connect();
                con = ConnectDB.getConnection();
            }

            String sql
                    = """
                SELECT COUNT(*) FROM (
                                    SELECT kh.MaK
                         FROM KhachHang 
                          LEFT JOIN HoaDon hd ON kh.MaKH = hd.MaKH AND 
                                       WHERE kh.
                    DeleteAt = 0
                    GROUP BY kh.MaKH
                    HAVING COALESCE(SUM(hd.TongTien), 0) >= 1000000
                ) as VipCustomers
                """;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static int getTongDonHang() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = "SELECT COUNT(*) FROM HoaDon WHERE deleteAt = 0";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    static double getTongDoanhThu() {
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            String sql = "SELECT COALESCE(SUM(TongTien), 0) FROM HoaDon WHERE deleteAt = 0";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    boolean insertKhachHang(KhachHangDTO kh);

    boolean updateKhachHang(KhachHangDTO kh);

    KhachHangDTO getKhachHangTheoMa(String maKH);

    KhachHangDTO getKhachHangTheoSoDienThoai(String soDienThoai);

    ArrayList<KhachHangDTO> getAllKhachHang();

    int getMaxHash();
}
