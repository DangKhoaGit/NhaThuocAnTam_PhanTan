package com.antam.app.service;

import com.antam.app.dto.ChiTietHoaDonDTO;

import java.util.ArrayList;
import java.util.List;

/*
 * @ (#) I_ChiTietHoaDon_Service.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

/*
 * @description: Business Logic Interface cho ChiTietHoaDon
 * Theo chuẩn luồng dữ liệu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Đọc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa, Tran Tuan Hung
 * @date: 19/04/2026
 * @version: 1.0
 */
public interface I_ChiTietHoaDon_Service {
    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn
     * DB→DAO→Entity→Service→DTO→UI
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    List<ChiTietHoaDonDTO> getChiTietByMaHD(String maHD);

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn (ArrayList version)
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    ArrayList<ChiTietHoaDonDTO> getAllChiTietHoaDonTheoMaHD(String maHD);

    /**
     * Lấy tất cả chi tiết hóa đơn với trạng thái "Bán"
     * @param maHD mã hóa đơn
     * @return danh sách chi tiết hóa đơn có trạng thái "Bán"
     */
    ArrayList<ChiTietHoaDonDTO> getAllChiTietHoaDonTheoMaHDConBan(String maHD);

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
     * UI→DTO→Service→Entity→DAO→DB
     * @param cthd chi tiết hóa đơn DTO cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    boolean themChiTietHoaDon(ChiTietHoaDonDTO cthd);

    /**
     * Thêm chi tiết hóa đơn với trạng thái "Trả Khi Đổi"
     * Giới hạn tối đa 2 dòng với trạng thái này cho mỗi MaHD và MaCTT
     * @param cthd chi tiết hóa đơn DTO cần thêm
     * @return true nếu thêm thành công, false nếu thất bại hoặc đạt giới hạn
     */
    boolean themChiTietHoaDonTraKhiDoi(ChiTietHoaDonDTO cthd);

    /**
     * Cập nhật chi tiết hóa đơn
     * @param cthd chi tiết hóa đơn DTO cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateChiTietHoaDon(ChiTietHoaDonDTO cthd);

    /**
     * Kiểm tra chi tiết hóa đơn đã tồn tại theo khóa chính kép (MaHD, MaLoThuoc)
     * @param maHD mã hóa đơn
     * @param maLoThuoc mã lô thuốc
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean tonTaiChiTietHoaDon(String maHD, int maLoThuoc);

    /**
     * Kiểm tra chi tiết hóa đơn đã tồn tại theo khóa chính kép (MaHD, MaLoThuoc, TinhTrang)
     * @param maHD mã hóa đơn
     * @param maLoThuoc mã lô thuốc
     * @param tinhTrang trạng thái chi tiết hóa đơn
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean tonTaiChiTietHoaDonTheoTinhTrang(String maHD, int maLoThuoc, String tinhTrang);

    /**
     * Thêm hoặc cập nhật chi tiết hóa đơn (upsert theo khóa MaHD + MaLoThuoc + TinhTrang)
     * - Nếu chưa tồn tại: INSERT
     * - Nếu đã tồn tại: UPDATE (cập nhật SoLuong, MaDVT, ThanhTien)
     * @param cthd chi tiết hóa đơn DTO
     * @return true nếu thành công (insert hoặc update), false nếu thất bại
     */
    boolean themHoacCapNhatChiTietHoaDon(ChiTietHoaDonDTO cthd);

    /**
     * Xóa tất cả chi tiết hóa đơn theo mã hóa đơn
     * @param maHD mã hóa đơn
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean deleteChiTietHoaDonByMaHD(String maHD);
}
