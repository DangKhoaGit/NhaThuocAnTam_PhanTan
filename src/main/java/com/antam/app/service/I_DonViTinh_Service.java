package com.antam.app.service;

import com.antam.app.dao.I_DonViTinh_DAO;
import com.antam.app.dto.DonViTinhDTO;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_DonViTinh_Service {
    String getHashDVT();

    DonViTinhDTO getDVTTheoMaDVT(int ma);

    ArrayList<DonViTinhDTO> getTatCaDonViTinh();

    ArrayList<DonViTinhDTO> getAllDonViTinh();

    DonViTinhDTO getDVTTheoTen(String ten);

    DonViTinhDTO getDVTTheoMa(int ma);

    int themDonViTinh(DonViTinhDTO donViTinhDTO);

    int updateDonViTinh(DonViTinhDTO donViTinhDTO);

    int xoaDonViTinh(DonViTinhDTO donViTinhDTO);

    int khoiPhucDonViTinh(DonViTinhDTO dvt);
}
