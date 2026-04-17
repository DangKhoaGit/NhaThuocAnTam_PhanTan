/*
 * @ (#) KhachHang_DAO.java   1.0 1/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_KhachHang_Service;
import com.antam.app.dto.KhachHangDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * @description
 * @author: Tran Tuan Hung
 * @date: 1/10/25
 * @version: 1.0
 */
public class KhachHang_Service implements I_KhachHang_Service {

    /**
     * Thêm khách hàng mới vào CSDL
     * @param kh KhachHang
     * @return true nếu thành công, false nếu thất bại
     */
    @Override
    public boolean insertKhachHang(KhachHangDTO kh) {
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "INSERT INTO KhachHang (MaKH, TenKH, SoDienThoai, DeleteAt) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSoDienThoai());
            ps.setBoolean(4, kh.isDeleteAt());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin khách hàng trong CSDL
     * @param kh KhachHang
     * @return true nếu thành công, false nếu thất bại
     */
    @Override
    public boolean updateKhachHang(KhachHangDTO kh) {
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "UPDATE KhachHang SET TenKH = ?, SoDienThoai = ? WHERE MaKH = ? AND DeleteAt = 0";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSoDienThoai());
            ps.setString(3, kh.getMaKH());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public KhachHangDTO getKhachHangTheoMa(String maKH){
        KhachHangDTO kh = new KhachHangDTO(maKH);
        String sql = "SELECT * FROM KhachHang WHERE MaKH = ? AND DeleteAt = 0";
        try{
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maKH);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDeleteAt(rs.getBoolean("DeleteAt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    /**
     * Tìm khách hàng theo số điện thoại
     * @param soDienThoai Số điện thoại khách hàng
     * @return KhachHang nếu tìm thấy, null nếu không tìm thấy
     */
    @Override
    public KhachHangDTO getKhachHangTheoSoDienThoai(String soDienThoai) {
        String sql = "SELECT * FROM KhachHang WHERE SoDienThoai = ? AND DeleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, soDienThoai);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                KhachHangDTO kh = new KhachHangDTO(rs.getString("MaKH"));
                kh.setTenKH(rs.getString("TenKH"));
                kh.setSoDienThoai(rs.getString("SoDienThoai"));
                kh.setDeleteAt(rs.getBoolean("DeleteAt"));
                return kh;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Trả về danh sách tất cả khách hàng trong CSDL
     */
    @Override
    public ArrayList<KhachHangDTO> getAllKhachHang() {
        return I_KhachHang_Service.loadBanFromDB();
    }

    @Override
    public int getMaxHash() {
        int hash = 0;
        Connection connection = ConnectDB.getConnection();
        String sql = "select top 1 MaKH from KhachHang order by MaKH desc";
        try {
            PreparedStatement state = connection.prepareStatement(sql);
            ResultSet result = state.executeQuery();
            if (result.next()){
                hash = Integer.parseInt(result.getString(1).replace("KH",""));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hash;
    }
}
