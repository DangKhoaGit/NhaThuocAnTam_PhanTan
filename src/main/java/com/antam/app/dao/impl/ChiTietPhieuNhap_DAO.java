/*
 * @ (#) ChiTietHoaDon_DAO.java   1.0 10/3/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_ChiTietPhieuNhap_DAO;
import com.antam.app.entity.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * @description
 * @author: Thanh Duy
 * @date: 10/3/2025
 * version: 1.0
 */
public class ChiTietPhieuNhap_DAO implements I_ChiTietPhieuNhap_DAO {
    /* Duy - Lấy danh sách chi tiết phiếu nhập theo mã phiếu nhập */
    @Override
    public ArrayList<ChiTietPhieuNhap> getDanhSachChiTietPhieuNhapTheoMaPN(String maPN) {
        ArrayList<ChiTietPhieuNhap> danhSach = new ArrayList<>();
        String sql = "SELECT ctpn.*, dvt.TenDVT, dvt.MaDVT, t.MaThuoc, t.TenThuoc, t.Thue  FROM ChiTietPhieuNhap ctpn JOIN DonViTinh dvt ON ctpn.MaDVT = dvt.MaDVT JOIN LoThuoc l ON ctpn.MaLoThuoc = l.MaLoThuoc JOIN thuoc t ON l.MaThuoc = t.MaThuoc WHERE ctpn.MaPN = ?";
        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPN);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
                ctpn.setMaPN(new PhieuNhap(rs.getString("MaPN")));

                Thuoc thuoc = new Thuoc(rs.getString("MaThuoc"));
                thuoc.setMaThuoc(rs.getString("MaThuoc"));
                thuoc.setTenThuoc(rs.getString("TenThuoc"));
                thuoc.setThue(rs.getFloat("Thue"));

                LoThuoc lo = new LoThuoc();
                lo.setMaLoThuoc(rs.getInt("MaLoThuoc"));
                lo.setMaThuoc(thuoc);

                ctpn.setLoThuoc(lo);

                DonViTinh dvt = new DonViTinh();
                dvt.setMaDVT(rs.getInt("MaDVT"));
                dvt.setTenDVT(rs.getString("TenDVT"));

                ctpn.setMaDVT(dvt);

                ctpn.setSoLuong(rs.getInt("SoLuong"));
                ctpn.setGiaNhap(rs.getDouble("GiaNhap"));
                danhSach.add(ctpn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }


    /* Duy- Thêm chi tiết phiếu nhập */
    @Override
    public boolean themChiTietPhieuNhap(ChiTietPhieuNhap ctpn) {
        String sql = "INSERT INTO ChiTietPhieuNhap (MaPN, MaLoThuoc, MaDVT, SoLuong, GiaNhap, ThanhTien) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, ctpn.getMaPN().getMaPhieuNhap());

            ps.setInt(2, ctpn.getLoThuoc().getMaLoThuoc());

            ps.setInt(3, ctpn.getMaDVT().getMaDVT());
            ps.setInt(4, ctpn.getSoLuong());
            ps.setDouble(5, ctpn.getGiaNhap());
            ps.setDouble(6, ctpn.thanhTien());

            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}