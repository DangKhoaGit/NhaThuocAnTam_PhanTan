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

    // === CRUD Operations ===
    CREATE, READ, UPDATE, DELETE,

    // === HoaDon Operations ===
    GET_HOADON_LIST,
    CREATE_HOADON,
    UPDATE_HOADON,
    DELETE_HOADON,
    GET_HOADON_BY_ID,

    // === ChiTietHoaDon Operations ===
    GET_CHITIETHOADON_LIST,
    CREATE_CHITIETHOADON,
    UPDATE_CHITIETHOADON,
    DELETE_CHITIETHOADON,

    // === NhanVien Operations ===
    GET_NHANVIEN_LIST,
    GET_NHANVIEN_BY_ID,
    CREATE_NHANVIEN,
    UPDATE_NHANVIEN,
    DELETE_NHANVIEN,

    // === KhachHang Operations ===
    GET_KHACHHANG_LIST,
    GET_KHACHHANG_BY_ID,
    CREATE_KHACHHANG,
    UPDATE_KHACHHANG,
    DELETE_KHACHHANG,

    // === KhuyenMai Operations ===
    GET_KHUYENMAI_LIST,
    CREATE_KHUYENMAI,
    UPDATE_KHUYENMAI,
    DELETE_KHUYENMAI,

    // === LoThuoc Operations ===
    GET_LOTHUOC_LIST,
    CREATE_LOTHUOC,
    UPDATE_LOTHUOC,
    DELETE_LOTHUOC,

    // === Thuoc Operations ===
    GET_THUOC_LIST,
    CREATE_THUOC,
    UPDATE_THUOC,
    DELETE_THUOC,

    // === DonViTinh Operations ===
    GET_DONVITINH_LIST,
    CREATE_DONVITINH,
    UPDATE_DONVITINH,
    DELETE_DONVITINH,

    // === PhieuNhap Operations ===
    GET_PHIEUNHAP_LIST,
    CREATE_PHIEUNHAP,
    UPDATE_PHIEUNHAP,
    DELETE_PHIEUNHAP,

    // === PhieuDat Operations ===
    GET_PHIEUDAT_LIST,
    CREATE_PHIEUDAT,
    UPDATE_PHIEUDAT,
    DELETE_PHIEUDAT,

    // === Search/Filter ===
    SEARCH_HOADON,
    FILTER_BY_DATE,
    FILTER_BY_NHANVIEN,

    // === Statistics/Reports ===
    GET_THONGKE_DOANHTHU,
    GET_THONGKE_TRANGCHINH,

    // === System ===
    MESSAGE,
    BROADCAST,
    USER_LIST,
    FILE_TRANSFER,
    SERVER_STATUS,
    PING,

    // === Error Handling ===
    ERROR_RESPONSE
}

