package com.antam.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.antam.app.connect.ConnectDB;
import com.antam.app.entity.NhanVien;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_NhanVien_DAO {

    boolean themNhanVien(NhanVien nv);

    boolean updateNhanVienTrongDBS(NhanVien nv);

    boolean xoaNhanVienTrongDBS(String manv);

    String getMaxHashNhanVien();

    boolean khoiPhucNhanVien(String maNV);

    NhanVien findNhanVienVoiMa(String maVN);

    NhanVien getNhanVienTaiKhoan(String id);

    // duong
    // hung
    NhanVien getNhanVien(String id);

    ArrayList<NhanVien> getAllNhanVien();
}
