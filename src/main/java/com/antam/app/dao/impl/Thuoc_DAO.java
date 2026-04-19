/*
 * @ (#) Thuoc_DAO.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_Thuoc_DAO;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.Ke;
import com.antam.app.entity.Thuoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * @description: Implementation của I_Thuoc_DAO
 * Theo chuẩn luồng dữ liệu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Đọc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa
 * @date: 19/04/2026
 * @version: 1.0
 */
public class Thuoc_DAO implements I_Thuoc_DAO {

    private static final String BASE_SELECT = """
        SELECT t.MaThuoc, t.TenThuoc, t.HamLuong, t.GiaBan, t.GiaGoc, t.Thue, t.DeleteAt,
               t.DangDieuChe, ddc.TenDDC,
             t.MaDVTCoso, dvt.TenDVT, dvt.isDelete AS DVTDeleteAt,
               t.MaKe, k.TenKe, k.LoaiKe, k.DeleteAt AS KeDeleteAt
        FROM Thuoc t
        LEFT JOIN DangDieuChe ddc ON t.DangDieuChe = ddc.MaDDC
        LEFT JOIN DonViTinh dvt ON t.MaDVTCoso = dvt.MaDVT
        LEFT JOIN KeThuoc k ON t.MaKe = k.MaKe
    """;

    @Override
    public ArrayList<Thuoc> getAllThuoc() {
        ArrayList<Thuoc> listThuoc = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE t.DeleteAt = 0 ORDER BY t.MaThuoc";

        try {
            Connection con = ensureConnection();
            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
                while (rs.next()) {
                    listThuoc.add(mapResultSetToEntity(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay danh sach thuoc", e);
        }
        return listThuoc;
    }

    @Override
    public ArrayList<Thuoc> getAllThuocDaXoa() {
        ArrayList<Thuoc> listThuoc = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE t.DeleteAt = 1 ORDER BY t.MaThuoc";

        try {
            Connection con = ensureConnection();
            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
                while (rs.next()) {
                    listThuoc.add(mapResultSetToEntity(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay danh sach thuoc da xoa", e);
        }
        return listThuoc;
    }

    @Override
    public Thuoc getThuocTheoMa(String ma) {
        String sql = BASE_SELECT + " WHERE t.MaThuoc = ? AND t.DeleteAt = 0";

        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, ma);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay thuoc theo ma", e);
        }
        return null;
    }

    @Override
    public boolean themThuoc(Thuoc t) {
        String sql = """
            INSERT INTO Thuoc
            (MaThuoc, TenThuoc, HamLuong, GiaBan, GiaGoc, Thue, DangDieuChe, MaDVTCoso, MaKe, DeleteAt)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, t.getMaThuoc());
                ps.setString(2, t.getTenThuoc());
                ps.setString(3, t.getHamLuong());
                ps.setDouble(4, t.getGiaBan());
                ps.setDouble(5, t.getGiaGoc());
                ps.setFloat(6, t.getThue());
                ps.setInt(7, t.getDangDieuChe() != null ? t.getDangDieuChe().getMaDDC() : 0);
                ps.setInt(8, t.getMaDVTCoSo() != null ? t.getMaDVTCoSo().getMaDVT() : 0);
                ps.setString(9, t.getMaKe() != null ? t.getMaKe().getMaKe() : null);
                ps.setBoolean(10, t.isDeleteAt());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them thuoc", e);
        }
    }

    @Override
    public boolean capNhatThuoc(Thuoc t) {
        String sql = """
            UPDATE Thuoc
            SET TenThuoc = ?, HamLuong = ?, GiaBan = ?, GiaGoc = ?, Thue = ?,
                DangDieuChe = ?, MaDVTCoso = ?, MaKe = ?, DeleteAt = ?
            WHERE MaThuoc = ?
        """;

        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, t.getTenThuoc());
                ps.setString(2, t.getHamLuong());
                ps.setDouble(3, t.getGiaBan());
                ps.setDouble(4, t.getGiaGoc());
                ps.setFloat(5, t.getThue());
                ps.setInt(6, t.getDangDieuChe() != null ? t.getDangDieuChe().getMaDDC() : 0);
                ps.setInt(7, t.getMaDVTCoSo() != null ? t.getMaDVTCoSo().getMaDVT() : 0);
                ps.setString(8, t.getMaKe() != null ? t.getMaKe().getMaKe() : null);
                ps.setBoolean(9, t.isDeleteAt());
                ps.setString(10, t.getMaThuoc());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat thuoc", e);
        }
    }

    @Override
    public boolean xoaThuocTheoMa(String ma) {
        return updateDeleteState(ma, true);
    }

    @Override
    public boolean khoiPhucThuocTheoMa(String ma) {
        return updateDeleteState(ma, false);
    }

    private boolean updateDeleteState(String ma, boolean deleteAt) {
        String sql = "UPDATE Thuoc SET DeleteAt = ? WHERE MaThuoc = ?";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setBoolean(1, deleteAt);
                ps.setString(2, ma);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat trang thai xoa cua thuoc", e);
        }
    }

    private Connection ensureConnection() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        return con;
    }

    private Thuoc mapResultSetToEntity(ResultSet rs) throws SQLException {
        String maThuoc = rs.getString("MaThuoc");
        String tenThuoc = rs.getString("TenThuoc");
        String hamLuong = rs.getString("HamLuong");
        double giaBan = rs.getDouble("GiaBan");
        double giaGoc = rs.getDouble("GiaGoc");
        float thue = rs.getFloat("Thue");
        boolean deleteAt = rs.getBoolean("DeleteAt");

        int maDDC = rs.getInt("DangDieuChe");
        String tenDDC = rs.getString("TenDDC");
        DangDieuChe dangDieuChe = new DangDieuChe(maDDC, tenDDC == null ? "" : tenDDC);

        int maDVT = rs.getInt("MaDVTCoso");
        String tenDVT = rs.getString("TenDVT");
        boolean dvtDeleteAt = rs.getBoolean("DVTDeleteAt");
        DonViTinh donViTinh = new DonViTinh(maDVT, tenDVT == null ? "" : tenDVT, dvtDeleteAt);

        String maKe = rs.getString("MaKe");
        String tenKe = rs.getString("TenKe");
        String loaiKe = rs.getString("LoaiKe");
        boolean keDeleteAt = rs.getBoolean("KeDeleteAt");
        Ke ke = maKe == null ? new Ke() : new Ke(maKe, tenKe == null ? "" : tenKe, loaiKe == null ? "" : loaiKe, keDeleteAt);

        return new Thuoc(maThuoc, tenThuoc, hamLuong, giaBan, giaGoc, thue, deleteAt, dangDieuChe, donViTinh, ke);
    }
}
