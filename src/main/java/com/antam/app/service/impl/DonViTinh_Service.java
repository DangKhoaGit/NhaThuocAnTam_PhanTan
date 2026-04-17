/*
 * @ (#) DonViTinh_DAO.java   1.0 10/1/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_DonViTinh_Service;
import com.antam.app.dto.DonViTinhDTO;

import java.sql.*;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/1/2025
 * version: 1.0
 */
public class DonViTinh_Service implements I_DonViTinh_Service {
    /**
     * Duy - Lấy đơn vị tính theo mã
     * @param ma mã đơn vị tính
     * @return đơn vị tính
     */
    @Override
    public DonViTinhDTO getDVTTheoMaDVT(int ma) {
        DonViTinhDTO dvt = null;
        String sql = "SELECT * FROM DonViTinh WHERE MaDVT = ?";
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            var state = con.prepareStatement(sql);
            state.setInt(1, ma);
            var rs = state.executeQuery();
            if (rs.next()) {
                int maDVT = rs.getInt("MaDVT");
                String tenDVT = rs.getString("TenDVT");
                boolean isDelete = rs.getBoolean("DeleteAt");
                dvt = new DonViTinhDTO(maDVT, tenDVT,isDelete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dvt;
    }

    /**
     * Duy - Lấy tất cả đơn vị tính từ database
     * @return danh sách đơn vị tính
     */
    @Override
    public ArrayList<DonViTinhDTO> getTatCaDonViTinh() {
        ArrayList<DonViTinhDTO> list = new ArrayList<>();
        try (Connection con = ConnectDB.getInstance().connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM DonViTinh")) {
            while (rs.next()) {
                int maDVT = rs.getInt("MaDVT");
                String tenDVT = rs.getString("TenDVT");
                boolean isDelete = rs.getBoolean("DeleteAt");
                DonViTinhDTO dvt = new DonViTinhDTO(maDVT, tenDVT,isDelete);
                list.add(dvt);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * Lấy tất cả đơn vị tính từ database
     * @return danh sách đơn vị tính
     */
    @Override
    public ArrayList<DonViTinhDTO> getAllDonViTinh() {
        ArrayList<DonViTinhDTO> listDVT = new ArrayList<>();
        String sql = "SELECT * FROM DonViTinh";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            Statement statement = con.createStatement();
            var rs = statement.executeQuery(sql);
            while (rs.next()) {
                int maDVT = rs.getInt("MaDVT");
                String tenDVT = rs.getString("TenDVT");
                boolean isDelete = rs.getBoolean("DeleteAt");
                DonViTinhDTO dvt = new DonViTinhDTO(maDVT, tenDVT,isDelete);
                listDVT.add(dvt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDVT;
    }

    /**
     * Lấy đơn vị tính theo tên
     * @param ten tên đơn vị tính
     * @return đơn vị tính
     */
    @Override
    public DonViTinhDTO getDVTTheoTen(String ten) {
        DonViTinhDTO dvt = null;
        String sql = "SELECT * FROM DonViTinh WHERE TenDVT = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            var state = con.prepareStatement(sql);
            state.setString(1, ten);
            var rs = state.executeQuery();
            if (rs.next()) {
                int maDVT = rs.getInt("MaDVT");
                String tenDVT = rs.getString("TenDVT");
                boolean isDelete = rs.getBoolean("DeleteAt");
                dvt = new DonViTinhDTO(maDVT, tenDVT,isDelete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dvt;
    }
    /**
     * Lấy đơn vị tính theo mã
     * @param ma mã đơn vị tính
     * @return đơn vị tính
     */
    @Override
    public DonViTinhDTO getDVTTheoMa(int ma) {
        DonViTinhDTO dvt = null;
        String sql = "SELECT * FROM DonViTinh WHERE MaDVT = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            var state = con.prepareStatement(sql);
            state.setInt(1, ma);
            var rs = state.executeQuery();
            if (rs.next()) {
                int maDVT = rs.getInt("MaDVT");
                String tenDVT = rs.getString("TenDVT");
                boolean isDelete = rs.getBoolean("DeleteAt");
                dvt = new DonViTinhDTO(maDVT, tenDVT,isDelete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dvt;
    }

    @Override
    public int themDonViTinh(DonViTinhDTO donViTinhDTO) {
        try {
            // Đảm bảo đã kết nối đến DB
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();

            String sql = "INSERT INTO DonViTinh (TenDVT, DeleteAt) VALUES (?, ?)";
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setString(1, donViTinhDTO.getTenDVT());
                state.setBoolean(2, donViTinhDTO.isDelete());
                return state.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateDonViTinh(DonViTinhDTO donViTinhDTO) {
        String sql = "UPDATE DonViTinh SET TenDVT = ?, DeleteAt = ? WHERE MaDVT = ?";

        try {
            // Đảm bảo đã có kết nối tới DB
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();

            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setString(1, donViTinhDTO.getTenDVT());
                state.setBoolean(2, donViTinhDTO.isDelete());
                state.setInt(3, donViTinhDTO.getMaDVT());

                return state.executeUpdate(); // trả về số dòng bị ảnh hưởng (1 nếu thành công)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int xoaDonViTinh(DonViTinhDTO donViTinhDTO) {
        String sql = "UPDATE DonViTinh SET DeleteAt = ? WHERE MaDVT = ?";

        try {
            // Đảm bảo đã có kết nối tới DB
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();

            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setBoolean(1, true);
                state.setInt(2, donViTinhDTO.getMaDVT());

                return state.executeUpdate(); // trả về số dòng bị ảnh hưởng (1 nếu thành công)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int khoiPhucDonViTinh(DonViTinhDTO dvt){
        String sql = "UPDATE DonViTinh SET DeleteAt = ? WHERE MaDVT = ?";

        try {
            // Đảm bảo đã có kết nối tới DB
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();

            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setBoolean(1, false);
                state.setInt(2, dvt.getMaDVT());

                return state.executeUpdate(); // trả về số dòng bị ảnh hưởng (1 nếu thành công)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}


