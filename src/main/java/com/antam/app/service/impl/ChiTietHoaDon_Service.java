/*
 * @ (#) ChiTietHoaDon_Service.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.dao.impl.*;
import com.antam.app.service.I_ChiTietHoaDon_Service;
import com.antam.app.dto.*;
import com.antam.app.entity.*;

import java.util.ArrayList;
import java.util.List;

/*
 * @description: Implementation của I_ChiTietHoaDon_Service
 * Theo chuẩn luồng dữ liệu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Đọc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa, Tran Tuan Hung
 * @date: 19/04/2026
 * @version: 1.0
 */
public class ChiTietHoaDon_Service implements I_ChiTietHoaDon_Service {

    private final ChiTietHoaDon_DAO chiTietHoaDonDAO = new ChiTietHoaDon_DAO();
    private final HoaDon_DAO hoaDonDAO = new HoaDon_DAO();
    private final LoThuoc_DAO loThuocDAO = new LoThuoc_DAO();
    private final DonViTinh_DAO donViTinhDAO = new DonViTinh_DAO();

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn
     * DB→DAO→Entity→Service→DTO→UI
     */
    @Override
    public List<ChiTietHoaDonDTO> getChiTietByMaHD(String maHD) {
        List<ChiTietHoaDonDTO> result = new ArrayList<>();
        try {
            List<ChiTietHoaDon> list = chiTietHoaDonDAO.getChiTietByMaHD(maHD);
            for (ChiTietHoaDon cthd : list) {
                result.add(mapEntityToDTO(cthd));
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy chi tiết hóa đơn", e);
        }
        return result;
    }

    /**
     * Lấy tất cả chi tiết hóa đơn theo mã hóa đơn (ArrayList version)
     */
    @Override
    public ArrayList<ChiTietHoaDonDTO> getAllChiTietHoaDonTheoMaHD(String maHD) {
        ArrayList<ChiTietHoaDonDTO> result = new ArrayList<>();
        try {
            ArrayList<ChiTietHoaDon> list = chiTietHoaDonDAO.getAllChiTietHoaDonTheoMaHD(maHD);
            if (list != null) {
                for (ChiTietHoaDon cthd : list) {
                    result.add(mapEntityToDTO(cthd));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy tất cả chi tiết hóa đơn", e);
        }
        return result;
    }

    /**
     * Lấy tất cả chi tiết hóa đơn với trạng thái "Bán"
     */
    @Override
    public ArrayList<ChiTietHoaDonDTO> getAllChiTietHoaDonTheoMaHDConBan(String maHD) {
        ArrayList<ChiTietHoaDonDTO> result = new ArrayList<>();
        try {
            ArrayList<ChiTietHoaDon> list = chiTietHoaDonDAO.getAllChiTietHoaDonTheoMaHDConBan(maHD);
            if (list != null) {
                for (ChiTietHoaDon cthd : list) {
                    result.add(mapEntityToDTO(cthd));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy chi tiết hóa đơn con bán", e);
        }
        return result;
    }

    /**
     * Xóa mềm chi tiết hóa đơn (update trạng thái)
     */
    @Override
    public boolean xoaMemChiTietHoaDon(String maHD, int maCTT, String tinhTrang) {
        try {
            return chiTietHoaDonDAO.xoaMemChiTietHoaDon(maHD, maCTT, tinhTrang);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa mềm chi tiết hóa đơn", e);
        }
    }

    /**
     * Thêm chi tiết hóa đơn vào database
     * UI→DTO→Service→Entity→DAO→DB
     */
    @Override
    public boolean themChiTietHoaDon(ChiTietHoaDonDTO cthd) {
        try {
            ChiTietHoaDon entity = mapDTOToEntity(cthd);
            return chiTietHoaDonDAO.themChiTietHoaDon(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm chi tiết hóa đơn", e);
        }
    }

    /**
     * Thêm chi tiết hóa đơn với trạng thái "Trả Khi Đổi"
     * Giới hạn tối đa 2 dòng
     */
    @Override
    public boolean themChiTietHoaDonTraKhiDoi(ChiTietHoaDonDTO cthd) {
        try {
            ChiTietHoaDon entity = mapDTOToEntity(cthd);
            return chiTietHoaDonDAO.themChiTietHoaDonTraKhiDoi(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm chi tiết hóa đơn trả khi đổi", e);
        }
    }

    /**
     * Cập nhật chi tiết hóa đơn
     */
    @Override
    public boolean updateChiTietHoaDon(ChiTietHoaDonDTO cthd) {
        try {
            ChiTietHoaDon entity = mapDTOToEntity(cthd);
            return chiTietHoaDonDAO.updateChiTietHoaDon(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật chi tiết hóa đơn", e);
        }
    }

    /**
     * Kiểm tra chi tiết hóa đơn đã tồn tại theo khóa chính kép (MaHD, MaLoThuoc)
     */
    @Override
    public boolean tonTaiChiTietHoaDon(String maHD, int maLoThuoc) {
        try {
            return chiTietHoaDonDAO.tonTaiChiTietHoaDon(maHD, maLoThuoc);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi kiểm tra tồn tại chi tiết hóa đơn", e);
        }
    }

    /**
     * Kiểm tra chi tiết hóa đơn đã tồn tại theo khóa chính kép (MaHD, MaLoThuoc, TinhTrang)
     */
    @Override
    public boolean tonTaiChiTietHoaDonTheoTinhTrang(String maHD, int maLoThuoc, String tinhTrang) {
        try {
            return chiTietHoaDonDAO.tonTaiChiTietHoaDonTheoTinhTrang(maHD, maLoThuoc, tinhTrang);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi kiểm tra tồn tại chi tiết hóa đơn theo trạng thái", e);
        }
    }

    /**
     * Thêm hoặc cập nhật chi tiết hóa đơn (upsert theo khóa MaHD + MaLoThuoc + TinhTrang)
     */
    @Override
    public boolean themHoacCapNhatChiTietHoaDon(ChiTietHoaDonDTO cthd) {
        try {
            String maHD = cthd.getMaHD() != null ? cthd.getMaHD().getMaHD() : null;
            int maLoThuoc = cthd.getMaLoThuocDTO() != null ? cthd.getMaLoThuocDTO().getMaLoThuoc() : -1;
            String tinhTrang = cthd.getTinhTrang();

            if (maHD == null || maLoThuoc <= 0 || tinhTrang == null) {
                return false;
            }

            // Check nếu đã tồn tại theo 3 khóa (MaHD, MaLoThuoc, TinhTrang)
            if (tonTaiChiTietHoaDonTheoTinhTrang(maHD, maLoThuoc, tinhTrang)) {
                // Đã tồn tại: UPDATE
                return updateChiTietHoaDon(cthd);
            } else {
                // Chưa tồn tại: INSERT
                return themChiTietHoaDon(cthd);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm hoặc cập nhật chi tiết hóa đơn", e);
        }
    }

    /**
     * Xóa tất cả chi tiết hóa đơn theo mã hóa đơn
     */
    @Override
    public boolean deleteChiTietHoaDonByMaHD(String maHD) {
        try {
            return chiTietHoaDonDAO.deleteChiTietHoaDonByMaHD(maHD);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa chi tiết hóa đơn", e);
        }
    }

    /**
     * Helper method: Chuyển đổi Entity → DTO
     * DB→DAO→Entity→Service→DTO
     */
    private ChiTietHoaDonDTO mapEntityToDTO(ChiTietHoaDon entity) {
        if (entity == null) return null;

        HoaDonDTO hoaDonDTO = null;
        if (entity.getMaHD() != null && entity.getMaHD().getMaHD() != null) {
            hoaDonDTO = new HoaDonDTO(entity.getMaHD().getMaHD());
        }

        LoThuocDTO loThuocDTO = null;
        if (entity.getMaLoThuoc() != null) {
            loThuocDTO = new LoThuocDTO();
            loThuocDTO.setMaLoThuoc(entity.getMaLoThuoc().getMaLoThuoc());

            if (entity.getMaLoThuoc().getMaThuoc() != null && entity.getMaLoThuoc().getMaThuoc().getMaThuoc() != null) {
                ThuocDTO thuocDTO = new ThuocDTO(entity.getMaLoThuoc().getMaThuoc().getMaThuoc());
                thuocDTO.setTenThuoc(entity.getMaLoThuoc().getMaThuoc().getTenThuoc());
                loThuocDTO.setMaThuocDTO(thuocDTO);
            }
        }

        DonViTinhDTO donViTinhDTO = null;
        if (entity.getMaDVT() != null) {
            donViTinhDTO = new DonViTinhDTO(
                    entity.getMaDVT().getMaDVT(),
                    entity.getMaDVT().getTenDVT(),
                    entity.getMaDVT().isDelete()
            );
        }

        return ChiTietHoaDonDTO.builder()
                .MaHD(hoaDonDTO)
                .maLoThuocDTO(loThuocDTO)
                .soLuong(entity.getSoLuong())
                .maDVT(donViTinhDTO)
                .tinhTrang(entity.getTinhTrang())
                .thanhTien(entity.getThanhTien())
                .build();
    }

    /**
     * Helper method: Chuyển đổi DTO → Entity
     * UI→DTO→Service→Entity→DAO→DB
     */
    private ChiTietHoaDon mapDTOToEntity(ChiTietHoaDonDTO dto) {
        if (dto == null) return null;

        HoaDon hoaDon = null;
        if (dto.getMaHD() != null && dto.getMaHD().getMaHD() != null && !dto.getMaHD().getMaHD().trim().isEmpty()) {
            hoaDon = hoaDonDAO.getHoaDonTheoMa(dto.getMaHD().getMaHD());
            if (hoaDon == null) {
                hoaDon = new HoaDon(dto.getMaHD().getMaHD());
            }
        }

        LoThuoc loThuoc = null;
        if (dto.getMaLoThuocDTO() != null) {
            loThuoc = loThuocDAO.getChiTietThuoc(dto.getMaLoThuocDTO().getMaLoThuoc());
            if (loThuoc == null) {
                loThuoc = new LoThuoc();
                loThuoc.setMaLoThuoc(dto.getMaLoThuocDTO().getMaLoThuoc());
            }
        }

        DonViTinh donViTinh = null;
        if (dto.getMaDVT() != null && dto.getMaDVT().getMaDVT() > 0) {
            donViTinh = donViTinhDAO.getDVTTheoMa(dto.getMaDVT().getMaDVT());
            if (donViTinh == null) {
                donViTinh = new DonViTinh(dto.getMaDVT().getMaDVT());
            }
        }

        return ChiTietHoaDon.builder()
                .MaHD(hoaDon)
                .maLoThuoc(loThuoc)
                .soLuong(dto.getSoLuong())
                .maDVT(donViTinh)
                .tinhTrang(dto.getTinhTrang())
                .thanhTien(dto.getThanhTien())
                .build();
    }
}
