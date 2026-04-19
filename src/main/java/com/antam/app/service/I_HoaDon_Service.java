package com.antam.app.service;

import com.antam.app.dto.HoaDonDTO;
import java.util.ArrayList;

/*
 * @ (#) I_HoaDon_Service.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

/*
 * @description: Business Logic Interface cho HoaDon
 * Theo chuẩn luồng dữ liệu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Đọc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa, Tran Tuan Hung
 * @date: 19/04/2026
 * @version: 1.0
 */
public interface I_HoaDon_Service {
    /**
     * Lấy tất cả hóa đơn
     * DB→DAO→Entity→Service→DTO→UI
     * @return danh sách tất cả hóa đơn
     */
    ArrayList<HoaDonDTO> getAllHoaDon();

    /**
     * Lấy hóa đơn theo mã hóa đơn
     * @param maHD mã hóa đơn
     * @return hóa đơn nếu tìm thấy, null nếu không
     */
    HoaDonDTO getHoaDonTheoMa(String maHD);

    /**
     * Lấy danh sách hóa đơn theo mã khách hàng
     * @param maKH mã khách hàng
     * @return danh sách hóa đơn của khách hàng đó
     */
    ArrayList<HoaDonDTO> getHoaDonByMaKH(String maKH);

    /**
     * Tìm kiếm hóa đơn theo mã hóa đơn (LIKE)
     * @param maHd phần mã hóa đơn cần tìm
     * @return danh sách hóa đơn phù hợp
     */
    ArrayList<HoaDonDTO> searchHoaDonByMaHd(String maHd);

    /**
     * Tìm kiếm hóa đơn theo trạng thái
     * @param status trạng thái cần tìm ("Tất cả", "Hoạt động", "Đã huỷ")
     * @return danh sách hóa đơn phù hợp
     */
    ArrayList<HoaDonDTO> searchHoaDonByStatus(String status);

    /**
     * Tìm kiếm hóa đơn theo mã nhân viên
     * @param maNV mã nhân viên
     * @return danh sách hóa đơn của nhân viên đó
     */
    ArrayList<HoaDonDTO> searchHoaDonByMaNV(String maNV);

    /**
     * Thêm hóa đơn vào database
     * UI→DTO→Service→Entity→DAO→DB
     * @param hoaDonDTO hóa đơn DTO cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     */
    boolean insertHoaDon(HoaDonDTO hoaDonDTO);

    /**
     * Cập nhật hóa đơn trong database
     * @param hoaDonDTO hóa đơn DTO cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateHoaDon(HoaDonDTO hoaDonDTO);

    /**
     * Cập nhật tổng tiền của hóa đơn
     * @param maHD mã hóa đơn
     * @param tongTien tổng tiền mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean CapNhatTongTienHoaDon(String maHD, double tongTien);

    /**
     * Xóa mềm hóa đơn (set DeleteAt = 1)
     * @param maHD mã hóa đơn
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean xoaMemHoaDon(String maHD);

    /**
     * Đếm số lượng hóa đơn đã sử dụng khuyến mãi với mã cho trước
     * @param maKM mã khuyến mãi
     * @return số lượng hóa đơn
     */
    int soHoaDonDaCoKhuyenMaiVoiMa(String maKM);

    /**
     * Lấy mã hóa đơn lớn nhất (để tạo mã tiếp theo)
     * @return mã hóa đơn lớn nhất
     */
    String getMaxHash();
}
