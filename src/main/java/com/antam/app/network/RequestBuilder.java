package com.antam.app.network;

import com.antam.app.dto.*;
import com.antam.app.network.command.CommandType;
import com.antam.app.network.message.Command;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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

    private static Map<String, Object> thongKeDoanhThuPayload(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("startDate", tuNgay.toString());
        payload.put("endDate", denNgay.toString());
        payload.put("nhanVien", maNV);
        return payload;
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

    public static Command getHoaDonByKhachHangId(String maKH) {
        return build(CommandType.GET_HOADON_BY_KHACHHANG_ID, map("maKH", maKH));
    }

    public static Command createHoaDon(HoaDonDTO dto) {
        return build(CommandType.CREATE_HOADON, map("hoaDon", dto));
    }

    public static Command createHoaDonWithDetails(HoaDonDTO dto, List<ChiTietHoaDonDTO> chiTietHoaDonList) {
        Map<String, Object> payload = map("hoaDon", dto);
        payload.put("chiTietHoaDonList", chiTietHoaDonList);
        return build(CommandType.CREATE_HOADON_WITH_DETAILS, payload);
    }

    public static Command updateHoaDon(HoaDonDTO dto) {
        return build(CommandType.UPDATE_HOADON, map("hoaDon", dto));
    }

    public static Command deleteHoaDon(String maHD) {
        return build(CommandType.DELETE_HOADON, map("maHD", maHD));
    }

    public static Command searchHoaDonByMa(String ma) {
        return build(CommandType.SEARCH_HOADON_BY_MA, map("maHD", ma));
    }

    public static Command searchHoaDonByStatus(String status) {
        return build(CommandType.SEARCH_HOADON_BY_STATUS, map("status", status));
    }

    public static Command searchHoaDonByNhanVien(String maNV) {
        return build(CommandType.SEARCH_HOADON_BY_NHANVIEN, map("maNV", maNV));
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
        return build(CommandType.GET_KHACHHANG_BY_PHONE, map("phone", phone));
    }

    public static Command searchKhachHangByName(String name) {
        return build(CommandType.SEARCH_KHACHHANG_BY_NAME, map("tenKH", name));
    }


    public static Command getKhachHangById(String maKH) {
        return build(CommandType.GET_KHACHHANG_BY_ID, map("maKH", maKH));
    }

    public static Command getKhachHangWithStats() {
        return build(CommandType.GET_KHACHHANG_WITH_STATS);
    }

    public static Command updateKhachHang(KhachHangDTO khachHangDTO) {
        return build(CommandType.UPDATE_KHACHHANG, map("khachHang", khachHangDTO));
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

    public static Command getDoanhThuTheoThoiGian(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        return build(CommandType.GET_DOANHTHU_THEO_THOIGIAN, thongKeDoanhThuPayload(tuNgay, denNgay, maNV));
    }

    public static Command getDoanhThuTheoThang(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        return build(CommandType.GET_DOANHTHU_THEO_THANG, thongKeDoanhThuPayload(tuNgay, denNgay, maNV));
    }

    public static Command getTongDoanhThu(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        return build(CommandType.GET_TONG_DOANHTHU, thongKeDoanhThuPayload(tuNgay, denNgay, maNV));
    }

    public static Command getTongDonHang(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        return build(CommandType.GET_TONG_DONHANG, thongKeDoanhThuPayload(tuNgay, denNgay, maNV));
    }

    public static Command getSoKhachHangMoi(LocalDate tuNgay, LocalDate denNgay, String maNV) {
        return build(CommandType.GET_SO_KHACHHANG_MOI, thongKeDoanhThuPayload(tuNgay, denNgay, maNV));
    }

    public static Command getTopSanPhamBanChay(LocalDate tuNgay, LocalDate denNgay, int limit) {
        Map<String, Object> payload = thongKeDoanhThuPayload(tuNgay, denNgay, null);
        payload.put("limit", limit);
        return build(CommandType.GET_TOP_SANPHAM_BANCHAY_THEO_THOIGIAN, payload);
    }

    public static Command getNhanVienListForFilter() {
        return build(CommandType.GET_NHANVIEN_LIST_FOR_FILTER);
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

    public static Command updateDonViTinh(DonViTinhDTO donViTinhDTO) {
        return build(CommandType.UPDATE_DONVITINH, map("donViTinh", donViTinhDTO));
    }

    public static Command deleteDonViTinh(DonViTinhDTO donViTinhDTO) {
        return build(CommandType.DELETE_DONVITINH, map("maDVT", donViTinhDTO.getMaDVT()));
    }

    public static Command khoiPhucDonViTinh(DonViTinhDTO donViTinhDTO) {
        return build(CommandType.RESTORE_DONVITINH, map("maDVT", donViTinhDTO.getMaDVT()));
    }

    public static Command getDangDieuCheList() {
        return build(CommandType.GET_DANGDIEUCHE_LIST);
    }

    public static Command taoMaDCCTuDong() {
        return build(CommandType.GENERATE_DANGDIEUCHE_CODE);
    }

    public static Command createDangDieuChe(DangDieuCheDTO dangDieuCheDTO) {
        return build(CommandType.CREATE_DANGDIEUCHE, map("dangDieuChe", dangDieuCheDTO));
    }

    public static Command getDDCTheoName(String tenDDC) {
        return build(CommandType.GET_DANGDIEUCHE_BY_NAME, map("tenDDC", tenDDC));
    }

    public static Command suaDangDieuChe(DangDieuCheDTO dangDieuCheDTO) {
        return build(CommandType.UPDATE_DANGDIEUCHE, map("dangDieuChe", dangDieuCheDTO));
    }

    public static Command xoaDangDieuChe(int i) {
        return build(CommandType.DELETE_DANGDIEUCHE, map("maDDC", i));
    }

    public static Command khoiPhucDangDieuChe(int i) {
        return build(CommandType.RESTORE_DANGDIEUCHE, map("maDDC", i));
    }

    public static Command getChiTietPDT(String maPhieu) {
        return build(CommandType.GET_CHITIETPHIEUDAT_BY_PHIEUDAT_ID, map("maPD", maPhieu));
    }

    public static Command getPhieuDatThuocDaXoa() {
        return  build(CommandType.GET_PHIEUDAT_DELETED_LIST);
    }

    public static Command khoiPhucChiTietPhieu(String maPhieu) {
        return build(CommandType.RESTORE_CHITIETPHIEUDAT, map("maCTPD", maPhieu));
    }

    public static Command capNhatSoLuongChiTietThuoc(int maLoThuoc, int soMoi) {
        return build(CommandType.UPDATE_LOTHUOC_QUANTITY, map(
                "maLoThuoc", maLoThuoc,
                "deltaSoLuong", soMoi
        ));
    }

    public static Command huyChiTietPhieu(String maPhieu) {
        return build(CommandType.CANCEL_CHITIETPHIEUDAT, map("maCTPD", maPhieu));
    }

    public static Command xoaPhieuDat(String maPhieu) {
        return build(CommandType.DELETE_PHIEUDAT, map("maPD", maPhieu));
    }

    public static Command countHoaDonByKhuyenMai(String maKM) {
        return build(CommandType.COUNT_HOADON_BY_KHUYENMAI, map("maKM", maKM));
    }

    public static Command insertKhachHang(KhachHangDTO khach) {
        return build(CommandType.CREATE_KHACHHANG, map("khachHang", khach));
    }

    public static Command updateSoLuongLoThuoc(int maLoThuoc, int i) {
        return build(CommandType.UPDATE_LOTHUOC_QUANTITY, map(
                "maLoThuoc", maLoThuoc,
                "deltaSoLuong", i
        ));
    }

    public static Command getMaxHashKhachHang() {
        return build(CommandType.GET_MAX_HASH_KHACHHANG);
    }

    public static Command getKhuyenMaiConHieuLuc() {
        return build(CommandType.GET_KHUYENMAI_ACTIVE_LIST);
    }

    public static Command getMaxHashHoaDon() {
        return build(CommandType.GET_MAX_HASH_HOADON);
    }

    public static Command getKeList() {
        return build(CommandType.GET_KE_LIST);
    }

    public static Command getLoThuocByMaThuoc(String maThuoc) {
        return build(CommandType.GET_LOTHUOC_FEFO_BY_THUOC_ID, map("maThuoc", maThuoc));
    }

    public static Command updateTrangThaiPhieuDat(String maPhieu, boolean b) {
        return  build(CommandType.UPDATE_PHIEUNHAP_STATUS, map(
                "maPD", maPhieu,
                "status", b
        ));
    }
}