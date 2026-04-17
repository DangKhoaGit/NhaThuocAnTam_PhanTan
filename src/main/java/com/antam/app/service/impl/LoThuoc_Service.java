/*
 * @ (#) ChiTietThuoc_DAO.java   1.0 10/6/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_LoThuoc_Service;
import com.antam.app.dto.LoThuocDTO;
import com.antam.app.dto.ThuocDTO;

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
public class LoThuoc_Service implements I_LoThuoc_Service {

    /** Duy - Thêm chi tiết thuốc */
    @Override
    public boolean themChiTietThuoc(LoThuocDTO ctt){
        String sql = "INSERT INTO ChiTietThuoc (MaPN, MaThuoc, HanSuDung, NgaySanXuat, TonKho) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setString(1, ctt.get().getMaPhieuNhap());
            ps.setString(2, ctt.getMaThuocDTO().getMaThuoc());
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
    public ArrayList<LoThuocDTO> getAllChiTietThuoc() {
        ArrayList<LoThuocDTO> listLoThuoc = new ArrayList<>();
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

                LoThuocDTO loThuocDTO = new LoThuocDTO(
//                        maCTT,
//                        new PhieuNhap(maPN),
//                        new Thuoc(maThuoc),
//                        soLuong,
//                        hanSuDung.toLocalDate(),
//                        ngaySanXuat.toLocalDate()
                );
                listLoThuoc.add(loThuocDTO);
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
    public ArrayList<LoThuocDTO> getAllCHiTietThuocTheoMaThuoc(String ma) {
        ArrayList<LoThuocDTO> listLoThuoc = new ArrayList<>();
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

                LoThuocDTO loThuocDTO = new LoThuocDTO(
//                        maCTT,
//                        new PhieuNhap(maPN),
//                        new Thuoc(maThuoc),
//                        soLuong,
//                        hanSuDung.toLocalDate(),
//                        ngaySanXuat.toLocalDate()
                );
                listLoThuoc.add(loThuocDTO);
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
    public LoThuocDTO getChiTietThuoc(int ma) {
        LoThuocDTO loThuocDTO = new LoThuocDTO();
        String sql = "SELECT * FROM ChiTietThuoc WHERE MaCTT = ?";
        Thuoc_Service thuocDAO = new Thuoc_Service();

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
                    ThuocDTO thuocDTO = thuocDAO.getThuocTheoMa(maThuoc);
                    if (thuocDTO == null) {
                        // Fallback nếu không tìm thấy thuốc
                        thuocDTO = new ThuocDTO(maThuoc);
                    }

                    loThuocDTO = new LoThuocDTO(
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
        return loThuocDTO;
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
    public ArrayList<LoThuocDTO> getChiTietThuocHanSuDungGiamDan(String maThuoc) {
        ArrayList<LoThuocDTO> listLoThuoc = new ArrayList<>();
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

                LoThuocDTO loThuocDTO = new LoThuocDTO(
//                        maCTT,
//                        new PhieuNhap(maPN),
//                        new Thuoc(maThuocDB),
//                        soLuong,
//                        hanSuDung.toLocalDate(),
//                        ngaySanXuat.toLocalDate()
                );
                listLoThuoc.add(loThuocDTO);
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
    public ArrayList<LoThuocDTO> getAllChiTietThuocVoiMaChoCTPD(String maThuoc) {

        ArrayList<LoThuocDTO> ds = new ArrayList<>();

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

            ThuocDTO thuocDTO = new Thuoc_Service().getThuocTheoMa(maThuoc);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maThuoc);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {

                        LoThuocDTO ctt = new LoThuocDTO(
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
