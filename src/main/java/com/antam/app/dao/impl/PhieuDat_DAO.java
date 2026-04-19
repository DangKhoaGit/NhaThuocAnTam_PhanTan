package com.antam.app.dao.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 02/10/2025
 * @version: 1.0
 */

import java.util.ArrayList;

import com.antam.app.dao.I_PhieuDat_DAO;
import com.antam.app.entity.KhachHang;
import com.antam.app.entity.PhieuDatThuoc;

public class PhieuDat_DAO implements I_PhieuDat_DAO {
    public static ArrayList<PhieuDatThuoc> list = I_PhieuDat_DAO.getAllPhieuDatThuocFromDBS();

    public static Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private NhanVien_DAO nvDAO = new NhanVien_DAO();
    private KhachHang_DAO khDAO = new KhachHang_DAO();
    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAO();

    /**
     * Helper method để lấy danh sách tất cả khách hàng (không bị xóa)
     * Dùng cho static methods trong I_PhieuDat_DAO
     * @return danh sách KhachHang
     */
    public ArrayList<KhachHang> getAllKhachHangFromDAO() {
        return khDAO.getAllKhachHang();
    }
}
