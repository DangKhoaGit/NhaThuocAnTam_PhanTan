/*
 * @ (#) Thuoc_Service.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.dao.impl.Thuoc_DAO;
import com.antam.app.dto.DangDieuCheDTO;
import com.antam.app.dto.DonViTinhDTO;
import com.antam.app.dto.KeDTO;
import com.antam.app.dto.ThuocDTO;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.entity.DonViTinh;
import com.antam.app.entity.Ke;
import com.antam.app.entity.Thuoc;
import com.antam.app.service.I_Thuoc_Service;

import java.util.ArrayList;

/*
 * @description: Implementation của I_Thuoc_Service
 * Theo chuẩn luồng dữ liệu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Đọc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa
 * @date: 19/04/2026
 * @version: 1.0
 */
public class Thuoc_Service implements I_Thuoc_Service {

    private final Thuoc_DAO thuocDAO = new Thuoc_DAO();

    @Override
    public ArrayList<ThuocDTO> getAllThuoc() {
        ArrayList<ThuocDTO> result = new ArrayList<>();
        try {
            ArrayList<Thuoc> entities = thuocDAO.getAllThuoc();
            for (Thuoc entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay tat ca thuoc", e);
        }
        return result;
    }

    @Override
    public ArrayList<ThuocDTO> getAllThuocDaXoa() {
        ArrayList<ThuocDTO> result = new ArrayList<>();
        try {
            ArrayList<Thuoc> entities = thuocDAO.getAllThuocDaXoa();
            for (Thuoc entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay tat ca thuoc da xoa", e);
        }
        return result;
    }

    @Override
    public ThuocDTO getThuocTheoMa(String ma) {
        try {
            Thuoc entity = thuocDAO.getThuocTheoMa(ma);
            return entity == null ? null : mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay thuoc theo ma", e);
        }
    }

    @Override
    public boolean themThuoc(ThuocDTO t) {
        try {
            Thuoc entity = mapDTOToEntity(t);
            return thuocDAO.themThuoc(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them thuoc", e);
        }
    }

    @Override
    public boolean capNhatThuoc(ThuocDTO t) {
        try {
            Thuoc entity = mapDTOToEntity(t);
            return thuocDAO.capNhatThuoc(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat thuoc", e);
        }
    }

    @Override
    public boolean xoaThuocTheoMa(String ma) {
        try {
            return thuocDAO.xoaThuocTheoMa(ma);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa thuoc theo ma", e);
        }
    }

    @Override
    public boolean khoiPhucThuocTheoMa(String ma) {
        try {
            return thuocDAO.khoiPhucThuocTheoMa(ma);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi khoi phuc thuoc theo ma", e);
        }
    }

    private ThuocDTO mapEntityToDTO(Thuoc entity) {
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

    private Thuoc mapDTOToEntity(ThuocDTO dto) {
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
}
