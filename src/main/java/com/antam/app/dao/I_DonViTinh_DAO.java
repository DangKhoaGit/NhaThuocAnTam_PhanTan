package com.antam.app.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.DonViTinh;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_DonViTinh_DAO {
    static String getHashDVT() {
        String sql = "SELECT MaDVT FROM DonViTinh ORDER BY MaDVT DESC LIMIT 1";
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("MaDVT");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    DonViTinh getDVTTheoMaDVT(int ma);

    ArrayList<DonViTinh> getTatCaDonViTinh();

    ArrayList<DonViTinh> getAllDonViTinh();

    DonViTinh getDVTTheoTen(String ten);

    DonViTinh getDVTTheoMa(int ma);

    int themDonViTinh(DonViTinh donViTinh);

    int updateDonViTinh(DonViTinh donViTinh);

    int xoaDonViTinh(DonViTinh donViTinh);

    int khoiPhucDonViTinh(DonViTinh dvt);
}
