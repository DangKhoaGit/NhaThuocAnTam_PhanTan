package com.antam.app.network;

import com.antam.app.dto.*;
import com.antam.app.network.command.CommandType;
import com.antam.app.network.message.Command;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {

    // =========================================================
    // 🔧 CORE BUILDER (QUAN TRỌNG NHẤT)
    // =========================================================
    private static Command build(CommandType type) {
        return Command.builder()
                .type(type)
                .payload(new HashMap<>())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    private static Command build(CommandType type, Map<String, Object> payload) {
        return Command.builder()
                .type(type)
                .payload(payload != null ? payload : new HashMap<>())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    private static Map<String, Object> map(String key, Object value) {
        Map<String, Object> m = new HashMap<>();
        m.put(key, value);
        return m;
    }

    // =========================================================
    // 🔐 AUTH
    // =========================================================
    public static Command login(String u, String p) {
        Map<String, Object> m = new HashMap<>();
        m.put("username", u);
        m.put("password", p);
        return build(CommandType.LOGIN, m);
    }

    public static Command logout(String sessionId) {
        return Command.builder()
                .type(CommandType.LOGOUT)
                .sessionId(sessionId)
                .payload(new HashMap<>())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    // =========================================================
    // 🧾 HOADON
    // =========================================================
    public static Command getHoaDonList() { return build(CommandType.GET_HOADON_LIST); }

    public static Command getHoaDonById(String maHD) {
        return build(CommandType.GET_HOADON_BY_ID, map("maHD", maHD));
    }

    public static Command createHoaDon(HoaDonDTO dto) {
        return build(CommandType.CREATE_HOADON, map("hoaDon", dto));
    }

    public static Command updateHoaDon(HoaDonDTO dto) {
        return build(CommandType.UPDATE_HOADON, map("hoaDon", dto));
    }

    public static Command deleteHoaDon(String maHD) {
        return build(CommandType.DELETE_HOADON, map("maHD", maHD));
    }

    public static Command searchHoaDonByMa(String ma) {
        return build(CommandType.SEARCH_HOADON_BY_MA, map("ma", ma));
    }

    // =========================================================
    // 👥 NHANVIEN
    // =========================================================
    public static Command getNhanVienList() { return build(CommandType.GET_NHANVIEN_LIST); }

    public static Command createNhanVien(NhanVienDTO dto) {
        return build(CommandType.CREATE_NHANVIEN, map("nv", dto));
    }

    public static Command updateNhanVien(NhanVienDTO dto) {
        return build(CommandType.UPDATE_NHANVIEN, map("nv", dto));
    }

    public static Command deleteNhanVien(String id) {
        return build(CommandType.DELETE_NHANVIEN, map("maNV", id));
    }

    // =========================================================
    // 👤 KHACHHANG
    // =========================================================
    public static Command getKhachHangList() {
        return build(CommandType.GET_KHACHHANG_LIST);
    }

    public static Command getKhachHangByPhone(String phone) {
        return build(CommandType.GET_KHACHHANG_BY_PHONE, map("soDienThoai", phone));
    }

    public static Command searchKhachHangByName(String name) {
        return build(CommandType.SEARCH_KHACHHANG_BY_NAME, map("tenKH", name));
    }


    public static Command getKhachHangById(String maKH) {
        return build(CommandType.GET_KHACHHANG_BY_PHONE, map("MaKH", maKH));
    }

    // =========================================================
    // 💊 THUOC
    // =========================================================
    public static Command getThuocList() { return build(CommandType.GET_THUOC_LIST); }

    public static Command createThuoc(ThuocDTO dto) {
        return build(CommandType.CREATE_THUOC, map("thuoc", dto));
    }

    // =========================================================
    // 📦 PHIEUNHAP
    // =========================================================
    public static Command getPhieuNhapList() {
        return build(CommandType.GET_PHIEUNHAP_LIST);
    }

    public static Command createPhieuNhap(PhieuNhapDTO dto) {
        return build(CommandType.CREATE_PHIEUNHAP, map("pn", dto));
    }

    public static Command cancelPhieuNhap(String ma) {
        return build(CommandType.CANCEL_PHIEUNHAP, map("maPN", ma));
    }

    // =========================================================
    // 📦 PHIEUDAT
    // =========================================================
    public static Command getPhieuDatList() {
        return build(CommandType.GET_PHIEUDAT_LIST);
    }

    public static Command createPhieuDat(PhieuDatThuocDTO dto) {
        return build(CommandType.CREATE_PHIEUDAT, map("pdt", dto));
    }

    // =========================================================
    // 📊 THONGKE
    // =========================================================
    public static Command thongKeTrangChinh() {
        return build(CommandType.GET_THONGKE_TRANGCHINH);
    }

    public static Command doanhThuTheoThoiGian(Map<String, Object> payload) {
        return build(CommandType.GET_DOANHTHU_THEO_THOIGIAN, payload);
    }

    public static Command tongDoanhThu(Map<String, Object> payload) {
        return build(CommandType.GET_TONG_DOANHTHU, payload);
    }

    // =========================================================
    // 🧾 CHITIET HOADON
    // =========================================================
    public static Command getChiTietHoaDon(String maHD) {
        return build(CommandType.GET_CHITIETHOADON_BY_HOADON_ID, map("maHD", maHD));
    }

    public static Command createChiTietHoaDon(ChiTietHoaDonDTO dto) {
        return build(CommandType.CREATE_CHITIETHOADON, map("chiTietHoaDon", dto));
    }

    // =========================================================
    // ⚙️ SYSTEM
    // =========================================================
    public static Command ping() {
        return build(CommandType.PING);
    }

    public static Command serverStatus() {
        return build(CommandType.SERVER_STATUS);
    }

    public static Command broadcast(String msg) {
        return build(CommandType.BROADCAST, map("message", msg));
    }

    public static Command createKhuyenMai(KhuyenMaiDTO khuyenMaiDTO) {
        return build(CommandType.CREATE_KHUYENMAI, map("khuyenMai", khuyenMaiDTO));
    }

    public static Command tonTaiCTHD(String maHD, int maLoThuoc) {
        return build(CommandType.CHECK_CHITIETHOADON_EXISTS, map(
                "maHD" , maHD,
                "maLoThuoc", maLoThuoc
        ));
    }

    private static Map<String, Object> map(String k1, Object v1, String k2, Object v2) {
        Map<String, Object> m = map(k1, v1);
        m.put(k2, v2);
        return m;
    }

    public static Command getDonViTinhById(int maDVT) {
        return build(CommandType.GET_DONVITINH_BY_ID, map("maDVT", maDVT));
    }

    public static Command getActiveKeList() {
        return build(CommandType.GET_KE_ACTIVE_LIST);
    }

    public static Command getThuocById(String maThuoc) {
        return build(CommandType.GET_THUOC_BY_ID, map("maThuoc",maThuoc));
    }

    public static Command getActivceDDCList() {
        return build(CommandType.GET_DANGDIEUCHE_ACTIVE_LIST);
    }

    public static Command getLoThuocList() {
        return build(CommandType.GET_LOTHUOC_LIST);
    }

    public static Command getKhuyenMaiById(String maKM) {
        return build(CommandType.GET_KHUYENMAI_BY_ID, map("maKM", maKM));
    }

    public static Command updateThuoc(ThuocDTO thuocDTO) {
        return build(CommandType.UPDATE_THUOC, map("thuoc", thuocDTO));
    }

    public static Command deleteThuoc(String maThuoc) {
        return build(CommandType.DELETE_THUOC, map("maThuoc", maThuoc));
    }

    public static Command getDonViTinhList() {
        return build(CommandType.GET_DONVITINH_LIST);
    }

    public static Command getLoThuocByLoThuocId(int maLoThuoc) {
        return build(CommandType.GET_LOTHUOC_BY_ID, map("maLoThuoc", maLoThuoc));
    }

    public static Command softDeleteChiTietHoaDon(String maHD, int maLoThuoc, String tinhTrang) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("maHD", maHD);
        payload.put("maLoThuoc", maLoThuoc);
        payload.put("tinhTrang", tinhTrang);
        return build(CommandType.SOFT_DELETE_CHITIETHOADON, payload);
    }

    public static Command updateLoThuocQuantity(int maLoThuoc, int soLuong) {
        return build(CommandType.UPDATE_LOTHUOC_QUANTITY, map(
                "maLoThuoc", maLoThuoc,
                "deltaSoLuong", soLuong
        ));
    }

    public static Command updateHoaDonTongTien(String maHD, double v) {
        return build(CommandType.UPDATE_HOADON_TOTAL, map(
                "maHD", maHD,
                "tongTien", v
        ));
    }

    public static Command getLoaiKhuyenMaiList() {
        return build(CommandType.GET_LOAIKHUYENMAI_LIST);
    }

    public static Command getNhanVienByTaiKhoan(String text) {
        return build(CommandType.GET_NHANVIEN_TAIKHOAN, map("taiKhoan", text));
    }

    public static Command getMaxHashNhanVien() {
        return  build(CommandType.GET_MAX_HASH_NHANVIEN);
    }
}