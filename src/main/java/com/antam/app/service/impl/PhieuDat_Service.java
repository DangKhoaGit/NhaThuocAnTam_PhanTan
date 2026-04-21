package com.antam.app.service.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 02/10/2025
 * @version: 1.0
 */

import java.util.ArrayList;
import java.util.List;

import com.antam.app.dao.impl.PhieuDat_DAO;
import com.antam.app.dto.ChiTietPhieuDatThuocDTO;
import com.antam.app.dto.KhachHangDTO;
import com.antam.app.dto.PhieuDatThuocDTO;
import com.antam.app.entity.PhieuDatThuoc;
import com.antam.app.service.I_PhieuDat_Service;

public class PhieuDat_Service implements I_PhieuDat_Service {
    private PhieuDat_DAO phieuDatDAO = new PhieuDat_DAO();
    private NhanVien_Service nhanVienService = new NhanVien_Service();
    private KhachHang_Service khachHangService = new KhachHang_Service();
    private KhuyenMai_Service khuyenMaiService = new KhuyenMai_Service();
    private ChiTietPhieuDat_Service chiTietService;

    public PhieuDat_Service() {
        // chiTietService will be initialized lazily
    }

    private ChiTietPhieuDat_Service getChiTietService() {
        if (chiTietService == null) {
            chiTietService = new ChiTietPhieuDat_Service(this);
        }
        return chiTietService;
    }

    @Override
    public List<PhieuDatThuocDTO> getAllPhieuDatThuocFromDBS() {
        List<PhieuDatThuocDTO> result = new ArrayList<>();
        try {
            List<PhieuDatThuoc> entities = phieuDatDAO.getAllPhieuDatThuocFromDBS();
            for (PhieuDatThuoc entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay tat ca phieu dat thuoc", e);
        }
        return result;
    }

    @Override
    public ArrayList<PhieuDatThuocDTO> getAllPhieuDatThuocDaXoa() {
        ArrayList<PhieuDatThuocDTO> result = new ArrayList<>();
        try {
            List<PhieuDatThuoc> entities = phieuDatDAO.getAllPhieuDatThuocDaXoa();
            for (PhieuDatThuoc entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay tat ca phieu dat thuoc da xoa", e);
        }
        return result;
    }

    @Override
    public boolean themPhieuDatThuocVaoDBS(PhieuDatThuocDTO dto) {
        try {
            PhieuDatThuoc entity = mapDTOToEntity(dto);
            return phieuDatDAO.themPhieuDatThuocVaoDBS(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them phieu dat thuoc", e);
        }
    }

    @Override
    public boolean xoaPhieuDatThuocTrongDBS(String maPDT) {
        try {
            return phieuDatDAO.xoaPhieuDatThuocTrongDBS(maPDT);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa phieu dat thuoc", e);
        }
    }

    @Override
    public String getMaxHash() {
        try {
            return phieuDatDAO.getMaxHash();
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay max hash", e);
        }
    }

    @Override
    public void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuocDTO ctPDT) {
        try {
            getChiTietService().themChiTietPhieuDatVaoDBS(ctPDT);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them chi tiet phieu dat", e);
        }
    }

    @Override
    public boolean capNhatThanhToanPhieuDat(String maPDT) {
        try {
            return phieuDatDAO.capNhatThanhToanPhieuDat(maPDT);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat thanh toan", e);
        }
    }

    @Override
    public PhieuDatThuocDTO getPhieuDatByMaFromDBS(String maPDT) {
        try {
            PhieuDatThuoc entity = phieuDatDAO.getPhieuDatByMaFromDBS(maPDT);
            return mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay phieu dat by ma", e);
        }
    }

    @Override
    public boolean khoiPhucPhieuDat(String maPhieu) {
        try {
            return phieuDatDAO.khoiPhucPhieuDat(maPhieu);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi khoi phuc phieu dat", e);
        }
    }

    public PhieuDatThuocDTO mapEntityToDTO(PhieuDatThuoc entity) {
        if (entity == null) {
            return null;
        }
        return PhieuDatThuocDTO.builder()
                .maPhieu(entity.getMaPhieu())
                .ngayTao(entity.getNgayTao())
                .isThanhToan(entity.isThanhToan())
                .tongTien(entity.getTongTien())
                .nhanVienDTO(nhanVienService.mapEntityToDTO(entity.getNhanVien()))
                .khachHang(khachHangService.mapEntityToDTO(entity.getKhachHang()))
                .khuyenMaiDTO(khuyenMaiService.mapEntityToDTO(entity.getKhuyenMai()))
                .build();
    }

    public PhieuDatThuoc mapDTOToEntity(PhieuDatThuocDTO dto) {
        if (dto == null) {
            return null;
        }
        return new PhieuDatThuoc(
                dto.getMaPhieu(),
                dto.getNgayTao(),
                dto.isThanhToan(),
                nhanVienService.mapDTOToEntity(dto.getNhanVienDTO()),
                khachHangService.mapDTOToEntity(dto.getKhachHang()),
                khuyenMaiService.mapDTOToEntity(dto.getKhuyenMaiDTO()),
                dto.getTongTien()
        );
    }

    public static Thuoc_Service thuoc_dao = new Thuoc_Service();
    private NhanVien_Service nvDAO = new NhanVien_Service();
    private KhachHang_Service khDAO = new KhachHang_Service();
    private KhuyenMai_Service kmDAO = new KhuyenMai_Service();

    /**
     * Helper method để lấy danh sách tất cả khách hàng (không bị xóa)
     * Dùng cho static methods trong I_PhieuDat_Service
     * @return danh sách KhachHangDTO
     */
    public ArrayList<KhachHangDTO> getAllKhachHangFromService() {
        return khDAO.getAllKhachHang();
    }


}
