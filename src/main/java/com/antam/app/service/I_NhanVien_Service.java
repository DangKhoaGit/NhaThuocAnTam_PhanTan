package com.antam.app.service;

import com.antam.app.dto.NhanVienDTO;
import com.antam.app.entity.NhanVien;

import java.util.ArrayList;
import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_NhanVien_Service {

    boolean themNhanVien(NhanVienDTO nv);

    boolean updateNhanVienTrongDBS(NhanVienDTO nv);

    boolean xoaNhanVienTrongDBS(String manv);

    String getMaxHashNhanVien();

    boolean khoiPhucNhanVien(String maNV);

    NhanVienDTO findNhanVienVoiMa(String maVN);

    NhanVienDTO getNhanVienTaiKhoan(String id);

    // duong
    // hung
    NhanVienDTO getNhanVien(String id);

    List<NhanVienDTO> getAllNhanVien();
}
