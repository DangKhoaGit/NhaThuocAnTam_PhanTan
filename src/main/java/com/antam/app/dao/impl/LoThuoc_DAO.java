/*
 * @ (#) ChiTietThuoc_DAO.java   1.0 10/6/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_LoThuoc_DAO;
import com.antam.app.entity.LoThuoc;
import com.antam.app.entity.PhieuNhap;
import com.antam.app.entity.Thuoc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/6/2025
 * version: 1.0
 */
public class LoThuoc_DAO implements I_LoThuoc_DAO {

    /** Duy - Thêm chi tiết thuốc */
    @Override
    public boolean themChiTietThuoc(LoThuoc ctt){
        String sql = "INSERT INTO ChiTietThuoc (MaPN, MaThuoc, HanSuDung, NgaySanXuat, TonKho) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setString(1, ctt.get().getMaPhieuNhap());
            ps.setString(2, ctt.getMaThuoc().getMaThuoc());
            ps.setString(3, ctt.getHanSuDung().toString());
            ps.setString(4, ctt.getNgaySanXuat().toString());
            ps.setInt(5, ctt.getSoLuong());
            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả chi tiết thuốc từ database
     * @return danh sách chi tiết thuốc
     */
    @Override
    public ArrayList<LoThuoc> getAllChiTietThuoc() {
        ArrayList<LoThuoc> listLoThuoc = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietThuoc";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int maCTT = rs.getInt("MaCTT");
                String maPN = rs.getString("MaPN");
                String maThuoc = rs.getString("MaThuoc");
                int soLuong = rs.getInt("TonKho");
                java.sql.Date hanSuDung = rs.getDate("HanSuDung");
                java.sql.Date ngaySanXuat = rs.getDate("NgaySanXuat");

                LoThuoc loThuoc = new LoThuoc(
//                        maCTT,
//                        new PhieuNhap(maPN),
//                        new Thuoc(maThuoc),
//                        soLuong,
//                        hanSuDung.toLocalDate(),
//                        ngaySanXuat.toLocalDate()
                );
                listLoThuoc.add(loThuoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLoThuoc;
    }

    /**
     * Lấy tất cả chi tiết thuốc theo mã thuốc từ database
     * @param ma mã thuốc
     * @return danh sách chi tiết thuốc
     */
    @Override
    public ArrayList<LoThuoc> getAllCHiTietThuocTheoMaThuoc(String ma) {
        ArrayList<LoThuoc> listLoThuoc = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietThuoc WHERE MaThuoc = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ma);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int maCTT = rs.getInt("MaCTT");
                String maPN = rs.getString("MaPN");
                String maThuoc = rs.getString("MaThuoc");
                int soLuong = rs.getInt("TonKho");
                java.sql.Date hanSuDung = rs.getDate("HanSuDung");
                java.sql.Date ngaySanXuat = rs.getDate("NgaySanXuat");

                LoThuoc loThuoc = new LoThuoc(
//                        maCTT,
//                        new PhieuNhap(maPN),
//                        new Thuoc(maThuoc),
//                        soLuong,
//                        hanSuDung.toLocalDate(),
//                        ngaySanXuat.toLocalDate()
                );
                listLoThuoc.add(loThuoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLoThuoc;
    }

    /**
     * Lấy chi tiết thuốc theo mã từ database
     * @param ma mã chi tiết thuốc
     * @return chi tiết thuốc
     */
    @Override
    public LoThuoc getChiTietThuoc(int ma) {
        LoThuoc loThuoc = new LoThuoc();
        String sql = "SELECT * FROM ChiTietThuoc WHERE MaCTT = ?";
        Thuoc_DAO thuocDAO = new Thuoc_DAO();

        // Tạo connection riêng cho method này
        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ma);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int maCTT = rs.getInt("MaCTT");
                    String maPN = rs.getString("MaPN");
                    String maThuoc = rs.getString("MaThuoc");
                    int soLuong = rs.getInt("TonKho");
                    java.sql.Date hanSuDung = rs.getDate("HanSuDung");
                    java.sql.Date ngaySanXuat = rs.getDate("NgaySanXuat");

                    // Lấy đầy đủ thông tin thuốc từ Thuoc_DAO
                    Thuoc thuoc = thuocDAO.getThuocTheoMa(maThuoc);
                    if (thuoc == null) {
                        // Fallback nếu không tìm thấy thuốc
                        thuoc = new Thuoc(maThuoc);
                    }

                    loThuoc = new LoThuoc(
//                            maCTT,
//                            new PhieuNhap(maPN),
//                            thuoc,
//                            soLuong,
//                            hanSuDung.toLocalDate(),
//                            ngaySanXuat.toLocalDate()
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy chi tiết thuốc: " + e.getMessage());
            e.printStackTrace();
        }
        return loThuoc;
    }

    /**
     * Cập nhật số lượng chi tiết thuốc trong database
     * @param maCTT mã chi tiết thuốc
     * @param soLuong số lượng cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    @Override
    public boolean CapNhatSoLuongChiTietThuoc(int maCTT, int soLuong) {
        String sql = "UPDATE ChiTietThuoc SET TonKho = TonKho + ? WHERE MaCTT = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, soLuong);
            statement.setInt(2, maCTT);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<LoThuoc> getChiTietThuocHanSuDungGiamDan(String maThuoc) {
        ArrayList<LoThuoc> listLoThuoc = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietThuoc WHERE TonKho > 0 AND MaThuoc = ? ORDER BY HanSuDung ASC";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maThuoc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int maCTT = rs.getInt("MaCTT");
                String maPN = rs.getString("MaPN");
                String maThuocDB = rs.getString("MaThuoc");
                int soLuong = rs.getInt("TonKho");
                java.sql.Date hanSuDung = rs.getDate("HanSuDung");
                java.sql.Date ngaySanXuat = rs.getDate("NgaySanXuat");

                LoThuoc loThuoc = new LoThuoc(
//                        maCTT,
//                        new PhieuNhap(maPN),
//                        new Thuoc(maThuocDB),
//                        soLuong,
//                        hanSuDung.toLocalDate(),
//                        ngaySanXuat.toLocalDate()
                );
                listLoThuoc.add(loThuoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLoThuoc;
    }

    /**
     * Lấy tổng số lượng tồn kho (TonKho) của một mã thuốc
     * @param maThuoc mã thuốc
     * @return tổng số lượng tồn kho
     */
    @Override
    public int getTongTonKhoTheoMaThuoc(String maThuoc) {
        int tong = 0;
        String sql = "SELECT SUM(TonKho) AS TongTonKho FROM ChiTietThuoc WHERE MaThuoc = ?";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maThuoc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tong = rs.getInt("TongTonKho");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;
    }

    @Override
    public ArrayList<LoThuoc> getAllChiTietThuocVoiMaChoCTPD(String maThuoc) {

        ArrayList<LoThuoc> ds = new ArrayList<>();

        String sql = """
        SELECT *
        FROM ChiTietThuoc
        WHERE MaThuoc = ?
          AND TonKho > 0
          AND HanSuDung > GETDATE()
        ORDER BY HanSuDung ASC
    """;

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            Thuoc thuoc = new Thuoc_DAO().getThuocTheoMa(maThuoc);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maThuoc);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {

                        LoThuoc ctt = new LoThuoc(
//                                rs.getInt("MaCTT"),
//                                new PhieuNhap(rs.getString("MaPN")),
//                                thuoc,
//                                rs.getInt("TonKho"),
//                                rs.getDate("HanSuDung").toLocalDate(),
//                                rs.getDate("NgaySanXuat").toLocalDate()
                        );

                        ds.add(ctt);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

}
