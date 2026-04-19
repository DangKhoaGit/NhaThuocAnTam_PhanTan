package com.antam.app.dao;

import java.util.ArrayList;
import java.util.List;

import com.antam.app.entity.KhachHang;

/*
 * @description: DAO interface cho KhachHang - định nghĩa các phương thức truy cập dữ liệu
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 2.0 (refactored according to n-layer architecture)
 * 
 * Architecture: n-layer pattern
 * - Controller → DTO → Service → DAO (instance methods only) → Database
 * - Service manages DAO instance lifecycle
 * - NO static helper methods (violates layer separation)
 */
public interface I_KhachHang_DAO {
    // ========== Instance Methods Only ==========
    // Service sẽ tạo instance DAO và gọi các method này
    /**
     * Thêm khách hàng mới vào cơ sở dữ liệu
     * @param kh KhachHang entity
     * @return true nếu thêm thành công, false nếu thất bại
     */
    boolean insertKhachHang(KhachHang kh);

    /**
     * Cập nhật thông tin khách hàng trong cơ sở dữ liệu
     * @param kh KhachHang entity
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    boolean updateKhachHang(KhachHang kh);

    /**
     * Tìm khách hàng theo mã
     * @param maKH mã khách hàng
     * @return KhachHang entity nếu tìm thấy, null nếu không tìm thấy
     */
    KhachHang getKhachHangTheoMa(String maKH);

    /**
     * Tìm khách hàng theo số điện thoại
     * @param soDienThoai số điện thoại
     * @return KhachHang entity nếu tìm thấy, null nếu không tìm thấy
     */
    KhachHang getKhachHangTheoSoDienThoai(String soDienThoai);

    /**
     * Lấy danh sách tất cả khách hàng
     * @return danh sách KhachHang
     */
    ArrayList<KhachHang> getAllKhachHang();

    /**
     * Lấy danh sách tất cả khách hàng với thông tin thống kê
     * @return danh sách KhachHang với thông tin chi tiêu, số đơn hàng
     */
    List<KhachHang> getAllKhachHangWithStats();

    /**
     * Tìm khách hàng theo tên (LIKE search)
     * @param tenKH tên khách hàng để tìm kiếm
     * @return danh sách KhachHang entity phù hợp với thông tin thống kê
     */
    List<KhachHang> searchKhachHangByName(String tenKH);

    /**
     * Lấy mã khách hàng tiếp theo
     * @return số hash tối đa từ mã khách hàng
     */
    int getMaxHash();

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
     * Lấy tổng số đơn hàng (không bị xóa)
     * @return tổng số đơn hàng
     */
    int getTongDonHang();

    /**
     * Lấy tổng doanh thu từ tất cả hóa đơn (không bị xóa)
     * @return tổng doanh thu
     */
    double getTongDoanhThu();
}

