package com.antam.app.service.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 02/10/2025
 * @version: 1.0
 */

import java.util.ArrayList;

import com.antam.app.dto.KhachHangDTO;
import com.antam.app.dto.PhieuDatThuocDTO;
import com.antam.app.service.I_PhieuDat_Service;

public class PhieuDat_Service implements I_PhieuDat_Service {
    public static ArrayList<PhieuDatThuocDTO> list = I_PhieuDat_Service.getAllPhieuDatThuocFromDBS();

    public static Thuoc_Service thuoc_dao = new Thuoc_Service();
    private NhanVien_Service nvDAO = new NhanVien_Service();
    private KhachHang_Service khDAO = new KhachHang_Service();
    private KhuyenMai_Service kmDAO = new KhuyenMai_Service();

    /**
     * Helper method để lấy danh sách tất cả khách hàng (không bị xóa)
     * Dùng cho static methods trong I_PhieuDat_Service
     * @return danh sách KhachHangDTO
     */
    public ArrayList<KhachHangDTO> getAllKhachHangFromService() {
        return khDAO.getAllKhachHang();
    }
}
