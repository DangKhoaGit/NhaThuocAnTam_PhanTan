package com.antam.app.service;

import com.antam.app.dto.KhuyenMaiDTO;
import com.antam.app.dto.LoaiKhuyenMaiDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_KhuyenMai_Service {
    List<KhuyenMaiDTO> getAllKhuyenMaiConHieuLuc();

    ArrayList<KhuyenMaiDTO> getAllKhuyenMaiChuaXoa();

    ArrayList<KhuyenMaiDTO> getAllKhuyenMaiDaXoa();

    boolean khoiPhucKhuyenMai(String maKM);

    ArrayList<KhuyenMaiDTO> getAllKhuyenMai();

    KhuyenMaiDTO getKhuyenMaiTheoMa(String maKM);

    boolean themKhuyenMai(String maKM, String tenKM, LoaiKhuyenMaiDTO loaiKM, double so, LocalDate ngayBatDau, LocalDate ngayKetThuc, int soLuongToiDa);

    boolean capNhatKhuyenMai(KhuyenMaiDTO km);

    boolean xoaKhuyenMai(String maKM);
}
