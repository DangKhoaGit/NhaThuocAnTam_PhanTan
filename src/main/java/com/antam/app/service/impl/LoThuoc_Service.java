/*
 * @ (#) LoThuoc_Service.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.dao.impl.LoThuoc_DAO;
import com.antam.app.dto.ChiTietPhieuNhapDTO;
import com.antam.app.dto.DangDieuCheDTO;
import com.antam.app.dto.DonViTinhDTO;
import com.antam.app.dto.KeDTO;
import com.antam.app.dto.LoThuocDTO;
import com.antam.app.dto.PhieuNhapDTO;
import com.antam.app.dto.ThuocDTO;
import com.antam.app.entity.ChiTietPhieuNhap;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.Ke;
import com.antam.app.entity.LoThuoc;
import com.antam.app.entity.PhieuNhap;
import com.antam.app.entity.Thuoc;
import com.antam.app.service.I_LoThuoc_Service;

import java.util.ArrayList;

/*
 * @description: Implementation của I_LoThuoc_Service
 * Theo chuẩn luồng dữ liệu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Đọc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa
 * @date: 19/04/2026
 * @version: 1.0
 */
public class LoThuoc_Service implements I_LoThuoc_Service {

    private final LoThuoc_DAO loThuocDAO = new LoThuoc_DAO();

    @Override
    public boolean themChiTietThuoc(LoThuocDTO ctt) {
        try {
            LoThuoc entity = mapDTOToEntity(ctt);
            return loThuocDAO.themChiTietThuoc(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them lo thuoc", e);
        }
    }

    @Override
    public ArrayList<LoThuocDTO> getAllChiTietThuoc() {
        ArrayList<LoThuocDTO> result = new ArrayList<>();
        try {
            ArrayList<LoThuoc> entities = loThuocDAO.getAllChiTietThuoc();
            for (LoThuoc entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay tat ca lo thuoc", e);
        }
        return result;
    }

    @Override
    public ArrayList<LoThuocDTO> getAllCHiTietThuocTheoMaThuoc(String ma) {
        ArrayList<LoThuocDTO> result = new ArrayList<>();
        try {
            ArrayList<LoThuoc> entities = loThuocDAO.getAllCHiTietThuocTheoMaThuoc(ma);
            for (LoThuoc entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay lo thuoc theo ma thuoc", e);
        }
        return result;
    }

    @Override
    public LoThuocDTO getChiTietThuoc(int ma) {
        try {
            LoThuoc entity = loThuocDAO.getChiTietThuoc(ma);
            return entity == null ? null : mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay lo thuoc theo ma", e);
        }
    }

    @Override
    public boolean CapNhatSoLuongChiTietThuoc(int maCTT, int soLuong) {
        try {
            return loThuocDAO.CapNhatSoLuongChiTietThuoc(maCTT, soLuong);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat ton kho lo thuoc", e);
        }
    }

    @Override
    public ArrayList<LoThuocDTO> getChiTietThuocHanSuDungGiamDan(String maThuoc) {
        ArrayList<LoThuocDTO> result = new ArrayList<>();
        try {
            ArrayList<LoThuoc> entities = loThuocDAO.getChiTietThuocHanSuDungGiamDan(maThuoc);
            for (LoThuoc entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay lo thuoc theo han su dung", e);
        }
        return result;
    }

    @Override
    public int getTongTonKhoTheoMaThuoc(String maThuoc) {
        try {
            return loThuocDAO.getTongTonKhoTheoMaThuoc(maThuoc);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi tinh tong ton kho theo ma thuoc", e);
        }
    }

    @Override
    public ArrayList<LoThuocDTO> getAllChiTietThuocVoiMaChoCTPD(String maThuoc) {
        ArrayList<LoThuocDTO> result = new ArrayList<>();
        try {
            ArrayList<LoThuoc> entities = loThuocDAO.getAllChiTietThuocVoiMaChoCTPD(maThuoc);
            for (LoThuoc entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay lo thuoc cho CTPDT", e);
        }
        return result;
    }

    private LoThuocDTO mapEntityToDTO(LoThuoc entity) {
        if (entity == null) {
            return null;
        }

        ThuocDTO thuocDTO = mapThuocEntityToDTO(entity.getMaThuoc());

        ArrayList<ChiTietPhieuNhapDTO> chiTietPhieuNhapDTOS = new ArrayList<>();
        if (entity.getMaCTPN() != null) {
            for (ChiTietPhieuNhap item : entity.getMaCTPN()) {
                if (item == null || item.getMaPN() == null) {
                    continue;
                }
                ChiTietPhieuNhapDTO dto = ChiTietPhieuNhapDTO.builder()
                        .MaPN(new PhieuNhapDTO(item.getMaPN().getMaPhieuNhap()))
                        .build();
                chiTietPhieuNhapDTOS.add(dto);
            }
        }

        return LoThuocDTO.builder()
                .MaLoThuoc(entity.getMaLoThuoc())
                .maThuocDTO(thuocDTO)
                .soLuong(entity.getSoLuong())
                .hanSuDung(entity.getHanSuDung())
                .ngaySanXuat(entity.getNgaySanXuat())
                .maCTPN(chiTietPhieuNhapDTOS)
                .build();
    }

    private LoThuoc mapDTOToEntity(LoThuocDTO dto) {
        if (dto == null) {
            return null;
        }

        Thuoc thuoc = mapThuocDTOToEntity(dto.getMaThuocDTO());

        LoThuoc entity = new LoThuoc(
                0,
                new PhieuNhapDTOToEntityAdapter().fromDTO(
                        dto.getMaCTPN() != null && !dto.getMaCTPN().isEmpty() ? dto.getMaCTPN().get(0).getMaPN() : null
                ),
                thuoc,
                dto.getSoLuong(),
                dto.getHanSuDung(),
                dto.getNgaySanXuat(),
                dto.getMaLoThuoc()
        );

        return entity;
    }

    private ThuocDTO mapThuocEntityToDTO(Thuoc entity) {
        if (entity == null) {
            return null;
        }

        DangDieuCheDTO dangDieuCheDTO = null;
        if (entity.getDangDieuChe() != null) {
            dangDieuCheDTO = new DangDieuCheDTO(
                    entity.getDangDieuChe().getMaDDC(),
                    entity.getDangDieuChe().getTenDDC(),
                    entity.getDangDieuChe().isDeleteAt()
            );
        }

        DonViTinhDTO donViTinhDTO = null;
        if (entity.getMaDVTCoSo() != null) {
            donViTinhDTO = new DonViTinhDTO(
                    entity.getMaDVTCoSo().getMaDVT(),
                    entity.getMaDVTCoSo().getTenDVT(),
                    entity.getMaDVTCoSo().isDelete()
            );
        }

        KeDTO keDTO = null;
        if (entity.getMaKe() != null) {
            keDTO = new KeDTO(
                    entity.getMaKe().getMaKe(),
                    entity.getMaKe().getTenKe(),
                    entity.getMaKe().getLoaiKe(),
                    entity.getMaKe().isDeleteAt()
            );
        }

        return ThuocDTO.builder()
                .maThuoc(entity.getMaThuoc())
                .TenThuoc(entity.getTenThuoc())
                .hamLuong(entity.getHamLuong())
                .giaBan(entity.getGiaBan())
                .giaGoc(entity.getGiaGoc())
                .thue(entity.getThue())
                .deleteAt(entity.isDeleteAt())
                .dangDieuCheDTO(dangDieuCheDTO)
                .maDVTCoSo(donViTinhDTO)
                .maKeDTO(keDTO)
                .build();
    }

    private Thuoc mapThuocDTOToEntity(ThuocDTO dto) {
        if (dto == null) {
            return null;
        }

        DangDieuChe dangDieuChe = null;
        if (dto.getDangDieuCheDTO() != null) {
            dangDieuChe = new DangDieuChe(
                    dto.getDangDieuCheDTO().getMaDDC(),
                    dto.getDangDieuCheDTO().getTenDDC(),
                    dto.getDangDieuCheDTO().isDeleteAt()
            );
        }

        DonViTinh donViTinh = null;
        if (dto.getMaDVTCoSo() != null) {
            donViTinh = new DonViTinh(
                    dto.getMaDVTCoSo().getMaDVT(),
                    dto.getMaDVTCoSo().getTenDVT(),
                    dto.getMaDVTCoSo().isDelete()
            );
        }

        Ke ke = null;
        if (dto.getMaKeDTO() != null) {
            ke = new Ke(
                    dto.getMaKeDTO().getMaKe(),
                    dto.getMaKeDTO().getTenKe(),
                    dto.getMaKeDTO().getLoaiKe(),
                    dto.getMaKeDTO().isDeleteAt()
            );
        }

        return Thuoc.builder()
                .maThuoc(dto.getMaThuoc())
                .TenThuoc(dto.getTenThuoc())
                .hamLuong(dto.getHamLuong())
                .giaBan(dto.getGiaBan())
                .giaGoc(dto.getGiaGoc())
                .thue(dto.getThue())
                .deleteAt(dto.isDeleteAt())
                .dangDieuChe(dangDieuChe)
                .maDVTCoSo(donViTinh)
                .maKe(ke)
                .build();
    }

    private static class PhieuNhapDTOToEntityAdapter {
        PhieuNhap fromDTO(PhieuNhapDTO dto) {
            if (dto == null || dto.getMaPhieuNhap() == null) {
                return new PhieuNhap("");
            }
            return new PhieuNhap(dto.getMaPhieuNhap());
        }
    }
}
