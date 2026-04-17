package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_ThongKeDoanhThu_Service;
import com.antam.app.dto.ThongKeDoanhThuDTO;
import com.antam.app.dto.NhanVienDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO class cho thống kê doanh thu
 */
public class ThongKeDoanhThu_Service implements I_ThongKeDoanhThu_Service {

    /**
     * Lấy doanh thu theo khoảng thời gian
     */
    @Override
    public ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoThoiGian(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        ArrayList<ThongKeDoanhThuDTO> dsThongKe = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT CAST(hd.NgayTao AS DATE) as Ngay, " +
            "COUNT(hd.MaHD) as SoDonHang, " +
            "SUM(hd.TongTien) as DoanhThu, " +
            "AVG(hd.TongTien) as DonHangTB, " +
            "COUNT(DISTINCT hd.MaKH) as KhachHangMoi, " +
            "(SELECT TOP 1 nv2.HoTen FROM NhanVien nv2 WHERE nv2.MaNV = MIN(hd.MaNV)) as NhanVien " +
            "FROM HoaDon hd " +
            "WHERE hd.DeleteAt = 0 AND hd.NgayTao BETWEEN ? AND ? "
        );

        if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
            sql.append("AND hd.MaNV = ? ");
        }

        sql.append("GROUP BY CAST(hd.NgayTao AS DATE) ORDER BY Ngay ASC");

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
                ps.setString(3, maNV);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ThongKeDoanhThuDTO tk = new ThongKeDoanhThuDTO(
                    rs.getDate("Ngay").toLocalDate(),
                    rs.getInt("SoDonHang"),
                    rs.getDouble("DoanhThu"),
                    rs.getDouble("DonHangTB"),
                    rs.getInt("KhachHangMoi"),
                    rs.getString("NhanVien")
                );
                dsThongKe.add(tk);
            }
        } catch (Exception e) {
            System.err.println("Lỗi getDoanhThuTheoThoiGian: " + e.getMessage());
            e.printStackTrace();
        }
        return dsThongKe;
    }

    /**
     * Lấy doanh thu theo tháng (dùng cho khoảng thời gian dài như năm)
     */
    @Override
    public ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoThang(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        ArrayList<ThongKeDoanhThuDTO> dsThongKe = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "YEAR(hd.NgayTao) as Nam, " +
            "MONTH(hd.NgayTao) as Thang, " +
            "COUNT(hd.MaHD) as SoDonHang, " +
            "SUM(hd.TongTien) as DoanhThu, " +
            "AVG(hd.TongTien) as DonHangTB, " +
            "COUNT(DISTINCT hd.MaKH) as KhachHangMoi, " +
            "(SELECT TOP 1 nv2.HoTen FROM NhanVien nv2 WHERE nv2.MaNV = MIN(hd.MaNV)) as NhanVien " +
            "FROM HoaDon hd " +
            "WHERE hd.DeleteAt = 0 AND hd.NgayTao BETWEEN ? AND ? "
        );

        if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
            sql.append("AND hd.MaNV = ? ");
        }

        sql.append("GROUP BY YEAR(hd.NgayTao), MONTH(hd.NgayTao) ORDER BY Nam ASC, Thang ASC");

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
                ps.setString(3, maNV);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int nam = rs.getInt("Nam");
                int thang = rs.getInt("Thang");
                // Tạo LocalDate đại diện cho tháng (ngày 1 của tháng)
                LocalDate ngayThang = LocalDate.of(nam, thang, 1);

                ThongKeDoanhThuDTO tk = new ThongKeDoanhThuDTO(
                    ngayThang,
                    rs.getInt("SoDonHang"),
                    rs.getDouble("DoanhThu"),
                    rs.getDouble("DonHangTB"),
                    rs.getInt("KhachHangMoi"),
                    rs.getString("NhanVien")
                );
                dsThongKe.add(tk);
            }
        } catch (Exception e) {
            System.err.println("Lỗi getDoanhThuTheoThang: " + e.getMessage());
            e.printStackTrace();
        }
        return dsThongKe;
    }

    /**
     * Lấy tổng doanh thu theo khoảng thời gian
     */
    @Override
    public double getTongDoanhThu(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        double tongDoanhThu = 0;
        StringBuilder sql = new StringBuilder(
            "SELECT ISNULL(SUM(TongTien), 0) as TongDoanhThu " +
            "FROM HoaDon WHERE DeleteAt = 0 AND NgayTao BETWEEN ? AND ? "
        );

        if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
            sql.append("AND MaNV = ?");
        }

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
                ps.setString(3, maNV);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tongDoanhThu = rs.getDouble("TongDoanhThu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tongDoanhThu;
    }

    /**
     * Lấy tổng số đơn hàng
     */
    @Override
    public int getTongDonHang(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        int tongDonHang = 0;
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(MaHD) as TongDonHang " +
            "FROM HoaDon WHERE DeleteAt = 0 AND NgayTao BETWEEN ? AND ? "
        );

        if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
            sql.append("AND MaNV = ?");
        }

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
                ps.setString(3, maNV);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tongDonHang = rs.getInt("TongDonHang");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tongDonHang;
    }

    /**
     * Lấy số khách hàng mới
     */
    @Override
    public int getSoKhachHangMoi(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        int soKhachHangMoi = 0;
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(DISTINCT MaKH) as SoKhachHangMoi " +
            "FROM HoaDon WHERE DeleteAt = 0 AND NgayTao BETWEEN ? AND ? "
        );

        if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
            sql.append("AND MaNV = ?");
        }

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            if (maNV != null && !maNV.isEmpty() && !maNV.equals("Tất cả")) {
                ps.setString(3, maNV);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                soKhachHangMoi = rs.getInt("SoKhachHangMoi");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return soKhachHangMoi;
    }

    /**
     * Lấy top sản phẩm bán chạy
     */
    @Override
    public Map<String, Integer> getTopSanPhamBanChay(LocalDate tuNgay, LocalDate denNgay, int top) {
        Map<String, Integer> topSanPham = new LinkedHashMap<>();
        String sql =
            "SELECT TOP " + top + " t.TenThuoc, SUM(cthd.SoLuong) as TongSoLuong " +
            "FROM ChiTietHoaDon cthd " +
            "JOIN HoaDon hd ON cthd.MaHD = hd.MaHD " +
            "JOIN ChiTietThuoc ctt ON cthd.MaCTT = ctt.MaCTT " +
            "JOIN Thuoc t ON ctt.MaThuoc = t.MaThuoc " +
            "WHERE hd.DeleteAt = 0 AND hd.NgayTao BETWEEN ? AND ? " +
            "GROUP BY t.TenThuoc " +
            "ORDER BY TongSoLuong DESC";

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(tuNgay));
            ps.setDate(2, Date.valueOf(denNgay));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                topSanPham.put(rs.getString("TenThuoc"), rs.getInt("TongSoLuong"));
            }
        } catch (Exception e) {
            System.err.println("Lỗi getTopSanPhamBanChay: " + e.getMessage());
            e.printStackTrace();
        }
        return topSanPham;
    }

    /**
     * Lấy danh sách nhân viên
     */
    @Override
    public ArrayList<NhanVienDTO> getDanhSachNhanVien() {
        ArrayList<NhanVienDTO> dsNhanVien = new ArrayList<>();
        String sql = "SELECT MaNV, HoTen FROM NhanVien WHERE DeleteAt = 0";

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO(rs.getString("MaNV"));
                nv.setHoTen(rs.getString("HoTen"));
                dsNhanVien.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsNhanVien;
    }
}
