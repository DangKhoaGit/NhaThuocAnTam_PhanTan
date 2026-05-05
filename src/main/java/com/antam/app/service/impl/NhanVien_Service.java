package com.antam.app.service.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 01/10/2025
 * @version: 1.0
 */

import com.antam.app.dao.impl.NhanVien_DAO;
import com.antam.app.entity.NhanVien;
import com.antam.app.service.I_NhanVien_Service;
import com.antam.app.dto.NhanVienDTO;

import java.util.List;

public class NhanVien_Service implements I_NhanVien_Service {

    public NhanVienDTO mapEntityToDTO(NhanVien entity) {
        if (entity == null) return null;
        return NhanVienDTO.builder()
                .MaNV(entity.getMaNV())
                .hoTen(entity.getHoTen())
                .soDienThoai(entity.getSoDienThoai())
                .email(entity.getEmail())
                .diaChi(entity.getDiaChi())
                .luongCoBan(entity.getLuongCoBan())
                .taiKhoan(entity.getTaiKhoan())
                .matKhau(entity.getMatKhau())
                .quanLi(entity.isQuanLi())
                .deleteAt(entity.isDeleteAt())
                .build();
    }

    public NhanVien mapDTOToEntity(NhanVienDTO dto) {
        if (dto == null) return null;

        return NhanVien.builder()
                .MaNV(dto.getMaNV())
                .hoTen(dto.getHoTen())
                .soDienThoai(dto.getSoDienThoai())
                .email(dto.getEmail())
                .diaChi(dto.getDiaChi())
                .luongCoBan(dto.getLuongCoBan())
                .taiKhoan(dto.getTaiKhoan())
                .matKhau(dto.getMatKhau())
                .quanLi(dto.isQuanLi())
                .deleteAt(dto.isDeleteAt())
                .build();
    }

    NhanVien_DAO nhanVienDao = new NhanVien_DAO();

    @Override
    public boolean themNhanVien(NhanVienDTO nv) {
        NhanVien e = mapDTOToEntity(nv);
        return nhanVienDao.themNhanVien(e);
    }

    @Override
    public boolean updateNhanVienTrongDBS(NhanVienDTO nv) {
        NhanVien e = mapDTOToEntity(nv);
        return nhanVienDao.updateNhanVienTrongDBS(e);
    }

    @Override
    public boolean xoaNhanVienTrongDBS(String manv) {
        return nhanVienDao.xoaNhanVienTrongDBS(manv);
    }

    @Override
    public String getMaxHashNhanVien() {
        return nhanVienDao.getMaxHashNhanVien();
    }

    @Override
    public boolean khoiPhucNhanVien(String maNV) {
        return  nhanVienDao.khoiPhucNhanVien(maNV);
    }

    @Override
    public NhanVienDTO findNhanVienVoiMa(String maVN) {
        NhanVien nv = nhanVienDao.getNhanVien(maVN);
        return mapEntityToDTO(nv);
    }

    @Override
    public NhanVienDTO getNhanVienTaiKhoan(String id) {
        NhanVien nv = nhanVienDao.getNhanVienTaiKhoan(id);
        return mapEntityToDTO(nv);
    }

    @Override
    public NhanVienDTO getNhanVien(String id) {
        NhanVien nv = nhanVienDao.getNhanVien(id);
        return mapEntityToDTO(nv);
    }

    @Override
    public List<NhanVienDTO> getAllNhanVien() {
        List<NhanVien> nhanViens = nhanVienDao.getAllNhanVien();
        return nhanViens.stream()
                .map(this::mapEntityToDTO)
                .toList();
    }
}
