package com.antam.app.dao.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 02/10/2025
 * @version: 1.0
 */

import com.antam.app.dao.I_PhieuDat_DAO;
import com.antam.app.entity.*;

import java.util.ArrayList;

public class PhieuDat_DAO implements I_PhieuDat_DAO {
    public static ArrayList<PhieuDatThuoc> list = I_PhieuDat_DAO.getAllPhieuDatThuocFromDBS();

    public static Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private NhanVien_DAO nvDAO = new NhanVien_DAO();
    private KhachHang_DAO khDAO = new KhachHang_DAO();
    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAO();


}
