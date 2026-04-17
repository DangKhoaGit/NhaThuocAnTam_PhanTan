package com.antam.app.dao.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 01/10/2025
 * @version: 1.0
 */

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_NhanVien_DAO;
import com.antam.app.entity.NhanVien;

import java.sql.*;
import java.util.ArrayList;

public class NhanVien_DAO implements I_NhanVien_DAO {

    //danh sách nhân viên truy xuất trực tiếp khi vào tầng Application.
    public static ArrayList<NhanVien> dsNhanViens = I_NhanVien_DAO.getDsNhanVienformDBS();

    @Override
    public NhanVien findNhanVienVoiMa(String maVN){
        return dsNhanViens.stream().
                filter(t-> t.getMaNV().equalsIgnoreCase(maVN))
                .findFirst()
                .orElse(null);
    }

    @Override
    public NhanVien getNhanVien(){
        NhanVien nv = null;
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        String sql = "Select * from NhanVien";
        try {
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet result = state.executeQuery();
            while (result.next()){
                boolean isXoa = result.getBoolean("DeleteAt");
                if (!isXoa){
                    String maNV = result.getNString("MaNV");
                    String hoTen = result.getNString("HoTen");
                    String soDT = result.getNString("SoDienThoai");
                    String email = result.getNString("Email");
                    String diaChi = result.getNString("DiaChi");
                    double luongCb = result.getDouble("LuongCoBan");
                    String taiKhoan = result.getNString("TaiKhoan");
                    String matKhau = result.getNString("MatKhau");
                    boolean deleteAt = result.getBoolean("DeleteAt");
                    boolean isQL = result.getBoolean("IsQuanLi");
                    nv = new NhanVien(maNV,hoTen,soDT,email,diaChi,luongCb
                            ,taiKhoan,matKhau,deleteAt,isQL);

                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nv;
    }

    // duong
    /**
     * Lấy nhân viên theo tài khoản
     * @param id Tài khoản
     * @return Nhân viên
     */
    @Override
    public NhanVien getNhanVienTaiKhoan(String id) {
        NhanVien nhanVien = null;
        String sql = "SELECT * FROM NhanVien WHERE TaiKhoan = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, id);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                String maNV = rs.getString("MaNV");
                nhanVien = new NhanVien(maNV);
                nhanVien.setHoTen(rs.getString("HoTen"));
                nhanVien.setSoDienThoai(rs.getString("SoDienThoai"));
                nhanVien.setEmail(rs.getString("Email"));
                nhanVien.setDiaChi(rs.getString("DiaChi"));
                nhanVien.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nhanVien.setTaiKhoan(rs.getString("TaiKhoan"));
                nhanVien.setMatKhau(rs.getString("MatKhau"));
                nhanVien.setQuanLy(rs.getBoolean("IsQuanLi"));
                nhanVien.setDeleteAt(rs.getBoolean("DeleteAt"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhanVien;
    }
    // duong
    // hung
    @Override
    public NhanVien getNhanVien(String id) {
        NhanVien nhanVien = null;
        String sql = "SELECT * FROM NhanVien WHERE TaiKhoan = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, id);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                String maNV = rs.getString("MaNV");
                nhanVien = new NhanVien(maNV);
                nhanVien.setHoTen(rs.getString("HoTen"));
                nhanVien.setSoDienThoai(rs.getString("SoDienThoai"));
                nhanVien.setEmail(rs.getString("Email"));
                nhanVien.setDiaChi(rs.getString("DiaChi"));
                nhanVien.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nhanVien.setTaiKhoan(rs.getString("TaiKhoan"));
                nhanVien.setMatKhau(rs.getString("MatKhau"));
                nhanVien.setQuanLy(rs.getBoolean("IsQuanLi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhanVien;
    }

    @Override
    public ArrayList<NhanVien> getAllNhanVien() {
        ArrayList<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try {
            Connection con = ConnectDB.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String maNV = rs.getString("MaNV");
                NhanVien nv = new NhanVien(maNV);
                nv.setHoTen(rs.getString("HoTen"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setEmail(rs.getString("Email"));
                nv.setDiaChi(rs.getString("DiaChi"));
                nv.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nv.setTaiKhoan(rs.getString("TaiKhoan"));
                nv.setMatKhau(rs.getString("MatKhau"));
                nv.setQuanLy(rs.getBoolean("IsQuanLi"));
                ds.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

}
