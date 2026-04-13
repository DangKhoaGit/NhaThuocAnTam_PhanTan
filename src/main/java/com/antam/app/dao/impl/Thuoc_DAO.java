/*
 * @ (#) Thuoc_DAO.java   1.0 9/24/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.Ke;
import com.antam.app.entity.Thuoc;

import java.sql.*;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 9/24/2025
 * version: 1.0
 */
public class Thuoc_DAO implements com.antam.app.dao.I_Thuoc_DAO {
    /**
     * Phương thức thao tác với bảng Thuoc trong CSDL
     * return: ArrayList<Thuoc> - danh sách thuốc
     */
    @Override
    public ArrayList<Thuoc> getAllThuoc() {
        ArrayList<Thuoc> listThuoc = new ArrayList<>();
        String sql = """
            SELECT t.MaThuoc, t.TenThuoc,
                   t.HamLuong, t.GiaBan, t.GiaGoc, t.Thue, t.MaDVTCoso, t.deleteAt,
                   ddc.MaDDC, ddc.TenDDC,
                   k.MaKe, k.TenKe, k.LoaiKe
            FROM Thuoc t
            JOIN DangDieuChe ddc ON t.DangDieuChe = ddc.MaDDC
            JOIN KeThuoc k ON t.MaKe = k.MaKe
            WHERE t.deleteAt = 0
        """;
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try  {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);

            while (rs.next()) {
                String maThuoc = rs.getString("MaThuoc");
                String tenThuoc = rs.getString("TenThuoc");
                String hamLuong = rs.getString("HamLuong");
                double giaBan = rs.getDouble("GiaBan");
                double giaGoc = rs.getDouble("GiaGoc");
                float thue = rs.getFloat("Thue");
                int maDonViTinhCoSo = rs.getInt("MaDVTCoso");
                boolean deleteAt = rs.getBoolean("deleteAt");

                // Tạo object DDC và Ke với đủ thông tin
                DangDieuChe ddc = new DangDieuChe(rs.getInt("MaDDC"), rs.getString("TenDDC"));
                Ke ke = new Ke(rs.getString("MaKe"), rs.getString("TenKe"), rs.getString("LoaiKe"), false);
                DonViTinh dvt = new DonViTinh(maDonViTinhCoSo);

                Thuoc thuoc = new Thuoc();
                listThuoc.add(thuoc);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listThuoc;
    }

    /**
     * Phương thức thao tác với bảng Thuoc trong CSDL
     * return: ArrayList<Thuoc> - danh sách thuốc
     */
    @Override
    public ArrayList<Thuoc> getAllThuocDaXoa() {
        ArrayList<Thuoc> listThuoc = new ArrayList<>();
        String sql = """
            SELECT t.MaThuoc, t.TenThuoc,
                   t.HamLuong, t.GiaBan, t.GiaGoc, t.Thue, t.MaDVTCoso, t.deleteAt,
                   ddc.MaDDC, ddc.TenDDC,
                   k.MaKe, k.TenKe, k.LoaiKe
            FROM Thuoc t
            JOIN DangDieuChe ddc ON t.DangDieuChe = ddc.MaDDC
            JOIN KeThuoc k ON t.MaKe = k.MaKe
            WHERE t.deleteAt = 1
        """;
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try  {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);

            while (rs.next()) {
                String maThuoc = rs.getString("MaThuoc");
                String tenThuoc = rs.getString("TenThuoc");
                String hamLuong = rs.getString("HamLuong");
                double giaBan = rs.getDouble("GiaBan");
                double giaGoc = rs.getDouble("GiaGoc");
                float thue = rs.getFloat("Thue");
                int maDonViTinhCoSo = rs.getInt("MaDVTCoso");
                boolean deleteAt = rs.getBoolean("deleteAt");

                // Tạo object DDC và Ke với đủ thông tin
                DangDieuChe ddc = new DangDieuChe(rs.getInt("MaDDC"), rs.getString("TenDDC"));
                Ke ke = new Ke(rs.getString("MaKe"), rs.getString("TenKe"), rs.getString("LoaiKe"), false);
                DonViTinh dvt = new DonViTinh(maDonViTinhCoSo);

                Thuoc thuoc = new Thuoc();
                listThuoc.add(thuoc);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listThuoc;
    }

    /**
     * Lấy thông tin thuốc theo mã thuốc
     * @param ma
     * @return Thuoc
     */
    @Override
    public Thuoc getThuocTheoMa(String ma) {
        Thuoc t = null;
        String sql = """
            SELECT t.MaThuoc, t.TenThuoc,
                   t.HamLuong, t.GiaBan, t.GiaGoc, t.Thue, t.MaDVTCoso, t.deleteAt,
                   ddc.MaDDC, ddc.TenDDC,
                   k.MaKe, k.TenKe, k.LoaiKe
            FROM Thuoc t
            JOIN DangDieuChe ddc ON t.DangDieuChe = ddc.MaDDC
            JOIN KeThuoc k ON t.MaKe = k.MaKe
            WHERE t.MaThuoc = ? AND t.deleteAt = 0
        """;
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ma);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String maThuoc = rs.getString("MaThuoc");
                String tenThuoc = rs.getString("TenThuoc");
                String hamLuong = rs.getString("HamLuong");
                double giaBan = rs.getDouble("GiaBan");
                double giaGoc = rs.getDouble("GiaGoc");
                float thue = rs.getFloat("Thue");
                int maDonViTinhCoSo = rs.getInt("MaDVTCoso");
                boolean deleteAt = rs.getBoolean("deleteAt");

                // Tạo object DDC và Ke với đủ thông tin
                DangDieuChe ddc = new DangDieuChe(rs.getInt("MaDDC"), rs.getString("TenDDC"));
                Ke ke = new Ke(rs.getString("MaKe"), rs.getString("TenKe"), rs.getString("LoaiKe"), false);
                DonViTinh dvt = new DonViTinh(maDonViTinhCoSo);

                t = new Thuoc();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * Thêm thuốc mới vào CSDL
     * @param t
     * @return boolean
     */
    @Override
    public boolean themThuoc(Thuoc t) {
        String sql = "INSERT INTO Thuoc (MaThuoc, TenThuoc, HamLuong, GiaBan, GiaGoc, Thue, DangDieuChe, MaDVTCoso, MaKe, deleteAt) " +
                "VALUES (?,?,?,?,?,?,?,?,?,0)";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            var ps = con.prepareStatement(sql);
            ps.setString(1, t.getMaThuoc());
            ps.setString(2, t.getTenThuoc());
            ps.setString(3, t.getHamLuong());
            ps.setDouble(4, t.getGiaBan());
            ps.setDouble(5, t.getGiaGoc());
            ps.setFloat(6, t.getThue());
//            ps.setInt(7, t.getDangDieuChe().getMaDDC());
//            ps.setInt(8, t.getMaDVTCoSo().getMaDVT());
//            ps.setString(9, t.getMaKe().getMaKe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật thông tin thuốc
     * @param t
     * @return boolean
     */
    @Override
    public boolean capNhatThuoc(Thuoc t) {
        String sql = "UPDATE Thuoc SET TenThuoc = ?, HamLuong = ?, GiaBan = ?, GiaGoc = ?, Thue = ?, DangDieuChe = ?, MaDVTCoso = ?, MaKe = ?, DeleteAt = ? WHERE MaThuoc = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            var ps = con.prepareStatement(sql);
            ps.setString(1, t.getTenThuoc());
            ps.setString(2, t.getHamLuong());
            ps.setDouble(3, t.getGiaBan());
            ps.setDouble(4, t.getGiaGoc());
            ps.setFloat(5, t.getThue());
//            ps.setInt(6, t.getDangDieuChe().getMaDDC());
//            ps.setInt(7, t.getMaDVTCoSo().getMaDVT());
//            ps.setString(8, t.getMaKe().getMaKe());
            ps.setBoolean(9, t.isDeleteAt());
            ps.setString(10, t.getMaThuoc());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa thuốc theo mã (cập nhật DeleteAt = 1)
     * @param ma
     * @return boolean
     */
    @Override
    public boolean xoaThuocTheoMa(String ma) {
        String sql = "UPDATE Thuoc SET DeleteAt = 1 WHERE MaThuoc = ?";
        PreparedStatement statement = null;
        int n = 0;
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            statement = con.prepareStatement(sql);
            statement.setString(1, ma);
            n = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

    @Override
    public boolean khoiPhucThuocTheoMa(String ma) {
        String sql = "UPDATE Thuoc SET DeleteAt = 0 WHERE MaThuoc = ?";
        PreparedStatement statement = null;
        int n = 0;
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            statement = con.prepareStatement(sql);
            statement.setString(1, ma);
            n = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

}
