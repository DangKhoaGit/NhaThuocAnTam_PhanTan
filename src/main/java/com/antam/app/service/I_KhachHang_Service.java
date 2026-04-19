package com.antam.app.service;

import java.util.ArrayList;
import java.util.List;

import com.antam.app.dto.KhachHangDTO;

/*
 * @description: Service interface cho KhachHang
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_KhachHang_Service {
    // === Data Access Methods ===
    /**
     * Lấy tất cả khách hàng từ cơ sở dữ liệu (không bao gồm thống kê)
     * @return danh sách KhachHangDTO
     */
    ArrayList<KhachHangDTO> getAllKhachHang();

    /**
     * Lấy tất cả khách hàng với thông tin thống kê
     * @return danh sách KhachHangDTO với thông tin chi tiêu, số đơn hàng, etc.
     */
    List<KhachHangDTO> loadKhachHangWithStats();

    /**
     * Tìm khách hàng theo mã
     * @param maKH mã khách hàng
     * @return KhachHangDTO nếu tìm thấy, null nếu không tìm thấy
     */
    KhachHangDTO getKhachHangTheoMa(String maKH);

    /**
     * Tìm khách hàng theo số điện thoại
     * @param soDienThoai số điện thoại
     * @return KhachHangDTO nếu tìm thấy, null nếu không tìm thấy
     */
    KhachHangDTO getKhachHangTheoSoDienThoai(String soDienThoai);

    /**
     * Tìm khách hàng theo tên (LIKE search)
     * @param tenKH tên khách hàng để tìm
     * @return danh sách KhachHangDTO phù hợp
     */
    List<KhachHangDTO> searchKhachHangByName(String tenKH);

    // === Mutation Methods ===
    /**
     * Thêm khách hàng mới vào cơ sở dữ liệu
     * @param kh KhachHangDTO chứa thông tin khách hàng
     * @return true nếu thêm thành công, false nếu thất bại
     */
    boolean insertKhachHang(KhachHangDTO kh);

    /**
     * Cập nhật thông tin khách hàng trong cơ sở dữ liệu
     * @param kh KhachHangDTO chứa thông tin cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateKhachHang(KhachHangDTO kh);

    // === Statistics Methods ===
    /**
     * Lấy tổng số khách hàng (không bị xóa)
     * @return số lượng khách hàng
     */
    int getTongKhachHang();

    /**
     * Lấy tổng số khách hàng VIP (chi tiêu >= 1.000.000)
     * @return số lượng khách hàng VIP
     */
    int getTongKhachHangVIP();

    /**
     * Lấy tổng số đơn hàng
     * @return tổng số đơn hàng
     */
    int getTongDonHang();

    /**
     * Lấy tổng doanh thu
     * @return tổng doanh thu
     */
    double getTongDoanhThu();

    /**
     * Lấy mã khách hàng tiếp theo
     * @return số hash tối đa
     */
    int getMaxHash();
}
