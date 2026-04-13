/*
 * @ (#) ChiTietHoaDon_DAO.java   1.0 10/3/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.dao.impl;

import com.antam.app.dao.I_ChiTietHoaDon_DAO;
import com.antam.app.entity.*;

import java.util.ArrayList;
import java.util.List;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/3/2025
 * version: 1.0
 */
public class ChiTietHoaDon_DAO implements I_ChiTietHoaDon_DAO {
    @Override
    public List<ChiTietHoaDon> getChiTietByMaHD(String maHD) {
        return List.of();
    }

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn (phương thức cũ)
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    @Override
    public ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHD(String maHD) {
        return null;
    }

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn còn trạng thái bán
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    @Override
    public ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHDConBan(String maHD){
        return null;
    }

    /**
     * Xóa mềm chi tiết hóa đơn
     * @param maHD mã hóa đơn
     * @param maCTT mã chi tiết thuốc
     * @param tinhTrang trạng thái mới
     * @return true nếu xóa thành công, false nếu xóa thất bại
     */
    @Override
    public boolean xoaMemChiTietHoaDon(String maHD, int maCTT, String tinhTrang){
        return false;
    }
    /**
     * Thêm chi tiết hóa đơn
     * @param cthd chi tiết hóa đơn
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */
    @Override
    public boolean themChiTietHoaDon(ChiTietHoaDon cthd){
        return false;
    }

    /**
     * Thêm chi tiết hóa đơn với trạng thái "Trả Khi Đổi"
     * Giới hạn chỉ được có tối đa 2 dòng với trạng thái này cho mỗi MaHD và MaCTT
     * @param cthd chi tiết hóa đơn
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */

    @Override
    public boolean themChiTietHoaDon1(ChiTietHoaDon cthd) {
        return false;
    }


}