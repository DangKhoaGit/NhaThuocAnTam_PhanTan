/*
 * @ (#) ChiTietHoaDon_DAO.java   1.0 10/3/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_ChiTietPhieuNhap_Service;
import com.antam.app.dto.*;

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
public class ChiTietPhieuNhap_Service implements I_ChiTietPhieuNhap_Service {
    /* Duy - Lấy danh sách chi tiết phiếu nhập theo mã phiếu nhập */
    @Override
    public ArrayList<ChiTietPhieuNhapDTO> getDanhSachChiTietPhieuNhapTheoMaPN(String maPN) {
        ArrayList<ChiTietPhieuNhapDTO> danhSach = new ArrayList<>();
        String sql = "SELECT ctpn.*, dvt.TenDVT\n" +
                "    FROM ChiTietPhieuNhap ctpn\n" +
                "    JOIN DonViTinh dvt ON ctpn.MaDVT = dvt.MaDVT\n" +
                "    WHERE MaPN = ?";
        try (Connection con = ConnectDB.getInstance().connect();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maPN);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietPhieuNhapDTO ctpn = new ChiTietPhieuNhapDTO();
                ctpn.setMaPN(new PhieuNhapDTO(rs.getString("MaPN")));
                ctpn.setLoThuocDTO(new LoThuocDTO());

                DonViTinhDTO dvt = new DonViTinhDTO(rs.getInt("MaDVT"));
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
    public boolean themChiTietPhieuNhap(ChiTietPhieuNhapDTO ctpn){
        String sql = "INSERT INTO ChiTietPhieuNhap (MaPN, MaThuoc, MaDVT, SoLuong, GiaNhap, ThanhTien) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ctpn.getMaPN().getMaPhieuNhap());
            ps.setString(2, ctpn.getLoThuocDTO().getMaThuocDTO().getMaThuoc());
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