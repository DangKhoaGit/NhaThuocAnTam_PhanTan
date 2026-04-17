/*
 * @ (#) Ke_DAO.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_Ke_Service;
import com.antam.app.dto.KeDTO;

import java.sql.*;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class Ke_Service implements I_Ke_Service {
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
    public boolean suaKe(KeDTO keDTO) {
        String sql = "UPDATE KeThuoc SET TenKe = ?, LoaiKe = ? WHERE MaKe = ? AND DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, keDTO.getTenKe());
            ps.setString(2, keDTO.getLoaiKe());
            ps.setString(3, keDTO.getMaKe());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /* Duy - Thêm kệ */
    @Override
    public boolean themKe(KeDTO keDTO) {
        String sql = "INSERT INTO KeThuoc (MaKe, TenKe, LoaiKe, DeleteAt) VALUES (?, ?, ?, 0)";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, keDTO.getMaKe());
            ps.setString(2, keDTO.getTenKe());
            ps.setString(3, keDTO.getLoaiKe());
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
    public ArrayList<KeDTO> getTatCaKeThuoc() {
        ArrayList<KeDTO> listKe = new ArrayList<>();
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
                KeDTO keDTO = new KeDTO();
                listKe.add(keDTO);
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
    public ArrayList<KeDTO> getTatCaKeHoatDong() {
        ArrayList<KeDTO> listKe = new ArrayList<>();
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
                KeDTO keDTO = new KeDTO(maKe, tenKe, loaiKe, deleteAt);
                listKe.add(keDTO);
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
    public KeDTO getKeTheoMa(String ma) {
        KeDTO keDTO = null;
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
                keDTO = new KeDTO(maKe, tenKe, loaiKe, deleteAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return keDTO;
    }
    /**
     * Lấy kệ theo tên
     * @param name Tên kệ
     * @return Kệ
     */
    @Override
    public KeDTO getKeTheoName(String name) {
        KeDTO keDTO = null;
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
                keDTO = new KeDTO(maKe, tenKe, loaiKe, deleteAt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return keDTO;
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
