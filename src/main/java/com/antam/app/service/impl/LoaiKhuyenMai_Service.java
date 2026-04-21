/*
 * @ (#) LoaiKhuyenMai_DAO.java   1.0 10/15/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.dao.I_LoaiKhuyenMai_DAO;
import com.antam.app.dao.impl.LoaiKhuyenMai_DAO;
import com.antam.app.dto.LoaiKhuyenMaiDTO;
import com.antam.app.entity.LoaiKhuyenMai;
import com.antam.app.service.I_LoaiKhuyenMai_Service;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/15/2025
 * version: 1.0
 */
public class LoaiKhuyenMai_Service implements I_LoaiKhuyenMai_Service {
    private final I_LoaiKhuyenMai_DAO loaiKhuyenMaiDAO;

    public LoaiKhuyenMai_Service() {
        this.loaiKhuyenMaiDAO = new LoaiKhuyenMai_DAO();
    }

    @Override
    public ArrayList<LoaiKhuyenMaiDTO> getAllLoaiKhuyenMai() {
        ArrayList<LoaiKhuyenMaiDTO> list = new ArrayList<>();
        for (LoaiKhuyenMai entity : loaiKhuyenMaiDAO.getAllLoaiKhuyenMai()) {
            list.add(new LoaiKhuyenMaiDTO(entity.getMaLKM(), entity.getTenLKM()));
        }
        return list;
    }
}
