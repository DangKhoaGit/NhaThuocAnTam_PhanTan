/*
 * @ (#) DonViTinh_Service.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.dao.I_DonViTinh_DAO;
import com.antam.app.dao.impl.DonViTinh_DAO;
import com.antam.app.dto.DonViTinhDTO;
import com.antam.app.entity.DonViTinh;
import com.antam.app.service.I_DonViTinh_Service;

import java.util.ArrayList;

/*
 * @description: Implementation cua I_DonViTinh_Service
 * Theo chuan luong du lieu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Doc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa
 * @date: 19/04/2026
 * @version: 1.0
 */
public class DonViTinh_Service implements I_DonViTinh_Service {
    @Override
    public String getHashDVT() {
        try {
            return donViTinhDAO.getHashDVT();
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay hash don vi tinh", e);
        }
    }

    private final I_DonViTinh_DAO donViTinhDAO = new DonViTinh_DAO();

    @Override
    public DonViTinhDTO getDVTTheoMaDVT(int ma) {
        try {
            DonViTinh entity = donViTinhDAO.getDVTTheoMaDVT(ma);
            return entity == null ? null : mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay don vi tinh theo ma", e);
        }
    }

    @Override
    public ArrayList<DonViTinhDTO> getTatCaDonViTinh() {
        ArrayList<DonViTinhDTO> result = new ArrayList<>();
        try {
            ArrayList<DonViTinh> entities = donViTinhDAO.getTatCaDonViTinh();
            for (DonViTinh entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay tat ca don vi tinh", e);
        }
        return result;
    }

    @Override
    public ArrayList<DonViTinhDTO> getAllDonViTinh() {
        ArrayList<DonViTinhDTO> result = new ArrayList<>();
        try {
            ArrayList<DonViTinh> entities = donViTinhDAO.getAllDonViTinh();
            for (DonViTinh entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay danh sach don vi tinh", e);
        }
        return result;
    }

    @Override
    public DonViTinhDTO getDVTTheoTen(String ten) {
        try {
            DonViTinh entity = donViTinhDAO.getDVTTheoTen(ten);
            return entity == null ? null : mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay don vi tinh theo ten", e);
        }
    }

    @Override
    public DonViTinhDTO getDVTTheoMa(int ma) {
        try {
            DonViTinh entity = donViTinhDAO.getDVTTheoMa(ma);
            return entity == null ? null : mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay don vi tinh theo ma", e);
        }
    }

    @Override
    public int themDonViTinh(DonViTinhDTO donViTinhDTO) {
        try {
            return donViTinhDAO.themDonViTinh(mapDTOToEntity(donViTinhDTO));
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them don vi tinh", e);
        }
    }

    @Override
    public int updateDonViTinh(DonViTinhDTO donViTinhDTO) {
        try {
            return donViTinhDAO.updateDonViTinh(mapDTOToEntity(donViTinhDTO));
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat don vi tinh", e);
        }
    }

    @Override
    public int xoaDonViTinh(DonViTinhDTO donViTinhDTO) {
        try {
            return donViTinhDAO.xoaDonViTinh(mapDTOToEntity(donViTinhDTO));
        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa don vi tinh", e);
        }
    }

    @Override
    public int khoiPhucDonViTinh(DonViTinhDTO dvt) {
        try {
            return donViTinhDAO.khoiPhucDonViTinh(mapDTOToEntity(dvt));
        } catch (Exception e) {
            throw new RuntimeException("Loi khi khoi phuc don vi tinh", e);
        }
    }

    public DonViTinhDTO mapEntityToDTO(DonViTinh entity) {
        if (entity == null) {
            return null;
        }
        return new DonViTinhDTO(entity.getMaDVT(), entity.getTenDVT(), entity.isDelete());
    }

    public DonViTinh mapDTOToEntity(DonViTinhDTO dto) {
        if (dto == null) {
            return null;
        }
        return new DonViTinh(dto.getMaDVT(), dto.getTenDVT(), dto.isDelete());
    }
}
