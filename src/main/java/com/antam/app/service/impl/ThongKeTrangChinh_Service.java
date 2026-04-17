/*
 * @ (#) ThongKeTrangChinh_DAO.java   1.0 14/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_ThongKeTrangChinh_Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @description DAO class for dashboard statistics
 * @author: Tran Tuan Hung
 * @date: 14/10/25
 * @version: 1.0
 */
public class ThongKeTrangChinh_Service implements I_ThongKeTrangChinh_Service {

    /**
     * Lấy tổng số thuốc trong hệ thống
     */
    @Override
    public int getTongSoThuoc() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Thuoc WHERE deleteAt = 0";

        try {
            Connection con = ConnectDB.getInstance().connect();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting total medicines: " + e.getMessage());
        }

        return count;
    }

    /**
     * Lấy tổng số nhân viên
     */
    @Override
    public int getTongSoNhanVien() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE deleteAt = 0";

        try {
            Connection con = ConnectDB.getInstance().connect();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting total employees: " + e.getMessage());
        }

        return count;
    }

    /**
     * Lấy số hóa đơn hôm nay
     */
    @Override
    public int getSoHoaDonHomNay() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE CAST(ngayTao AS DATE) = CAST(GETDATE() AS DATE) AND deleteAt = 0";

        try {
            Connection con = ConnectDB.getInstance().connect();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting today's invoices: " + e.getMessage());
        }

        return count;
    }

    /**
     * Lấy số khuyến mãi đang áp dụng
     */
    @Override
    public int getSoKhuyenMaiApDung() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM KhuyenMai WHERE GETDATE() BETWEEN ngayBatDau AND ngayKetThuc AND deleteAt = 0";

        try {
            Connection con = ConnectDB.getInstance().connect();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting active promotions: " + e.getMessage());
        }

        return count;
    }

    /**
     * Lấy doanh thu 7 ngày gần nhất
     */
    @Override
    public Map<String, Double> getDoanhThu7NgayGanNhat() {
        Map<String, Double> doanhThu = new HashMap<>();
        String sql = """
            SELECT
                CAST(ngayTao AS DATE) as ngay,
                SUM(tongTien) as doanhThu
            FROM HoaDon
            WHERE ngayTao >= DATEADD(day, -7, GETDATE())
                AND deleteAt = 0
            GROUP BY CAST(ngayTao AS DATE)
            ORDER BY ngay
            """;

        try {
            Connection con = ConnectDB.getInstance().connect();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String ngay = rs.getDate("ngay").toString();
                double revenue = rs.getDouble("doanhThu");
                doanhThu.put(ngay, revenue);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting 7-day revenue: " + e.getMessage());
        }

        return doanhThu;
    }

    /**
     * Lấy top sản phẩm bán chạy
     */
    @Override
    public Map<String, Integer> getTopSanPhamBanChay(int limit) {
        Map<String, Integer> topProducts = new HashMap<>();
        String sql = """
            SELECT TOP (?)
                t.tenThuoc,
                SUM(cthd.SoLuong) as tongSoLuong
            FROM ChiTietHoaDon cthd
            JOIN ChiTietThuoc ctt ON cthd.MaCTT = ctt.MaCTT
            JOIN Thuoc t ON ctt.MaThuoc = t.maThuoc
            JOIN HoaDon hd ON cthd.MaHD = hd.maHD
            WHERE hd.ngayTao >= DATEADD(month, -1, GETDATE())
                AND t.deleteAt = 0 AND hd.deleteAt = 0
            GROUP BY t.tenThuoc
            ORDER BY tongSoLuong DESC
            """;

        try {
            Connection con = ConnectDB.getInstance().connect();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String tenThuoc = rs.getString("tenThuoc");
                int soLuong = rs.getInt("tongSoLuong");
                topProducts.put(tenThuoc, soLuong);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting top selling products: " + e.getMessage());
        }

        return topProducts;
    }

    /**
     * Lấy danh sách thuốc sắp hết hạn (trong vòng 30 ngày)
     */
    @Override
    public List<Map<String, Object>> getThuocSapHetHan() {
        List<Map<String, Object>> thuocSapHetHan = new ArrayList<>();
        String sql = """
            SELECT TOP 10
                t.tenThuoc,
                ct.HanSuDung,
                ct.TonKho as soLuongTon,
                ct.MaCTT
            FROM ChiTietThuoc ct
            JOIN Thuoc t ON ct.MaThuoc = t.maThuoc
            WHERE ct.HanSuDung <= DATEADD(day, 30, GETDATE())
                AND ct.HanSuDung >= CAST(GETDATE() AS DATE)
                AND ct.TonKho > 0
                AND t.deleteAt = 0
            ORDER BY ct.HanSuDung ASC
            """;

        try {
            Connection con = ConnectDB.getInstance().connect();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> thuocDTO = new HashMap<>();
                thuocDTO.put("tenThuoc", rs.getString("tenThuoc"));
                thuocDTO.put("hanSuDung", rs.getDate("HanSuDung"));
                thuocDTO.put("soLuongTon", rs.getInt("soLuongTon"));
                thuocSapHetHan.add(thuocDTO);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting expiring medicines: " + e.getMessage());
            e.printStackTrace();
        }

        return thuocSapHetHan;
    }

    /**
     * Lấy danh sách thuốc tồn kho thấp
     */
    @Override
    public List<Map<String, Object>> getThuocTonKhoThap() {
        List<Map<String, Object>> thuocTonKhoThap = new ArrayList<>();
        String sql = """
            SELECT TOP 10
                t.maThuoc,
                t.tenThuoc,
                SUM(ct.TonKho) as tongTonKho
            FROM ChiTietThuoc ct
            JOIN Thuoc t ON ct.MaThuoc = t.maThuoc
            WHERE t.deleteAt = 0 AND ct.TonKho >= 0
            GROUP BY t.maThuoc, t.tenThuoc
            HAVING SUM(ct.TonKho) <= 50 AND SUM(ct.TonKho) > 0
            ORDER BY tongTonKho ASC
            """;

        try {
            Connection con = ConnectDB.getInstance().connect();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> thuocDTO = new HashMap<>();
                thuocDTO.put("maThuoc", rs.getString("maThuoc"));
                thuocDTO.put("tenThuoc", rs.getString("tenThuoc"));
                thuocDTO.put("soLuongTon", rs.getInt("tongTonKho"));
                thuocTonKhoThap.add(thuocDTO);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error getting low stock medicines: " + e.getMessage());
            e.printStackTrace();
        }

        return thuocTonKhoThap;
    }
}
