package com.antam.app.dao;

import com.antam.app.entity.LoThuoc;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_LoThuoc_DAO {
    boolean themChiTietThuoc(LoThuoc ctt);

    ArrayList<LoThuoc> getAllChiTietThuoc();

    ArrayList<LoThuoc> getAllCHiTietThuocTheoMaThuoc(String ma);

    LoThuoc getChiTietThuoc(int ma);

    boolean CapNhatSoLuongChiTietThuoc(int maCTT, int soLuong);

    ArrayList<LoThuoc> getChiTietThuocHanSuDungGiamDan(String maThuoc);

    int getTongTonKhoTheoMaThuoc(String maThuoc);

    ArrayList<LoThuoc> getAllChiTietThuocVoiMaChoCTPD(String maThuoc);
}
