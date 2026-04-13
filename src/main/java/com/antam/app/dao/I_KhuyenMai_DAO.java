package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.LoaiKhuyenMai;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_KhuyenMai_DAO {
    static List<KhuyenMai> getAllKhuyenMaiConHieuLuc() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT km.MaKM, km.TenKM, km.NgayBatDau, km.NgayKetThuc, km.LoaiKhuyenMai, km.So, km.SoLuongToiDa, km.deleteAt, lkm.TenLKM " +
                "FROM KhuyenMai km JOIN LoaiKhuyenMai lkm ON km.LoaiKhuyenMai = lkm.MaLKM " +
                "WHERE km.deleteAt = 0 AND km.NgayBatDau <= GETDATE() AND km.NgayKetThuc >= GETDATE()";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String maKM = rs.getString("MaKM");
                String tenKM = rs.getString("TenKM");
                java.sql.Date sqlNgayBatDau = rs.getDate("NgayBatDau");
                LocalDate ngayBatDau = sqlNgayBatDau != null ? sqlNgayBatDau.toLocalDate() : LocalDate.now();
                java.sql.Date sqlNgayKetThuc = rs.getDate("NgayKetThuc");
                LocalDate ngayKetThuc = sqlNgayKetThuc != null ? sqlNgayKetThuc.toLocalDate() : LocalDate.now();
                int maLoaiKM = rs.getInt("LoaiKhuyenMai");
                String tenLoaiKM = rs.getString("TenLKM");
                double so = rs.getDouble("So");
                int soLuongToiDa = rs.getInt("SoLuongToiDa");
                boolean deleteAt = rs.getBoolean("deleteAt");
                LoaiKhuyenMai loai = new LoaiKhuyenMai(maLoaiKM, tenLoaiKM);
                KhuyenMai km = new KhuyenMai(maKM, tenKM, ngayBatDau, ngayKetThuc, loai, so, soLuongToiDa, deleteAt);
                list.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    ArrayList<KhuyenMai> getAllKhuyenMaiChuaXoa();

    ArrayList<KhuyenMai> getAllKhuyenMaiDaXoa();

    boolean khoiPhucKhuyenMai(String maKM);

    ArrayList<KhuyenMai> getAllKhuyenMai();

    KhuyenMai getKhuyenMaiTheoMa(String maKM);

    boolean themKhuyenMai(String maKM, String tenKM, LoaiKhuyenMai loaiKM, double so, LocalDate ngayBatDau, LocalDate ngayKetThuc, int soLuongToiDa);

    boolean capNhatKhuyenMai(KhuyenMai km);

    boolean xoaKhuyenMai(String maKM);
}
