/*
 * @ (#) Ke_DAO.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.Ke;

import java.sql.*;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class Ke_DAO implements com.antam.app.dao.I_Ke_DAO {
    /* Duy - Xoá kệ */
    @Override
    public boolean xoaKe(String maKe) {
        String sql = "UPDATE KeThuoc SET DeleteAt = 1 WHERE MaKe = ? AND DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maKe);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* Duy - Khôi phục kệ */
    @Override
    public boolean khoiPhucKe(String maKe) {
        String sql = "UPDATE KeThuoc SET DeleteAt = 0 WHERE MaKe = ? AND DeleteAt = 1";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maKe);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* Duy - Sửa kệ */
    @Override
    public boolean suaKe(Ke ke) {
        String sql = "UPDATE KeThuoc SET TenKe = ?, LoaiKe = ? WHERE MaKe = ? AND DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ke.getTenKe());
            ps.setString(2, ke.getLoaiKe());
            ps.setString(3, ke.getMaKe());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /* Duy - Thêm kệ */
    @Override
    public boolean themKe(Ke ke) {
        String sql = "INSERT INTO KeThuoc (MaKe, TenKe, LoaiKe, DeleteAt) VALUES (?, ?, ?, 0)";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ke.getMaKe());
            ps.setString(2, ke.getTenKe());
            ps.setString(3, ke.getLoaiKe());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /* Duy - Tạo mã kệ tự động */
    @Override
    public String taoMaKeTuDong(){
        String sql = "SELECT TOP 1 MaKe FROM KeThuoc ORDER BY MaKe DESC";
        String maKeMoi = "";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String maPhieuNhap = rs.getString("MaKe");
                int soThuTu = Integer.parseInt(maPhieuNhap.substring(2)) + 1;
                maKeMoi = String.format("KE%04d", soThuTu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maKeMoi;
    }

    /* Duy - Lấy tất cả kệ */
    @Override
    public ArrayList<Ke> getTatCaKeThuoc() {
        ArrayList<Ke> listKe = new ArrayList<>();
        String sql = "SELECT * FROM KeThuoc ORDER BY MaKe DESC";
        try{
            Connection con = ConnectDB.getConnection();
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                String maKe = rs.getString("MaKe");
                String tenKe = rs.getString("TenKe");
                String loaiKe = rs.getString("LoaiKe");
                boolean deleteAt = rs.getBoolean("DeleteAt");
                Ke ke = new Ke(maKe, tenKe, loaiKe, deleteAt);
                listKe.add(ke);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        try{
            Connection con = ConnectDB.getConnection();
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                String maKe = rs.getString("MaKe");
                String tenKe = rs.getString("TenKe");
                String loaiKe = rs.getString("LoaiKe");
                boolean deleteAt = rs.getBoolean("DeleteAt");
                Ke ke = new Ke(maKe, tenKe, loaiKe, deleteAt);
                listKe.add(ke);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        Ke ke = null;
        String sql = "SELECT * FROM KeThuoc WHERE MaKe = ? AND DeleteAt = 0";
        try{
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, ma);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                String maKe = rs.getString("MaKe");
                String tenKe = rs.getString("TenKe");
                String loaiKe = rs.getString("LoaiKe");
                boolean deleteAt = rs.getBoolean("DeleteAt");
                ke = new Ke(maKe, tenKe, loaiKe, deleteAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ke;
    }
    /**
     * Lấy kệ theo tên
     * @param name Tên kệ
     * @return Kệ
     */
    @Override
    public Ke getKeTheoName(String name) {
        Ke ke = null;
        String sql = "SELECT * FROM KeThuoc WHERE TenKe = ? AND DeleteAt = 0";
        try{
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, name);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                String maKe = rs.getString("MaKe");
                String tenKe = rs.getString("TenKe");
                String loaiKe = rs.getString("LoaiKe");
                boolean deleteAt = rs.getBoolean("DeleteAt");
                ke = new Ke(maKe, tenKe, loaiKe, deleteAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ke;
    }

    @Override
    public boolean isTenKeTrung(String tenKe, String maKeHienTai) {
        String sql = "SELECT COUNT(*) FROM KeThuoc WHERE tenKe = ? AND maKe <> ?";
        try{
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tenKe);
            ps.setString(2, maKeHienTai);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
