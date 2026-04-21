/*
 * @ (#) DangDieuChe_Service.java   1.0 19/04/2026
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.dao.I_DangDieuChe_DAO;
import com.antam.app.dao.impl.DangDieuChe_DAO;
import com.antam.app.dto.DangDieuCheDTO;
import com.antam.app.entity.DangDieuChe;
import com.antam.app.service.I_DangDieuChe_Service;

import java.util.ArrayList;

/*
 * @description: Implementation cua I_DangDieuChe_Service
 * Theo chuan luong du lieu: Ghi UI→DTO→Service→Entity→DAO→DB
 *                         Doc DB→DAO→Entity→Service→DTO→UI
 * @author: Duong Nguyen, Pham Dang Khoa
 * @date: 19/04/2026
 * @version: 1.0
 */
public class DangDieuChe_Service implements I_DangDieuChe_Service {

    private final I_DangDieuChe_DAO dangDieuCheDAO = new DangDieuChe_DAO();

    @Override
    public boolean khoiPhucDangDieuChe(int maDDC) {
        try {
            return dangDieuCheDAO.khoiPhucDangDieuChe(maDDC);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi khoi phuc dang dieu che", e);
        }
    }

    @Override
    public boolean xoaDangDieuChe(int maDDC) {
        try {
            return dangDieuCheDAO.xoaDangDieuChe(maDDC);

        } catch (Exception e) {
            throw new RuntimeException("Loi khi xoa dang dieu che", e);
        }
    }

    @Override
    public boolean suaDangDieuChe(DangDieuCheDTO ddc) {
        try {
            return dangDieuCheDAO.suaDangDieuChe(mapDTOToEntity(ddc));
        } catch (Exception e) {
            throw new RuntimeException("Loi khi cap nhat dang dieu che", e);
        }
    }

    @Override
    public boolean themDDC(DangDieuCheDTO ddc) {
        try {
            return dangDieuCheDAO.themDDC(mapDTOToEntity(ddc));
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them dang dieu che", e);
        }
    }

    @Override
    public String taoMaDDCTuDong() {
        try {
            return dangDieuCheDAO.taoMaDDCTuDong();
        } catch (Exception e) {
            throw new RuntimeException("Loi khi tao ma dang dieu che tu dong", e);
        }
    }

    @Override
    public ArrayList<DangDieuCheDTO> getTatCaDangDieuChe() {
        ArrayList<DangDieuCheDTO> result = new ArrayList<>();
        try {
            ArrayList<DangDieuChe> entities = dangDieuCheDAO.getTatCaDangDieuChe();
            for (DangDieuChe entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay tat ca dang dieu che", e);
        }
        return result;
    }

    @Override
    public ArrayList<DangDieuCheDTO> getDangDieuCheHoatDong() {
        ArrayList<DangDieuCheDTO> result = new ArrayList<>();
        try {
            ArrayList<DangDieuChe> entities = dangDieuCheDAO.getDangDieuCheHoatDong();
            for (DangDieuChe entity : entities) {
                result.add(mapEntityToDTO(entity));
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay dang dieu che hoat dong", e);
        }
        return result;
    }

    @Override
    public DangDieuCheDTO getDDCTheoName(String name) {
        try {
            DangDieuChe entity = dangDieuCheDAO.getDDCTheoName(name);
            return entity == null ? null : mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi lay dang dieu che theo ten", e);
        }
    }

    private DangDieuCheDTO mapEntityToDTO(DangDieuChe entity) {
        if (entity == null) {
            return null;
        }
        return new DangDieuCheDTO(entity.getMaDDC(), entity.getTenDDC(), entity.isDeleteAt());
    }

    private DangDieuChe mapDTOToEntity(DangDieuCheDTO dto) {
        if (dto == null) {
            return null;
        }
        return new DangDieuChe(dto.getMaDDC(), dto.getTenDDC(), dto.isDeleteAt());
    }
}
