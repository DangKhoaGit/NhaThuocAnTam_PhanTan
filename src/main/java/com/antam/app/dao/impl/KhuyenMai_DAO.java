package com.antam.app.dao.impl;

import com.antam.app.dao.I_KhuyenMai_DAO;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.LoaiKhuyenMai;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import com.antam.app.connect.ConnectDB;

public class KhuyenMai_DAO implements I_KhuyenMai_DAO {


    @Override
    public ArrayList<KhuyenMai> getAllKhuyenMaiChuaXoa() {
        return loadKhuyenMai("WHERE km.deleteAt = 0");
    }

    @Override
    public ArrayList<KhuyenMai> getAllKhuyenMaiDaXoa() {
        return loadKhuyenMai("WHERE km.deleteAt = 1");
    }

    @Override
    public boolean khoiPhucKhuyenMai(String maKM) {
        String sql = "UPDATE KhuyenMai SET deleteAt = 0 WHERE MaKM = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKM);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<KhuyenMai> getAllKhuyenMai() {
        return loadKhuyenMai("");
    }

    @Override
    public KhuyenMai getKhuyenMaiTheoMa(String maKM) {
        KhuyenMai km = null;
        String sql = baseSelectSql() + " WHERE km.MaKM = ? AND km.deleteAt = 0";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    km = mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return km;
    }

    @Override
    public boolean themKhuyenMai(String maKM, String tenKM, LoaiKhuyenMai loaiKM, double so, LocalDate ngayBatDau, LocalDate ngayKetThuc, int soLuongToiDa) {
        String sql = "INSERT INTO KhuyenMai (MaKM, TenKM, LoaiKhuyenMai, So, NgayBatDau, NgayKetThuc, SoLuongToiDa, deleteAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 0)";
        try  {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maKM);
            ps.setString(2, tenKM);
            ps.setInt(3, loaiKM.getMaLKM());
            ps.setDouble(4, so);
            ps.setDate(5, Date.valueOf(ngayBatDau));
            ps.setDate(6, Date.valueOf(ngayKetThuc));
            ps.setInt(7, soLuongToiDa);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean capNhatKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET TenKM = ?, LoaiKhuyenMai = ?, So = ?, NgayBatDau = ?, NgayKetThuc = ?, SoLuongToiDa = ? " +
                "WHERE MaKM = ? AND deleteAt = 0";
        try  {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, km.getTenKM());
            ps.setInt(2, km.getLoaiKhuyenMai().getMaLKM());
            ps.setDouble(3, km.getSo());
            ps.setDate(4, Date.valueOf(km.getNgayBatDau()));
            ps.setDate(5, Date.valueOf(km.getNgayKetThuc()));
            ps.setInt(6, km.getSoLuongToiDa());
            ps.setString(7, km.getMaKM());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean xoaKhuyenMai(String maKM) {
        String sql = "UPDATE KhuyenMai SET deleteAt = 1 WHERE MaKM = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKM);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public java.util.List<KhuyenMai> fetchAllKhuyenMaiConHieuLuc() {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        String sql = baseSelectSql() + " WHERE km.deleteAt = 0 AND km.NgayBatDau <= ? AND km.NgayKetThuc >= ?";
        LocalDate today = LocalDate.now();
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(today));
                ps.setDate(2, Date.valueOf(today));
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(mapResultSetToEntity(rs));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<KhuyenMai> loadKhuyenMai(String whereClause) {
        ArrayList<KhuyenMai> list = new ArrayList<>();
        String sql = baseSelectSql() + (whereClause.isBlank() ? "" : " " + whereClause);
        try  {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private String baseSelectSql() {
        return "SELECT km.MaKM, km.TenKM, km.NgayBatDau, km.NgayKetThuc, km.LoaiKhuyenMai, km.So, km.SoLuongToiDa, km.deleteAt, " +
                "COALESCE(lkm.TenLKM, '') AS TenLKM " +
                "FROM KhuyenMai km LEFT JOIN LoaiKhuyenMai lkm ON km.LoaiKhuyenMai = lkm.MaLKM";
    }

    private KhuyenMai mapResultSetToEntity(ResultSet rs) throws SQLException {
        String maKM = rs.getString("MaKM");
        String tenKM = rs.getString("TenKM");
        Date sqlNgayBatDau = rs.getDate("NgayBatDau");
        LocalDate ngayBatDau = sqlNgayBatDau != null ? sqlNgayBatDau.toLocalDate() : LocalDate.now();
        Date sqlNgayKetThuc = rs.getDate("NgayKetThuc");
        LocalDate ngayKetThuc = sqlNgayKetThuc != null ? sqlNgayKetThuc.toLocalDate() : LocalDate.now();
        int maLoaiKM = rs.getInt("LoaiKhuyenMai");
        String tenLoaiKM = rs.getString("TenLKM");
        double so = rs.getDouble("So");
        int soLuongToiDa = rs.getInt("SoLuongToiDa");
        boolean deleteAt = rs.getBoolean("deleteAt");
        LoaiKhuyenMai loai = new LoaiKhuyenMai(maLoaiKM, tenLoaiKM == null ? "" : tenLoaiKM);
        return new KhuyenMai(maKM, tenKM, ngayBatDau, ngayKetThuc, loai, so, soLuongToiDa, deleteAt);
    }
}
