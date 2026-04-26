package com.antam.app.network.command;

/*
 * @description: Các loại command được hỗ trợ trong hệ thống
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public enum CommandType {

    // === Authentication ===
    LOGIN, LOGOUT, REGISTER,

    // =========================================================
    // === HoaDon ===
    // =========================================================
    GET_HOADON_LIST,
    GET_HOADON_BY_ID,
    GET_HOADON_BY_KHACHHANG_ID,

    SEARCH_HOADON_BY_MA,
    SEARCH_HOADON_BY_STATUS,
    SEARCH_HOADON_BY_NHANVIEN,

    CREATE_HOADON,
    UPDATE_HOADON,
    UPDATE_HOADON_TOTAL,

    DELETE_HOADON,

    COUNT_HOADON_BY_KHUYENMAI,
    GET_MAX_HASH_HOADON,

    // === ChiTietHoaDon ===
    GET_CHITIETHOADON_BY_HOADON_ID,
    GET_CHITIETHOADON_ACTIVE_BY_HOADON_ID,

    CREATE_CHITIETHOADON,
    CREATE_CHITIETHOADON_TRA_KHI_DOI,

    UPDATE_CHITIETHOADON,
    UPSERT_CHITIETHOADON,

    SOFT_DELETE_CHITIETHOADON,
    DELETE_CHITIETHOADON_BY_HOADON_ID,

    CHECK_CHITIETHOADON_EXISTS,
    CHECK_CHITIETHOADON_EXISTS_WITH_STATUS,

    // =========================================================
    // === NhanVien ===
    // =========================================================
    GET_NHANVIEN_LIST,
    GET_NHANVIEN_DELETED_LIST,

    GET_NHANVIEN_BY_ID,
    GET_NHANVIEN_TAIKHOAN,

    CREATE_NHANVIEN,
    UPDATE_NHANVIEN,
    DELETE_NHANVIEN,
    KHOI_PHUC_NHAN_VIEN,

    GET_MAX_HASH_NHANVIEN,

    // =========================================================
    // === KhachHang ===
    // =========================================================
    GET_KHACHHANG_LIST,
    GET_KHACHHANG_WITH_STATS,

    GET_KHACHHANG_BY_ID,
    GET_KHACHHANG_BY_PHONE,
    SEARCH_KHACHHANG_BY_NAME,

    CREATE_KHACHHANG,
    UPDATE_KHACHHANG,

    GET_TONG_KHACHHANG,
    GET_TONG_KHACHHANG_VIP,
    GET_TONG_DONHANG_KHACHHANG,
    GET_TONG_DOANHTHU_KHACHHANG,

    GET_MAX_HASH_KHACHHANG,

    // =========================================================
    // === Thuoc ===
    // =========================================================
    GET_THUOC_LIST,
    GET_THUOC_DELETED_LIST,
    GET_THUOC_BY_ID,

    CREATE_THUOC,
    UPDATE_THUOC,
    DELETE_THUOC,
    RESTORE_THUOC,

    // =========================================================
    // === LoThuoc ===
    // =========================================================
    CREATE_LOTHUOC,
    GET_LOTHUOC_LIST,
    GET_LOTHUOC_BY_ID,
    GET_LOTHUOC_BY_THUOC_ID,

    UPDATE_LOTHUOC_QUANTITY,

    GET_LOTHUOC_FEFO_BY_THUOC_ID,
    GET_TONG_TONKHO_BY_THUOC_ID,
    GET_LOTHUOC_FOR_CHITIETPHIEUDAT,

    // =========================================================
    // === KhuyenMai ===
    // =========================================================
    GET_KHUYENMAI_LIST,
    GET_KHUYENMAI_ACTIVE_LIST,
    GET_KHUYENMAI_NOT_DELETED_LIST,
    GET_KHUYENMAI_DELETED_LIST,

    GET_KHUYENMAI_BY_ID,

    CREATE_KHUYENMAI,
    UPDATE_KHUYENMAI,
    DELETE_KHUYENMAI,
    RESTORE_KHUYENMAI,

    GET_LOAIKHUYENMAI_LIST,

    // =========================================================
    // === DonViTinh ===
    // =========================================================
    GET_DONVITINH_LIST,
    GET_DONVITINH_BY_ID,
    GET_DONVITINH_BY_NAME,

    CREATE_DONVITINH,
    UPDATE_DONVITINH,
    DELETE_DONVITINH,
    RESTORE_DONVITINH,

    GET_MAX_HASH_DONVITINH,

    // =========================================================
    // === DangDieuChe ===
    // =========================================================
    GET_DANGDIEUCHE_LIST,
    GET_DANGDIEUCHE_ACTIVE_LIST,
    GET_DANGDIEUCHE_BY_NAME,

    CREATE_DANGDIEUCHE,
    UPDATE_DANGDIEUCHE,
    DELETE_DANGDIEUCHE,
    RESTORE_DANGDIEUCHE,

    GENERATE_DANGDIEUCHE_CODE,

    // =========================================================
    // === Ke ===
    // =========================================================
    GET_KE_LIST,
    GET_KE_ACTIVE_LIST,

    GET_KE_BY_ID,
    GET_KE_BY_NAME,

    CREATE_KE,
    UPDATE_KE,
    DELETE_KE,
    RESTORE_KE,

    GENERATE_KE_CODE,
    CHECK_TEN_KE_EXISTS,

    // =========================================================
    // === PhieuNhap ===
    // =========================================================
    GET_PHIEUNHAP_LIST,
    GET_PHIEUNHAP_BY_STATUS,

    CREATE_PHIEUNHAP,
    UPDATE_PHIEUNHAP,
    UPDATE_PHIEUNHAP_STATUS,

    DELETE_PHIEUNHAP,
    CANCEL_PHIEUNHAP,

    GENERATE_PHIEUNHAP_CODE,
    CHECK_PHIEUNHAP_EXISTS,

    // === ChiTietPhieuNhap ===
    GET_CHITIETPHIEUNHAP_BY_PHIEUNHAP_ID,
    CREATE_CHITIETPHIEUNHAP,

    // =========================================================
    // === PhieuDat ===
    // =========================================================
    GET_PHIEUDAT_LIST,
    GET_PHIEUDAT_DELETED_LIST,
    GET_PHIEUDAT_BY_ID,

    CREATE_PHIEUDAT,
    UPDATE_PHIEUDAT,
    DELETE_PHIEUDAT,
    RESTORE_PHIEUDAT,

    UPDATE_PHIEUDAT_PAYMENT_STATUS,
    GET_MAX_HASH_PHIEUDAT,

    // === ChiTietPhieuDat ===
    GET_CHITIETPHIEUDAT_BY_PHIEUDAT_ID,
    CREATE_CHITIETPHIEUDAT,

    UPDATE_CHITIETPHIEUDAT_PAYMENT_STATUS,
    CANCEL_CHITIETPHIEUDAT,
    RESTORE_CHITIETPHIEUDAT,

    // =========================================================
    // === ThongKe ===
    // =========================================================
    GET_THONGKE_TRANGCHINH,

    GET_TONG_SO_THUOC,
    GET_TONG_SO_NHANVIEN,
    GET_SO_HOADON_HOMNAY,
    GET_SO_KHUYENMAI_AP_DUNG,

    GET_DOANHTHU_7_NGAY,
    GET_TOP_SANPHAM_BANCHAY,
    GET_THUOC_SAP_HETHAN,
    GET_THUOC_TONKHO_THAP,

    // --- DoanhThu ---
    GET_THONGKE_DOANHTHU,
    GET_DOANHTHU_THEO_THOIGIAN,
    GET_DOANHTHU_THEO_THANG,
    GET_TONG_DOANHTHU,
    GET_TONG_DONHANG,
    GET_SO_KHACHHANG_MOI,
    GET_TOP_SANPHAM_BANCHAY_THEO_THOIGIAN,

    GET_NHANVIEN_LIST_FOR_FILTER,

    // =========================================================
    // === System ===
    // =========================================================
    MESSAGE,
    BROADCAST,
    USER_LIST,
    FILE_TRANSFER,
    SERVER_STATUS,
    PING,

    // === Error ===
    ERROR_RESPONSE
}