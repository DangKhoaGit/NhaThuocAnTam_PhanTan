/*
 * @ (#) HoaDon_Service.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.impl.*;
import com.antam.app.service.I_HoaDon_Service;
import com.antam.app.dto.*;
import com.antam.app.entity.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/*
 * @description: Implementation của I_HoaDon_Service
 * Theo chuẩn luồng dữ liệu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Đọc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa, Tran Tuan Hung
 * @date: 19/04/2026
 * @version: 1.0
 */
public class HoaDon_Service implements I_HoaDon_Service {

    private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
    private final NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
    private final KhachHang_DAO khachHangDAO = new KhachHang_DAO();
    private final KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();
    private final ChiTietHoaDon_Service chiTietHoaDonService = new ChiTietHoaDon_Service();
    private final LoThuoc_DAO loThuocDAO = new LoThuoc_DAO();

    /**
     * Lấy tất cả hóa đơn
     * DB→DAO→Entity→Service→DTO→UI
     */
    @Override
    public ArrayList<HoaDonDTO> getAllHoaDon() {
        ArrayList<HoaDonDTO> result = new ArrayList<>();
        try {
            ArrayList<HoaDon> hoaDonList = hoaDonDAO.getAllHoaDon();
            for (HoaDon hd : hoaDonList) {
                result.add(mapEntityToDTO(hd));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy tất cả hóa đơn", e);
        }
        return result;
    }

    /**
     * Lấy hóa đơn theo mã hóa đơn
     */
    @Override
    public HoaDonDTO getHoaDonTheoMa(String maHD) {
        try {
            HoaDon hd = hoaDonDAO.getHoaDonTheoMa(maHD);
            return hd != null ? mapEntityToDTO(hd) : null;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy hóa đơn theo mã", e);
        }
    }

    /**
     * Lấy danh sách hóa đơn theo mã khách hàng
     */
    @Override
    public ArrayList<HoaDonDTO> getHoaDonByMaKH(String maKH) {
        ArrayList<HoaDonDTO> result = new ArrayList<>();
        try {
            ArrayList<HoaDon> hoaDonList = hoaDonDAO.getHoaDonByMaKH(maKH);
            for (HoaDon hd : hoaDonList) {
                result.add(mapEntityToDTO(hd));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy hóa đơn theo khách hàng", e);
        }
        return result;
    }

    /**
     * Tìm kiếm hóa đơn theo mã hóa đơn (LIKE)
     */
    @Override
    public ArrayList<HoaDonDTO> searchHoaDonByMaHd(String maHd) {
        ArrayList<HoaDonDTO> result = new ArrayList<>();
        try {
            ArrayList<HoaDon> hoaDonList = hoaDonDAO.searchHoaDonByMaHd(maHd);
            for (HoaDon hd : hoaDonList) {
                result.add(mapEntityToDTO(hd));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm hóa đơn theo mã", e);
        }
        return result;
    }

    /**
     * Tìm kiếm hóa đơn theo trạng thái
     */
    @Override
    public ArrayList<HoaDonDTO> searchHoaDonByStatus(String status) {
        ArrayList<HoaDonDTO> result = new ArrayList<>();
        try {
            ArrayList<HoaDon> hoaDonList = hoaDonDAO.searchHoaDonByStatus(status);
            for (HoaDon hd : hoaDonList) {
                result.add(mapEntityToDTO(hd));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm hóa đơn theo trạng thái", e);
        }
        return result;
    }

    /**
     * Tìm kiếm hóa đơn theo mã nhân viên
     */
    @Override
    public ArrayList<HoaDonDTO> searchHoaDonByMaNV(String maNV) {
        ArrayList<HoaDonDTO> result = new ArrayList<>();
        try {
            ArrayList<HoaDon> hoaDonList = hoaDonDAO.searchHoaDonByMaNV(maNV);
            for (HoaDon hd : hoaDonList) {
                result.add(mapEntityToDTO(hd));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm kiếm hóa đơn theo nhân viên", e);
        }
        return result;
    }

    /**
     * Thêm hóa đơn vào database
     * UI→DTO→Service→Entity→DAO→DB
     */
    @Override
    public boolean insertHoaDon(HoaDonDTO hoaDonDTO) {
        try {
            HoaDon hd = mapDTOToEntity(hoaDonDTO);
            return hoaDonDAO.insertHoaDon(hd);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm hóa đơn", e);
        }
    }

    public boolean insertHoaDonVaChiTiet(HoaDonDTO hoaDonDTO, List<ChiTietHoaDonDTO> chiTietHoaDonList) {
        Connection con = null;
        boolean oldAutoCommit = true;
        try {
            if (hoaDonDTO == null || hoaDonDTO.getMaHD() == null || hoaDonDTO.getMaHD().trim().isEmpty()) {
                return false;
            }
            if (chiTietHoaDonList == null || chiTietHoaDonList.isEmpty()) {
                return false;
            }

            con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);

            if (!insertHoaDon(hoaDonDTO)) {
                con.rollback();
                return false;
            }

            for (ChiTietHoaDonDTO chiTiet : chiTietHoaDonList) {
                if (chiTiet == null || chiTiet.getMaLoThuocDTO() == null || chiTiet.getSoLuong() <= 0 || chiTiet.getThanhTien() <= 0) {
                    con.rollback();
                    return false;
                }
                if (chiTiet.getMaHD() == null || chiTiet.getMaHD().getMaHD() == null || chiTiet.getMaHD().getMaHD().trim().isEmpty()) {
                    chiTiet.setMaHD(new HoaDonDTO(hoaDonDTO.getMaHD()));
                }
                if (!chiTietHoaDonService.themHoacCapNhatChiTietHoaDon(chiTiet)) {
                    con.rollback();
                    return false;
                }
                if (!loThuocDAO.CapNhatSoLuongChiTietThuoc(chiTiet.getMaLoThuocDTO().getMaLoThuoc(), -chiTiet.getSoLuong())) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;
        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception rollbackException) {
                e.addSuppressed(rollbackException);
            }
            throw new RuntimeException("Lỗi khi thêm hóa đơn và chi tiết hóa đơn", e);
        } finally {
            try {
                if (con != null) con.setAutoCommit(oldAutoCommit);
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Cập nhật hóa đơn
     */
    @Override
    public boolean updateHoaDon(HoaDonDTO hoaDonDTO) {
        try {
            HoaDon hd = mapDTOToEntity(hoaDonDTO);
            return hoaDonDAO.updateHoaDon(hd);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật hóa đơn", e);
        }
    }

    /**
     * Cập nhật tổng tiền của hóa đơn
     */
    @Override
    public boolean CapNhatTongTienHoaDon(String maHD, double tongTien) {
        try {
            return hoaDonDAO.CapNhatTongTienHoaDon(maHD, tongTien);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật tổng tiền hóa đơn", e);
        }
    }

    /**
     * Xóa mềm hóa đơn
     */
    @Override
    public boolean xoaMemHoaDon(String maHD) {
        try {
            return hoaDonDAO.xoaMemHoaDon(maHD);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa mềm hóa đơn", e);
        }
    }

    /**
     * Đếm số lượng hóa đơn đã sử dụng khuyến mãi
     */
    @Override
    public int soHoaDonDaCoKhuyenMaiVoiMa(String maKM) {
        try {
            return hoaDonDAO.soHoaDonDaCoKhuyenMaiVoiMa(maKM);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đếm hóa đơn có khuyến mãi", e);
        }
    }

    /**
     * Lấy mã hóa đơn lớn nhất
     */
    @Override
    public String getMaxHash() {
        try {
            return hoaDonDAO.getMaxHash();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy mã hóa đơn lớn nhất", e);
        }
    }

    /**
     * Helper method: Chuyển đổi Entity → DTO
     * DB→DAO→Entity→Service→DTO
     */
    private HoaDonDTO mapEntityToDTO(HoaDon entity) {
        if (entity == null) return null;

        NhanVienDTO nhanVienDTO = null;
        if (entity.getMaNV() != null) {
            String maNV = entity.getMaNV().getMaNV();
            nhanVienDTO = new NhanVienDTO(maNV == null ? "" : maNV);
            nhanVienDTO.setHoTen(entity.getMaNV().getHoTen());
        }

        KhachHangDTO khachHangDTO = null;
        if (entity.getMaKH() != null) {
            String maKH = entity.getMaKH().getMaKH();
            khachHangDTO = new KhachHangDTO(maKH == null ? "" : maKH);
            String tenKH = entity.getMaKH().getTenKH();
            if (tenKH != null && !tenKH.trim().isEmpty()) {
                khachHangDTO.setTenKH(tenKH);
            }
        }

        KhuyenMaiDTO khuyenMaiDTO = null;
        if (entity.getMaKM() != null && entity.getMaKM().getMaKM() != null) {
            khuyenMaiDTO = new KhuyenMaiDTO(entity.getMaKM().getMaKM());
            khuyenMaiDTO.setTenKM(entity.getMaKM().getTenKM());
        }

        return HoaDonDTO.builder()
                .MaHD(entity.getMaHD())
                .ngayTao(entity.getNgayTao())
                .maNV(nhanVienDTO)
                .maKH(khachHangDTO)
                .maKM(khuyenMaiDTO)
                .tongTien(entity.getTongTien())
                .deleteAt(entity.isDeleteAt())
                .build();
    }

    /**
     * Helper method: Chuyển đổi DTO → Entity
     * UI→DTO→Service→Entity→DAO→DB
     */
    private HoaDon mapDTOToEntity(HoaDonDTO dto) {
        if (dto == null) return null;

        NhanVien nhanVien = null;
        if (dto.getMaNV() != null && dto.getMaNV().getMaNV() != null && !dto.getMaNV().getMaNV().trim().isEmpty()) {
            nhanVien = nhanVienDAO.findNhanVienVoiMa(dto.getMaNV().getMaNV());
            if (nhanVien == null) {
                nhanVien = new NhanVien(dto.getMaNV().getMaNV());
            }
        }

        KhachHang khachHang = null;
        if (dto.getMaKH() != null && dto.getMaKH().getMaKH() != null && !dto.getMaKH().getMaKH().trim().isEmpty()) {
            khachHang = khachHangDAO.getKhachHangTheoMa(dto.getMaKH().getMaKH());
            if (khachHang == null) {
                khachHang = new KhachHang(dto.getMaKH().getMaKH());
            }
        }

        KhuyenMai khuyenMai = null;
        if (dto.getMaKM() != null && dto.getMaKM().getMaKM() != null && !dto.getMaKM().getMaKM().trim().isEmpty()) {
            khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(dto.getMaKM().getMaKM());
            if (khuyenMai == null) {
                khuyenMai = new KhuyenMai(dto.getMaKM().getMaKM());
            }
        }

        return HoaDon.builder()
                .MaHD(dto.getMaHD())
                .ngayTao(dto.getNgayTao())
                .maNV(nhanVien)
                .maKH(khachHang)
                .maKM(khuyenMai)
                .tongTien(dto.getTongTien())
                .deleteAt(dto.isDeleteAt())
                .build();
    }
}
