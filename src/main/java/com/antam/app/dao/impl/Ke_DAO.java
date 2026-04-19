/*
 * @ (#) Ke_DAO.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_Ke_DAO;
import com.antam.app.entity.Ke;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class Ke_DAO implements I_Ke_DAO {

    /* Duy - Xoa ke */
    @Override
    public boolean xoaKe(String maKe) {
        String sql = "UPDATE KeThuoc SET DeleteAt = 1 WHERE MaKe = ? AND DeleteAt = 0";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maKe);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa ke", e);
        }
    }

    /* Duy - Khoi phuc ke */
    @Override
    public boolean khoiPhucKe(String maKe) {
        String sql = "UPDATE KeThuoc SET DeleteAt = 0 WHERE MaKe = ? AND DeleteAt = 1";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maKe);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi khoi phuc ke", e);
        }
    }

    /* Duy - Sua ke */
    @Override
    public boolean suaKe(Ke ke) {
        String sql = "UPDATE KeThuoc SET TenKe = ?, LoaiKe = ? WHERE MaKe = ? AND DeleteAt = 0";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, ke.getTenKe());
                ps.setString(2, ke.getLoaiKe());
                ps.setString(3, ke.getMaKe());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi sua ke", e);
        }
    }

    /* Duy - Them ke */
    @Override
    public boolean themKe(Ke ke) {
        String sql = "INSERT INTO KeThuoc (MaKe, TenKe, LoaiKe, DeleteAt) VALUES (?, ?, ?, 0)";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, ke.getMaKe());
                ps.setString(2, ke.getTenKe());
                ps.setString(3, ke.getLoaiKe());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them ke", e);
        }
    }

    /* Duy - Tao ma ke tu dong */
    @Override
    public String taoMaKeTuDong() {
        String sql = "SELECT MaKe FROM KeThuoc ORDER BY MaKe DESC LIMIT 1";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String maKeCuoi = rs.getString("MaKe");
                    int soThuTu = Integer.parseInt(maKeCuoi.substring(2)) + 1;
                    return String.format("KE%04d", soThuTu);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi tao ma ke tu dong", e);
        }
        return "KE0001";
    }

    /* Duy - Lay tat ca ke */
    @Override
    public ArrayList<Ke> getTatCaKeThuoc() {
        ArrayList<Ke> listKe = new ArrayList<>();
        String sql = "SELECT * FROM KeThuoc ORDER BY MaKe DESC";
        try {
            Connection con = ensureConnection();
            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
                while (rs.next()) {
                    listKe.add(mapResultSetToEntity(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay tat ca ke", e);
        }
        return listKe;
    }

    /**
     * Lấy tất cả kệ chưa bị xóa
     * @return Danh sách kệ
     */
    @Override
    public ArrayList<Ke> getTatCaKeHoatDong() {
        ArrayList<Ke> listKe = new ArrayList<>();
        String sql = "SELECT * FROM KeThuoc WHERE DeleteAt = 0";
        try {
            Connection con = ensureConnection();
            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
                while (rs.next()) {
                    listKe.add(mapResultSetToEntity(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay danh sach ke hoat dong", e);
        }
        return listKe;
    }

    /**
     * Lấy kệ theo mã
     * @param ma Mã kệ
     * @return Kệ
     */
    @Override
    public Ke getKeTheoMa(String ma) {
        String sql = "SELECT * FROM KeThuoc WHERE MaKe = ? AND DeleteAt = 0";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setString(1, ma);
                try (ResultSet rs = state.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay ke theo ma", e);
        }
        return null;
    }

    /**
     * Lấy kệ theo tên
     * @param name Tên kệ
     * @return Kệ
     */
    @Override
    public Ke getKeTheoName(String name) {
        String sql = "SELECT * FROM KeThuoc WHERE TenKe = ? AND DeleteAt = 0";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setString(1, name);
                try (ResultSet rs = state.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay ke theo ten", e);
        }
        return null;
    }

    @Override
    public boolean isTenKeTrung(String tenKe, String maKeHienTai) {
        String sql = "SELECT COUNT(*) FROM KeThuoc WHERE TenKe = ? AND MaKe <> ?";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tenKe);
                ps.setString(2, maKeHienTai);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi kiem tra trung ten ke", e);
        }
        return false;
    }

    private Connection ensureConnection() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        return con;
    }

    private Ke mapResultSetToEntity(ResultSet rs) throws SQLException {
        String maKe = rs.getString("MaKe");
        String tenKe = rs.getString("TenKe");
        String loaiKe = rs.getString("LoaiKe");
        boolean deleteAt = rs.getBoolean("DeleteAt");
        return new Ke(maKe, tenKe, loaiKe, deleteAt);
    }

}
