package com.antam.app.service.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 01/10/2025
 * @version: 1.0
 */

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_NhanVien_Service;
import com.antam.app.dto.NhanVienDTO;

import java.sql.*;
import java.util.ArrayList;

public class NhanVien_Service implements I_NhanVien_Service {

    //danh sách nhân viên truy xuất trực tiếp khi vào tầng Application.
    public static ArrayList<NhanVienDTO> dsNhanViens = I_NhanVien_Service.getDsNhanVienformDBS();

    @Override
    public NhanVienDTO findNhanVienVoiMa(String maVN){
        return dsNhanViens.stream().
                filter(t-> t.getMaNV().equalsIgnoreCase(maVN))
                .findFirst()
                .orElse(null);
    }

    @Override
    public NhanVienDTO getNhanVien(){
        NhanVienDTO nv = null;
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
                    nv = new NhanVienDTO(maNV,hoTen,soDT,email,diaChi,luongCb
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
    public NhanVienDTO getNhanVienTaiKhoan(String id) {
        NhanVienDTO nhanVienDTO = null;
        String sql = "SELECT * FROM NhanVien WHERE TaiKhoan = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, id);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                String maNV = rs.getString("MaNV");
                nhanVienDTO = new NhanVienDTO(maNV);
                nhanVienDTO.setHoTen(rs.getString("HoTen"));
                nhanVienDTO.setSoDienThoai(rs.getString("SoDienThoai"));
                nhanVienDTO.setEmail(rs.getString("Email"));
                nhanVienDTO.setDiaChi(rs.getString("DiaChi"));
                nhanVienDTO.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nhanVienDTO.setTaiKhoan(rs.getString("TaiKhoan"));
                nhanVienDTO.setMatKhau(rs.getString("MatKhau"));
                nhanVienDTO.setQuanLy(rs.getBoolean("IsQuanLi"));
                nhanVienDTO.setDeleteAt(rs.getBoolean("DeleteAt"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhanVienDTO;
    }
    // duong
    // hung
    @Override
    public NhanVienDTO getNhanVien(String id) {
        NhanVienDTO nhanVienDTO = null;
        String sql = "SELECT * FROM NhanVien WHERE TaiKhoan = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, id);
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                String maNV = rs.getString("MaNV");
                nhanVienDTO = new NhanVienDTO(maNV);
                nhanVienDTO.setHoTen(rs.getString("HoTen"));
                nhanVienDTO.setSoDienThoai(rs.getString("SoDienThoai"));
                nhanVienDTO.setEmail(rs.getString("Email"));
                nhanVienDTO.setDiaChi(rs.getString("DiaChi"));
                nhanVienDTO.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nhanVienDTO.setTaiKhoan(rs.getString("TaiKhoan"));
                nhanVienDTO.setMatKhau(rs.getString("MatKhau"));
                nhanVienDTO.setQuanLy(rs.getBoolean("IsQuanLi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nhanVienDTO;
    }

    @Override
    public ArrayList<NhanVienDTO> getAllNhanVien() {
        ArrayList<NhanVienDTO> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try {
            Connection con = ConnectDB.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String maNV = rs.getString("MaNV");
                NhanVienDTO nv = new NhanVienDTO(maNV);
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
