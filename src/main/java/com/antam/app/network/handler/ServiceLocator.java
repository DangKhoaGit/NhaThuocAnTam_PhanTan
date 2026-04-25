package com.antam.app.network.handler;

import com.antam.app.service.impl.*;

/*
 * @description: Locator để quản lý các Service instances
 * Sử dụng Singleton Pattern để cung cấp access tới các services
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class ServiceLocator {
    private static ServiceLocator instance;

    private final HoaDon_Service hoaDonService;
    private final ChiTietHoaDon_Service chiTietHoaDonService;
    private final NhanVien_Service nhanVienService;
    private final KhachHang_Service khachHangService;
    private final KhuyenMai_Service khuyenMaiService;
    private final LoaiKhuyenMai_Service loaiKhuyenMaiService;
    private final LoThuoc_Service loThuocService;
    private final Thuoc_Service thuocService;
    private final DonViTinh_Service donViTinhService;
    private final PhieuNhap_Service phieuNhapService;
    private final PhieuDat_Service phieuDatService;
    private final DangDieuChe_Service dangDieuCheService;
    private final Ke_Service keService;
    private final ThongKeTrangChinh_Service thongKeTrangChinhService;

    private ServiceLocator() {
        this.hoaDonService = new HoaDon_Service();
        this.chiTietHoaDonService = new ChiTietHoaDon_Service();
        this.nhanVienService = new NhanVien_Service();
        this.khachHangService = new KhachHang_Service();
        this.khuyenMaiService = new KhuyenMai_Service();
        this.loaiKhuyenMaiService = new LoaiKhuyenMai_Service();
        this.loThuocService = new LoThuoc_Service();
        this.thuocService = new Thuoc_Service();
        this.donViTinhService = new DonViTinh_Service();
        this.phieuNhapService = new PhieuNhap_Service();
        this.phieuDatService = new PhieuDat_Service();
        this.dangDieuCheService = new DangDieuChe_Service();
        this.keService = new Ke_Service();
        this.thongKeTrangChinhService = new ThongKeTrangChinh_Service();
    }

    public static synchronized ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }

    // Getters
    public HoaDon_Service getHoaDonService() {
        return hoaDonService;
    }

    public ChiTietHoaDon_Service getChiTietHoaDonService() {
        return chiTietHoaDonService;
    }

    public NhanVien_Service getNhanVienService() {
        return nhanVienService;
    }

    public KhachHang_Service getKhachHangService() {
        return khachHangService;
    }

    public KhuyenMai_Service getKhuyenMaiService() {
        return khuyenMaiService;
    }

    public LoaiKhuyenMai_Service getLoaiKhuyenMaiService() {
        return loaiKhuyenMaiService;
    }

    public LoThuoc_Service getLoThuocService() {
        return loThuocService;
    }

    public Thuoc_Service getThuocService() {
        return thuocService;
    }

    public DonViTinh_Service getDonViTinhService() {
        return donViTinhService;
    }

    public PhieuNhap_Service getPhieuNhapService() {
        return phieuNhapService;
    }

    public PhieuDat_Service getPhieuDatService() {
        return phieuDatService;
    }

    public DangDieuChe_Service getDangDieuCheService() {
        return dangDieuCheService;
    }

    public Ke_Service getKeService() {
        return keService;
    }

    public ThongKeTrangChinh_Service getThongKeTrangChinhService() {
        return thongKeTrangChinhService;
    }
}
