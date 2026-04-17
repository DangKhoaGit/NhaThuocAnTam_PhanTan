package com.antam.app.service;

import com.antam.app.dto.ChiTietHoaDonDTO;

import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_ChiTietHoaDon_Service {
    List<ChiTietHoaDonDTO> getChiTietByMaHD(String maHD);

    ArrayList<ChiTietHoaDonDTO> getAllChiTietHoaDonTheoMaHD(String maHD);

    ArrayList<ChiTietHoaDonDTO> getAllChiTietHoaDonTheoMaHDConBan(String maHD);

    boolean xoaMemChiTietHoaDon(String maHD, int maCTT, String tinhTrang);

    boolean themChiTietHoaDon(ChiTietHoaDonDTO cthd);

    boolean themChiTietHoaDon1(ChiTietHoaDonDTO cthd);
}
