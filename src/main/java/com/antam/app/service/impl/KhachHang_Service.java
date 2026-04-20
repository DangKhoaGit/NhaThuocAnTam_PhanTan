/*
 * @ (#) KhachHang_Service.java   1.0 1/10/25
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.antam.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_KhachHang_DAO;
import com.antam.app.dao.impl.KhachHang_DAO;
import com.antam.app.dto.KhachHangDTO;
import com.antam.app.entity.KhachHang;
import com.antam.app.service.I_KhachHang_Service;

/*
 * @description: Service layer for KhachHang - handles business logic and DTO/Entity conversion
 * @author: Tran Tuan Hung
 * @date: 1/10/25
 * @version: 2.0 (refactored according to n-layer architecture)
 */
public class KhachHang_Service implements I_KhachHang_Service {
    private final I_KhachHang_DAO khachHangDAO = new KhachHang_DAO();

    // ========== CONVERTER METHODS ==========
    /**
     * Chuyển đổi Entity → DTO
     */
    public KhachHangDTO mapEntityToDTO(KhachHang entity) {
        if (entity == null) return null;

        return KhachHangDTO.builder()
                .MaKH(entity.getMaKH())
                .tenKH(entity.getTenKH())
                .soDienThoai(entity.getSoDienThoai())
                .deleteAt(entity.isDeleteAt())
                .tongChiTieu(entity.getTongChiTieu())
                .soDonHang(entity.getSoDonHang())
                .ngayMuaGanNhat(entity.getNgayMuaGanNhat())
                .build();
    }

    /**
     * Chuyển đổi DTO → Entity
     */
    public KhachHang mapDTOToEntity(KhachHangDTO dto) {
        if (dto == null) return null;

        return KhachHang.builder()
                .MaKH(dto.getMaKH())
                .tenKH(dto.getTenKH())
                .soDienThoai(dto.getSoDienThoai())
                .deleteAt(dto.isDeleteAt())
                .tongChiTieu(dto.getTongChiTieu())
                .soDonHang(dto.getSoDonHang())
                .ngayMuaGanNhat(dto.getNgayMuaGanNhat())
                .build();
    }

    // ========== DATA ACCESS METHODS ==========
    /**
     * Lấy tất cả khách hàng (không có thống kê)
     */
    @Override
    public ArrayList<KhachHangDTO> getAllKhachHang() {
        try {
            ConnectDB.getInstance().connect();
            ArrayList<KhachHang> entities = khachHangDAO.getAllKhachHang();
            ArrayList<KhachHangDTO> dtos = new ArrayList<>();

            for (KhachHang entity : entities) {
                dtos.add(mapEntityToDTO(entity));
            }
            return dtos;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy danh sách khách hàng", e);
        }
    }

    /**
     * Lấy tất cả khách hàng với thông tin thống kê từ DAO
     */
    @Override
    public List<KhachHangDTO> loadKhachHangWithStats() {
        try {
            ConnectDB.getInstance().connect();
            List<KhachHang> entities = khachHangDAO.getAllKhachHangWithStats();
            List<KhachHangDTO> dtos = new ArrayList<>();

            for (KhachHang entity : entities) {
                dtos.add(mapEntityToDTO(entity));
            }
            return dtos;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy danh sách khách hàng với thống kê", e);
        }
    }

    /**
     * Tìm khách hàng theo mã
     */
    @Override
    public KhachHangDTO getKhachHangTheoMa(String maKH) {
        try {
            ConnectDB.getInstance().connect();
            KhachHang entity = khachHangDAO.getKhachHangTheoMa(maKH);
            return mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy khách hàng theo mã: " + maKH, e);
        }
    }

    /**
     * Tìm khách hàng theo số điện thoại
     */
    @Override
    public KhachHangDTO getKhachHangTheoSoDienThoai(String soDienThoai) {
        try {
            ConnectDB.getInstance().connect();
            KhachHang entity = khachHangDAO.getKhachHangTheoSoDienThoai(soDienThoai);
            return mapEntityToDTO(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy khách hàng theo số điện thoại: " + soDienThoai, e);
        }
    }

    /**
     * Tìm khách hàng theo tên (LIKE search)
     */
    @Override
    public List<KhachHangDTO> searchKhachHangByName(String tenKH) {
        try {
            ConnectDB.getInstance().connect();
            List<KhachHang> entities = khachHangDAO.searchKhachHangByName(tenKH);
            List<KhachHangDTO> dtos = new ArrayList<>();

            for (KhachHang entity : entities) {
                dtos.add(mapEntityToDTO(entity));
            }
            return dtos;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tìm kiếm khách hàng theo tên: " + tenKH, e);
        }
    }

    // ========== MUTATION METHODS ==========
    /**
     * Thêm khách hàng mới
     */
    @Override
    public boolean insertKhachHang(KhachHangDTO kh) {
        try {
            ConnectDB.getInstance().connect();
            // Validate DTO
            if (kh == null || kh.getMaKH() == null || kh.getMaKH().isEmpty()) {
                throw new IllegalArgumentException("Mã khách hàng không được để trống");
            }

            // Convert DTO → Entity
            KhachHang entity = mapDTOToEntity(kh);

            // Call DAO
            return khachHangDAO.insertKhachHang(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi thêm khách hàng", e);
        }
    }

    /**
     * Cập nhật khách hàng
     */
    @Override
    public boolean updateKhachHang(KhachHangDTO kh) {
        try {
            ConnectDB.getInstance().connect();
            // Validate DTO
            if (kh == null || kh.getMaKH() == null || kh.getMaKH().isEmpty()) {
                throw new IllegalArgumentException("Mã khách hàng không được để trống");
            }

            // Convert DTO → Entity
            KhachHang entity = mapDTOToEntity(kh);

            // Call DAO
            return khachHangDAO.updateKhachHang(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi cập nhật khách hàng", e);
        }
    }

    // ========== STATISTICS METHODS ==========
    /**
     * Lấy tổng số khách hàng
     */
    @Override
    public int getTongKhachHang() {
        try {
            ConnectDB.getInstance().connect();
            return khachHangDAO.getTongKhachHang();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy tổng số khách hàng", e);
        }
    }

    /**
     * Lấy tổng số khách hàng VIP
     */
    @Override
    public int getTongKhachHangVIP() {
        try {
            ConnectDB.getInstance().connect();
            return khachHangDAO.getTongKhachHangVIP();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy tổng số khách hàng VIP", e);
        }
    }

    /**
     * Lấy tổng số đơn hàng
     */
    @Override
    public int getTongDonHang() {
        try {
            ConnectDB.getInstance().connect();
            return khachHangDAO.getTongDonHang();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy tổng số đơn hàng", e);
        }
    }

    /**
     * Lấy tổng doanh thu
     */
    @Override
    public double getTongDoanhThu() {
        try {
            ConnectDB.getInstance().connect();
            return khachHangDAO.getTongDoanhThu();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy tổng doanh thu", e);
        }
    }

    /**
     * Lấy mã khách hàng tiếp theo
     */
    @Override
    public int getMaxHash() {
        return khachHangDAO.getMaxHash();
    }
}
