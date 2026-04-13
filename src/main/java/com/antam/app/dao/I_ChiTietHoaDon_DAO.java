package com.antam.app.dao;

import com.antam.app.entity.ChiTietHoaDon;

import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_ChiTietHoaDon_DAO {
    List<ChiTietHoaDon> getChiTietByMaHD(String maHD);

    ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHD(String maHD);

    ArrayList<ChiTietHoaDon> getAllChiTietHoaDonTheoMaHDConBan(String maHD);

    boolean xoaMemChiTietHoaDon(String maHD, int maCTT, String tinhTrang);

    boolean themChiTietHoaDon(ChiTietHoaDon cthd);

    boolean themChiTietHoaDon1(ChiTietHoaDon cthd);
}
