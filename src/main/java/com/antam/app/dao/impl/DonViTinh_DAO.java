/*
 * @ (#) DonViTinh_DAO.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_DonViTinh_DAO;
import com.antam.app.entity.DonViTinh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * @description: Implementation cua I_DonViTinh_DAO
 * Theo chuan luong du lieu: Ghi UI->DTO->Service->Entity->DAO->DB
 *                         Doc DB->DAO->Entity->Service->DTO->UI
 * @author: Duong Nguyen, Pham Dang Khoa
 * @date: 19/04/2026
 * @version: 1.0
 */
public class DonViTinh_DAO implements I_DonViTinh_DAO {

    @Override
    public String getHashDVT() {
        StringBuilder hashBuilder = new StringBuilder();
        String sql = "SELECT MaDVT, TenDVT, isDelete FROM DonViTinh ORDER BY MaDVT";

        try {
            Connection con = ensureConnection();
            try (Statement statement = con.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    hashBuilder.append(rs.getInt("MaDVT"))
                               .append(rs.getString("TenDVT"))
                               .append(rs.getBoolean("isDelete"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay hash don vi tinh", e);
        }
        return hashBuilder.toString();
    }

    @Override
    public DonViTinh getDVTTheoMaDVT(int ma) {
        String sql = "SELECT MaDVT, TenDVT, isDelete FROM DonViTinh WHERE MaDVT = ?";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setInt(1, ma);
                try (ResultSet rs = state.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay don vi tinh theo ten", e);
        }
        return null;
    }

    @Override
    public ArrayList<DonViTinh> getTatCaDonViTinh() {
        return getAllDonViTinh();
    }

    @Override
    public ArrayList<DonViTinh> getAllDonViTinh() {
        ArrayList<DonViTinh> listDVT = new ArrayList<>();
        String sql = "SELECT MaDVT, TenDVT, isDelete FROM DonViTinh ORDER BY MaDVT";

        try {
            Connection con = ensureConnection();
            try (Statement statement = con.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    listDVT.add(mapResultSetToEntity(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay danh sach don vi tinh", e);
        }
        return listDVT;
    }

    @Override
    public DonViTinh getDVTTheoTen(String ten) {
        String sql = "SELECT MaDVT, TenDVT, isDelete FROM DonViTinh WHERE TenDVT = ?";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setString(1, ten);
                try (ResultSet rs = state.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay don vi tinh theo ten", e);
        }
        return null;
    }

    @Override
    public DonViTinh getDVTTheoMa(int ma) {
        String sql = "SELECT MaDVT, TenDVT, isDelete FROM DonViTinh WHERE MaDVT = ?";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setInt(1, ma);
                try (ResultSet rs = state.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay don vi tinh theo ma", e);
        }
        return null;
    }

    @Override
    public int themDonViTinh(DonViTinh donViTinh) {
        String sql = "INSERT INTO DonViTinh (TenDVT, isDelete) VALUES (?, ?)";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setString(1, donViTinh.getTenDVT());
                state.setBoolean(2, donViTinh.isDelete());
                return state.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them don vi tinh", e);
        }
    }

    @Override
    public int updateDonViTinh(DonViTinh donViTinh) {
        String sql = "UPDATE DonViTinh SET TenDVT = ?, isDelete = ? WHERE MaDVT = ?";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setString(1, donViTinh.getTenDVT());
                state.setBoolean(2, donViTinh.isDelete());
                state.setInt(3, donViTinh.getMaDVT());
                return state.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat don vi tinh", e);
        }
    }

    @Override
    public int xoaDonViTinh(DonViTinh donViTinh) {
        String sql = "UPDATE DonViTinh SET isDelete = 1 WHERE MaDVT = ?";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setInt(1, donViTinh.getMaDVT());
                return state.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa don vi tinh", e);
        }
    }

    @Override
    public int khoiPhucDonViTinh(DonViTinh dvt) {
        String sql = "UPDATE DonViTinh SET isDelete = 0 WHERE MaDVT = ?";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setInt(1, dvt.getMaDVT());
                return state.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi khoi phuc don vi tinh", e);
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

    private DonViTinh mapResultSetToEntity(ResultSet rs) throws SQLException {
        int maDVT = rs.getInt("MaDVT");
        String tenDVT = rs.getString("TenDVT");
        boolean isDelete = rs.getBoolean("isDelete");
        return new DonViTinh(maDVT, tenDVT, isDelete);
    }
}
