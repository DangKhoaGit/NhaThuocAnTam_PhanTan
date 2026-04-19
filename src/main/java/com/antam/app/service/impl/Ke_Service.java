/*
 * @ (#) Ke_Service.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.dao.I_Ke_DAO;
import com.antam.app.dao.impl.Ke_DAO;
import com.antam.app.dto.KeDTO;
import com.antam.app.entity.Ke;
import com.antam.app.service.I_Ke_Service;

import java.util.ArrayList;

/*
 * @description: Implementation cua I_Ke_Service
 * Theo chuan luong du lieu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Doc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa
 * @date: 19/04/2026
 * @version: 1.0
 */
public class Ke_Service implements I_Ke_Service {

    private final I_Ke_DAO keDAO = new Ke_DAO();

    @Override
    public boolean xoaKe(String maKe) {
        try {
            return keDAO.xoaKe(maKe);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa ke", e);
        }
    }

    @Override
    public boolean khoiPhucKe(String maKe) {
        try {
            return keDAO.khoiPhucKe(maKe);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi khoi phuc ke", e);
        }
    }

    @Override
    public boolean suaKe(KeDTO keDTO) {
        try {
            return keDAO.suaKe(mapDTOToEntity(keDTO));
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat ke", e);
        }
    }

    @Override
    public boolean themKe(KeDTO keDTO) {
        try {
            return keDAO.themKe(mapDTOToEntity(keDTO));
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them ke", e);
        }
    }

    @Override
    public String taoMaKeTuDong() {
        try {
            return keDAO.taoMaKeTuDong();
        } catch (Exception e) {
            throw new RuntimeException("Loi khi tao ma ke tu dong", e);
        }
    }

    @Override
    public ArrayList<KeDTO> getTatCaKeThuoc() {
        ArrayList<KeDTO> result = new ArrayList<>();
        try {
            ArrayList<Ke> entities = keDAO.getTatCaKeThuoc();
            for (Ke ke : entities) {
                result.add(mapEntityToDTO(ke));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay tat ca ke", e);
        }
        return result;
    }

    @Override
    public ArrayList<KeDTO> getTatCaKeHoatDong() {
        ArrayList<KeDTO> result = new ArrayList<>();
        try {
            ArrayList<Ke> entities = keDAO.getTatCaKeHoatDong();
            for (Ke ke : entities) {
                result.add(mapEntityToDTO(ke));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay ke hoat dong", e);
        }
        return result;
    }

    @Override
    public KeDTO getKeTheoMa(String ma) {
        try {
            Ke entity = keDAO.getKeTheoMa(ma);
            return entity == null ? null : mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay ke theo ma", e);
        }
    }

    @Override
    public KeDTO getKeTheoName(String name) {
        try {
            Ke entity = keDAO.getKeTheoName(name);
            return entity == null ? null : mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay ke theo ten", e);
        }
    }

    @Override
    public boolean isTenKeTrung(String tenKe, String maKeHienTai) {
        try {
            return keDAO.isTenKeTrung(tenKe, maKeHienTai);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi kiem tra ten ke trung", e);
        }
    }

    private KeDTO mapEntityToDTO(Ke entity) {
        if (entity == null) {
            return null;
        }
        return new KeDTO(entity.getMaKe(), entity.getTenKe(), entity.getLoaiKe(), entity.isDeleteAt());
    }

    private Ke mapDTOToEntity(KeDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Ke(dto.getMaKe(), dto.getTenKe(), dto.getLoaiKe(), dto.isDeleteAt());
    }
}
