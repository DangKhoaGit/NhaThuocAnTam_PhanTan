/*
 * @ (#) DangDieuChe_DAO.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_DangDieuChe_Service;
import com.antam.app.dto.DangDieuCheDTO;

import java.sql.*;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class DangDieuChe_Service implements I_DangDieuChe_Service {
    /* Duy - Khôi phục dạng điều chế */
    @Override
    public boolean khoiPhucDangDieuChe(int maDDC) {
        String sql = "UPDATE DangDieuChe SET DeleteAt = 0 WHERE MaDDC = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setInt(1, maDDC);
            int rowsUpdated = state.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Duy - Cap nhat delete at dang dieu che */
    @Override
    public boolean xoaDangDieuChe(int maDDC) {
        String sql = "UPDATE DangDieuChe SET DeleteAt = 1 WHERE MaDDC = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setInt(1, maDDC);
            int rowsUpdated = state.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /* Duy - Sửa dang điều chế */
    @Override
    public boolean suaDangDieuChe(DangDieuCheDTO ddc) {
        String sql = "UPDATE DangDieuChe SET TenDDC = ? WHERE MaDDC = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, ddc.getTenDDC());
            state.setInt(2, ddc.getMaDDC());
            int rowsUpdated = state.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /* Duy - Thêm dạng điều chế */
    @Override
    public boolean themDDC(DangDieuCheDTO ddc) {
        String sql = "INSERT INTO DangDieuChe (TenDDC,DeleteAt) VALUES (?,?)";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, ddc.getTenDDC());
            state.setBoolean(2, ddc.isDeleteAt());
            int rowsInserted = state.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /* Duy - Lấy mã dạng điều chế tự động */
    @Override
    public String taoMaDDCTuDong() {
        String maDDC = "";
        String sql = "SELECT TOP 1 MaDDC FROM DangDieuChe ORDER BY MaDDC DESC";
        try {
            Connection con = ConnectDB.getConnection();
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                int maDDCInt = rs.getInt("MaDDC") + 1;
                maDDC = String.valueOf(maDDCInt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return maDDC;
    }

    /* Duy - Lấy tất cả dạng điều chế */
    @Override
    public ArrayList<DangDieuCheDTO> getTatCaDangDieuChe() {
        ArrayList<DangDieuCheDTO> listDDC = new ArrayList<>();
        String sql = "SELECT * FROM DangDieuChe ORDER BY MaDDC DESC";
        try{
            Connection con = ConnectDB.getConnection();
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                int maDDC = rs.getInt("MaDDC");
                String tenDDC = rs.getString("TenDDC");
                Boolean deleteAt = rs.getBoolean("DeleteAt");
                DangDieuCheDTO ke = new DangDieuCheDTO(maDDC, tenDDC, deleteAt);
                listDDC.add(ke);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listDDC;
    }

    /**
     * Lấy tất cả dạng điều chế từ database
     * @return danh sách dạng điều chế
     */
    @Override
    public ArrayList<DangDieuCheDTO> getDangDieuCheHoatDong() {
        ArrayList<DangDieuCheDTO> listDDC = new ArrayList<>();
        String sql = "SELECT * FROM DangDieuChe WHERE DeleteAt = 0";
        try{
            Connection con = ConnectDB.getConnection();
            Statement state = con.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                int maDDC = rs.getInt("MaDDC");
                String tenDDC = rs.getString("TenDDC");
                DangDieuCheDTO ke = new DangDieuCheDTO(maDDC, tenDDC);
                listDDC.add(ke);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listDDC;
    }
    /**
     * Lấy dạng điều chế theo tên
     * @param name tên dạng điều chế
     * @return dạng điều chế
     */
    @Override
    public DangDieuCheDTO getDDCTheoName(String name) {
        DangDieuCheDTO ddc = null;
        String sql = "SELECT * FROM DangDieuChe WHERE TenDDC =  ?";
        try{
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, name);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                int maDDC = rs.getInt("MaDDC");
                String tenDDC = rs.getString("TenDDC");
                ddc = new DangDieuCheDTO(maDDC, tenDDC);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ddc;
    }
}
