/*
 * @ (#) LoThuoc_DAO.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_LoThuoc_DAO;
import com.antam.app.entity.ChiTietPhieuNhap;
import com.antam.app.entity.LoThuoc;
import com.antam.app.entity.PhieuNhap;
import com.antam.app.entity.Thuoc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/*
 * @description: Implementation của I_LoThuoc_DAO
 * Theo chuẩn luồng dữ liệu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Đọc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa
 * @date: 19/04/2026
 * @version: 1.0
 */
public class LoThuoc_DAO implements I_LoThuoc_DAO {

    private final Thuoc_DAO thuocDAO = new Thuoc_DAO();

    private static final String BASE_SELECT = """
        SELECT lt.MaLoThuoc, lt.MaThuoc, lt.HanSuDung, lt.NgaySanXuat, lt.TonKho,
               ctpn.MaPN
        FROM LoThuoc lt
        LEFT JOIN (
            SELECT MaLoThuoc, MIN(MaPN) AS MaPN
            FROM ChiTietPhieuNhap
            GROUP BY MaLoThuoc
        ) ctpn ON ctpn.MaLoThuoc = lt.MaLoThuoc
    """;

    @Override
    public boolean themChiTietThuoc(LoThuoc ctt) {
        String sql = "INSERT INTO LoThuoc (MaThuoc, HanSuDung, NgaySanXuat, TonKho) VALUES (?, ?, ?, ?)";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, ctt.getMaThuoc().getMaThuoc());
                ps.setDate(2, java.sql.Date.valueOf(ctt.getHanSuDung()));
                ps.setDate(3, java.sql.Date.valueOf(ctt.getNgaySanXuat()));
                ps.setInt(4, ctt.getSoLuong());
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them lo thuoc", e);
        }
    }

    @Override
    public ArrayList<LoThuoc> getAllChiTietThuoc() {
        ArrayList<LoThuoc> list = new ArrayList<>();
        String sql = BASE_SELECT + " ORDER BY lt.MaLoThuoc";

        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEntity(rs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay tat ca lo thuoc", e);
        }
        return list;
    }

    @Override
    public ArrayList<LoThuoc> getAllCHiTietThuocTheoMaThuoc(String ma) {
        ArrayList<LoThuoc> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE lt.MaThuoc = ? ORDER BY lt.MaLoThuoc";

        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, ma);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToEntity(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay lo thuoc theo ma thuoc", e);
        }
        return list;
    }

    @Override
    public LoThuoc getChiTietThuoc(int ma) {
        String sql = BASE_SELECT + " WHERE lt.MaLoThuoc = ?";

        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, ma);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay lo thuoc theo ma", e);
        }
        return null;
    }

    @Override
    public boolean CapNhatSoLuongChiTietThuoc(int maCTT, int soLuong) {
        String sql = "UPDATE LoThuoc SET TonKho = TonKho + ? WHERE MaLoThuoc = ?";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, soLuong);
                ps.setInt(2, maCTT);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat ton kho lo thuoc", e);
        }
    }

    @Override
    public ArrayList<LoThuoc> getChiTietThuocHanSuDungGiamDan(String maThuoc) {
        ArrayList<LoThuoc> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE lt.TonKho > 0 AND lt.MaThuoc = ? ORDER BY lt.HanSuDung ASC";

        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maThuoc);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToEntity(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay lo thuoc theo han su dung", e);
        }
        return list;
    }

    @Override
    public int getTongTonKhoTheoMaThuoc(String maThuoc) {
        String sql = "SELECT COALESCE(SUM(TonKho), 0) AS TongTonKho FROM LoThuoc WHERE MaThuoc = ?";
        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maThuoc);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("TongTonKho");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi tinh tong ton kho theo ma thuoc", e);
        }
        return 0;
    }

    @Override
    public ArrayList<LoThuoc> getAllChiTietThuocVoiMaChoCTPD(String maThuoc) {
        ArrayList<LoThuoc> list = new ArrayList<>();
        String sql = BASE_SELECT + " WHERE lt.MaThuoc = ? AND lt.TonKho > 0 AND lt.HanSuDung > CURDATE() ORDER BY lt.HanSuDung ASC";

        try {
            Connection con = ensureConnection();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, maThuoc);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToEntity(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay lo thuoc cho CTPDT", e);
        }
        return list;
    }

    private Connection ensureConnection() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        return con;
    }

    private LoThuoc mapResultSetToEntity(ResultSet rs) throws SQLException {
        int maLoThuoc = rs.getInt("MaLoThuoc");
        String maThuoc = rs.getString("MaThuoc");
        Date hanSuDungSql = rs.getDate("HanSuDung");
        Date ngaySanXuatSql = rs.getDate("NgaySanXuat");
        LocalDate hanSuDung = hanSuDungSql == null ? null : hanSuDungSql.toLocalDate();
        LocalDate ngaySanXuat = ngaySanXuatSql == null ? null : ngaySanXuatSql.toLocalDate();
        int tonKho = rs.getInt("TonKho");
        String maPN = rs.getString("MaPN");

        Thuoc thuoc = thuocDAO.getThuocTheoMa(maThuoc);
        if (thuoc == null) {
            thuoc = new Thuoc(maThuoc);
        }

        LoThuoc loThuoc = new LoThuoc(0, new PhieuNhap(maPN == null ? "" : maPN), thuoc, tonKho, hanSuDung, ngaySanXuat, maLoThuoc);

        if (maPN != null && !maPN.isBlank()) {
            ChiTietPhieuNhap chiTietPhieuNhap = new ChiTietPhieuNhap();
            chiTietPhieuNhap.setMaPN(new PhieuNhap(maPN));
            chiTietPhieuNhap.setLoThuoc(loThuoc);
            loThuoc.setMaCTPN(new ArrayList<>(Collections.singletonList(chiTietPhieuNhap)));
        }

        return loThuoc;
    }
}
