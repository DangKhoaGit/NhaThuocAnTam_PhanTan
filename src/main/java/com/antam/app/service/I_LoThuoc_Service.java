package com.antam.app.service;

import com.antam.app.dto.LoThuocDTO;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_LoThuoc_Service {
    boolean themChiTietThuoc(LoThuocDTO ctt);

    ArrayList<LoThuocDTO> getAllChiTietThuoc();

    ArrayList<LoThuocDTO> getAllCHiTietThuocTheoMaThuoc(String ma);

    LoThuocDTO getChiTietThuoc(int ma);

    boolean CapNhatSoLuongChiTietThuoc(int maCTT, int soLuong);

    ArrayList<LoThuocDTO> getChiTietThuocHanSuDungGiamDan(String maThuoc);

    int getTongTonKhoTheoMaThuoc(String maThuoc);

    ArrayList<LoThuocDTO> getAllChiTietThuocVoiMaChoCTPD(String maThuoc);
}
