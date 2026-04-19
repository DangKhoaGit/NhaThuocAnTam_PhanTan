package com.antam.app.dao;

import com.antam.app.entity.ChiTietHoaDon;

import java.util.ArrayList;
import java.util.List;

/*
 * @ (#) I_ChiTietHoaDon_DAO.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

/*
 * @description: Data Access Object Interface cho ChiTietHoaDon
 * @author: Duong Nguyen, Pham Dang Khoa, Tran Tuan Hung
 * @date: 19/04/2026
 * @version: 1.0
 */
public interface I_ChiTietHoaDon_DAO {
    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    List<ChiTietHoaDon> getChiTietByMaHD(String maHD);

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn (phương thức cũ)
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHD(String maHD);

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn với trạng thái "Bán"
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn có trạng thái "Bán"
     */
    ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHDConBan(String maHD);

    /**
     * Xóa mềm chi tiết hóa đơn (update trạng thái)
     * @param maHD mã hóa đơn
     * @param maCTT mã chi tiết thuốc (ID chi tiết hóa đơn)
     * @param tinhTrang trạng thái mới
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean xoaMemChiTietHoaDon(String maHD, int maCTT, String tinhTrang);

    /**
     * Thêm chi tiết hóa đơn vào database
     * @param cthd chi tiết hóa đơn cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    boolean themChiTietHoaDon(ChiTietHoaDon cthd);

    /**
     * Thêm chi tiết hóa đơn với trạng thái "Trả Khi Đổi"
     * Giới hạn tối đa 2 dòng với trạng thái này cho mỗi MaHD và MaCTT
     * @param cthd chi tiết hóa đơn cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    boolean themChiTietHoaDonTraKhiDoi(ChiTietHoaDon cthd);

    /**
     * Cập nhật chi tiết hóa đơn
     * @param cthd chi tiết hóa đơn cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateChiTietHoaDon(ChiTietHoaDon cthd);

    /**
     * Xóa chi tiết hóa đơn theo mã hóa đơn
     * @param maHD mã hóa đơn
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteChiTietHoaDonByMaHD(String maHD);
}
