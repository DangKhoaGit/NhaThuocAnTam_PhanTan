/*
 * @ (#) HoaDon_DAO.java   1.0 10/3/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_HoaDon_Service;
import com.antam.app.dto.HoaDonDTO;
import com.antam.app.dto.KhachHangDTO;
import com.antam.app.dto.KhuyenMaiDTO;
import com.antam.app.dto.NhanVienDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * @description
 * @author: Duong Nguyen
 * @date: 10/3/2025
 * version: 1.0
 */
public class HoaDon_Service implements I_HoaDon_Service {
    // duong
    /**
     * Lấy tất cả hóa đơn từ database
     * @return danh sách hóa đơn
     */
    @Override
    public ArrayList<HoaDonDTO> getAllHoaDon() {
        ArrayList<HoaDonDTO> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";

        // Danh sách lưu trữ dữ liệu tạm từ ResultSet
        class TempHoaDonData {
            final String maHD, maKH, maKM, maNV;
            final LocalDate ngayTao;
            final double tongTien;
            final boolean deleteAt;

            TempHoaDonData(String maHD, LocalDate ngayTao, String maNV, String maKH, String maKM, double tongTien, boolean deleteAt) {
                this.maHD = maHD;
                this.ngayTao = ngayTao;
                this.maNV = maNV;
                this.maKH = maKH;
                this.maKM = maKM;
                this.tongTien = tongTien;
                this.deleteAt = deleteAt;
            }
        }

        ArrayList<TempHoaDonData> tempList = new ArrayList<>();

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            // Bước 1: Đọc tất cả dữ liệu từ ResultSet trước
            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
                while (rs.next()) {
                    String maHD = rs.getString("MaHD");
                    LocalDate ngayTaoDate = rs.getDate("NgayTao").toLocalDate();
                    String maKH = rs.getString("MaKH");
                    String maNV = rs.getString("MaNV");
                    String maKM = rs.getString("MaKM");
                    double tongTien = rs.getDouble("TongTien");
                    boolean deleteAt = rs.getBoolean("DeleteAt");

                    tempList.add(new TempHoaDonData(maHD, ngayTaoDate, maNV, maKH, maKM, tongTien, deleteAt));
                }
            }

            // Bước 2: Sau khi đóng ResultSet, gọi các DAO khác để lấy thông tin chi tiết
            NhanVien_Service nhanVienDAO = new NhanVien_Service();
            KhachHang_Service khachHangDAO = new KhachHang_Service();
            KhuyenMai_Service khuyenMaiDAO = new KhuyenMai_Service();

            for (TempHoaDonData temp : tempList) {
                // Lấy đầy đủ thông tin nhân viên từ NhanVien_DAO
                NhanVienDTO nhanVienDTO = nhanVienDAO.findNhanVienVoiMa(temp.maNV);
                if (nhanVienDTO == null) {
                    nhanVienDTO = new NhanVienDTO(temp.maNV);
                }

                // Lấy đầy đủ thông tin khách hàng từ KhachHang_DAO
                KhachHangDTO khachHangDTO = khachHangDAO.getKhachHangTheoMa(temp.maKH);
                if (khachHangDTO == null) {
                    khachHangDTO = new KhachHangDTO(temp.maKH);
                }

                // Lấy đầy đủ thông tin khuyến mãi từ KhuyenMai_DAO
                KhuyenMaiDTO khuyenMaiDTO = null;
                if (temp.maKM != null && !temp.maKM.trim().isEmpty()) {
                    khuyenMaiDTO = khuyenMaiDAO.getKhuyenMaiTheoMa(temp.maKM);
                }

                HoaDonDTO hoaDonDTO = new HoaDonDTO(temp.maHD, temp.ngayTao, nhanVienDTO, khachHangDTO, khuyenMaiDTO, temp.tongTien, temp.deleteAt);
                hoaDonDTO.setTongTien(temp.tongTien);
                dsHoaDon.add(hoaDonDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dsHoaDon;
    }

    /**
     * Thêm hóa đơn vào database
     * @param maHD hóa đơn cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */
    @Override
    public HoaDonDTO getHoaDonTheoMa(String maHD){
        HoaDonDTO hd = new HoaDonDTO(maHD);
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHD);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                hd.setMaKH(new KhachHangDTO(rs.getString("MaKH")));
                hd.setMaNV(new NhanVienDTO(rs.getString("MaNV")));
                hd.setNgayTao(rs.getDate("NgayTao").toLocalDate());
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setMaKM(rs.getString("MaKM") != null ? new KhuyenMaiDTO(rs.getString("MaKM")) : null);
                hd.setDeleteAt(rs.getBoolean("DeleteAt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hd;
    }

    /**
     * Cập nhật tổng tiền của hóa đơn
     * @param maHD mã hóa đơn cần cập nhật
     * @param tongTien tổng tiền mới
     * @return true nếu cập nhật thành công, false nếu cập nhật thất bại
     */
    @Override
    public boolean CapNhatTongTienHoaDon(String maHD, double tongTien){
        String sql = "UPDATE HoaDon SET TongTien = ? WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setDouble(1, tongTien);
            statement.setString(2, maHD);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Thêm hóa đơn vào database
     * @param maHD hóa đơn cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */
    @Override
    public boolean xoaMemHoaDon(String maHD){
        String sql = "UPDATE HoaDon SET DeleteAt = 1 WHERE MaHD = ?";
        Connection con = ConnectDB.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maHD);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // duong

    //hung
    /**
     * Tìm kiếm hóa đơn theo mã hóa đơn (MaHD) - tìm tương đối (LIKE).
     * @param maHd Mã hóa đơn cần tìm (có thể là một phần mã)
     * @return Danh sách hóa đơn có mã chứa chuỗi nhập vào
     */
    @Override
    public ArrayList<HoaDonDTO> searchHoaDonByMaHd(String maHd) {
        ArrayList<HoaDonDTO> dsHoaDon = new ArrayList<>();
        if (maHd == null || maHd.trim().isEmpty()) {
            return dsHoaDon; // Trả về rỗng nếu không truyền mã
        }
        String sql = "SELECT * FROM HoaDon WHERE MaHD LIKE ? AND DeleteAt = 0";

        // Danh sách lưu trữ dữ liệu tạm từ ResultSet
        class TempHoaDonData {
            final String maHD, maKH, maKM, maNV;
            final LocalDate ngayTao;
            final double tongTien;
            final boolean deleteAt;

            TempHoaDonData(String maHD, LocalDate ngayTao, String maNV, String maKH, String maKM, double tongTien, boolean deleteAt) {
                this.maHD = maHD;
                this.ngayTao = ngayTao;
                this.maNV = maNV;
                this.maKH = maKH;
                this.maKM = maKM;
                this.tongTien = tongTien;
                this.deleteAt = deleteAt;
            }
        }

        ArrayList<TempHoaDonData> tempList = new ArrayList<>();

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            // Bước 1: Đọc tất cả dữ liệu từ ResultSet trước
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + maHd + "%"); // Tìm tương đối
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String maHD = rs.getString("MaHD");
                        LocalDate ngayTaoDate = rs.getDate("NgayTao").toLocalDate();
                        String maKH = rs.getString("MaKH");
                        String maNVRes = rs.getString("MaNV");
                        String maKM = rs.getString("MaKM");
                        double tongTien = rs.getDouble("TongTien");
                        boolean deleteAt = rs.getBoolean("DeleteAt");

                        tempList.add(new TempHoaDonData(maHD, ngayTaoDate, maNVRes, maKH, maKM, tongTien, deleteAt));
                    }
                }
            }

            // Bước 2: Sau khi đóng ResultSet, gọi các DAO khác để lấy thông tin chi tiết
            NhanVien_Service nhanVienDAO = new NhanVien_Service();
            KhachHang_Service khachHangDAO = new KhachHang_Service();
            KhuyenMai_Service khuyenMaiDAO = new KhuyenMai_Service();

            for (TempHoaDonData temp : tempList) {
                // Lấy đầy đủ thông tin nhân viên từ NhanVien_DAO
                NhanVienDTO nhanVienDTO = nhanVienDAO.findNhanVienVoiMa(temp.maNV);
                if (nhanVienDTO == null) {
                    nhanVienDTO = new NhanVienDTO(temp.maNV);
                }

                // Lấy đầy đủ thông tin khách hàng từ KhachHang_DAO
                KhachHangDTO khachHangDTO = khachHangDAO.getKhachHangTheoMa(temp.maKH);
                if (khachHangDTO == null) {
                    khachHangDTO = new KhachHangDTO(temp.maKH);
                }

                // Lấy đầy đủ thông tin khuyến mãi từ KhuyenMai_DAO
                KhuyenMaiDTO khuyenMaiDTO = null;
                if (temp.maKM != null && !temp.maKM.trim().isEmpty()) {
                    khuyenMaiDTO = khuyenMaiDAO.getKhuyenMaiTheoMa(temp.maKM);
                }

                HoaDonDTO hoaDonDTO = new HoaDonDTO(temp.maHD, temp.ngayTao, nhanVienDTO, khachHangDTO, khuyenMaiDTO, temp.tongTien, temp.deleteAt);
                hoaDonDTO.setTongTien(temp.tongTien);
                dsHoaDon.add(hoaDonDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dsHoaDon;
    }

    /**
     * Lọc hóa đơn theo trạng thái: "Tất cả", "Hoạt động", "Đã huỷ"
     */
    @Override
    public ArrayList<HoaDonDTO> searchHoaDonByStatus(String status) {
        ArrayList<HoaDonDTO> dsHoaDon = new ArrayList<>();
        String sql;
        if ("Tất cả".equals(status)) {
            sql = "SELECT * FROM HoaDon";
        } else if ("Hoạt động".equals(status)) {
            sql = "SELECT * FROM HoaDon WHERE DeleteAt = 0";
        } else if ("Đã huỷ".equals(status)) {
            sql = "SELECT * FROM HoaDon WHERE DeleteAt = 1";
        } else {
            sql = "SELECT * FROM HoaDon";
        }

        // Danh sách lưu trữ dữ liệu tạm từ ResultSet
        class TempHoaDonData {
            final String maHD, maKH, maKM, maNV;
            final LocalDate ngayTao;
            final double tongTien;
            final boolean deleteAt;

            TempHoaDonData(String maHD, LocalDate ngayTao, String maNV, String maKH, String maKM, double tongTien, boolean deleteAt) {
                this.maHD = maHD;
                this.ngayTao = ngayTao;
                this.maNV = maNV;
                this.maKH = maKH;
                this.maKM = maKM;
                this.tongTien = tongTien;
                this.deleteAt = deleteAt;
            }
        }

        ArrayList<TempHoaDonData> tempList = new ArrayList<>();

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            // Bước 1: Đọc tất cả dữ liệu từ ResultSet trước
            try (Statement state = con.createStatement(); ResultSet rs = state.executeQuery(sql)) {
                while (rs.next()) {
                    String maHD = rs.getString("MaHD");
                    LocalDate ngayTaoDate = rs.getDate("NgayTao").toLocalDate();
                    String maKH = rs.getString("MaKH");
                    String maNV = rs.getString("MaNV");
                    String maKM = rs.getString("MaKM");
                    double tongTien = rs.getDouble("TongTien");
                    boolean deleteAt = rs.getBoolean("DeleteAt");

                    tempList.add(new TempHoaDonData(maHD, ngayTaoDate, maNV, maKH, maKM, tongTien, deleteAt));
                }
            }

            // Bước 2: Sau khi đóng ResultSet, gọi các DAO khác để lấy thông tin chi tiết
            NhanVien_Service nhanVienDAO = new NhanVien_Service();
            KhachHang_Service khachHangDAO = new KhachHang_Service();
            KhuyenMai_Service khuyenMaiDAO = new KhuyenMai_Service();

            for (TempHoaDonData temp : tempList) {
                // Lấy đầy đủ thông tin nhân viên từ NhanVien_DAO
                NhanVienDTO nhanVienDTO = nhanVienDAO.findNhanVienVoiMa(temp.maNV);
                if (nhanVienDTO == null) {
                    nhanVienDTO = new NhanVienDTO(temp.maNV);
                }

                // Lấy đầy đủ thông tin khách hàng từ KhachHang_DAO
                KhachHangDTO khachHangDTO = khachHangDAO.getKhachHangTheoMa(temp.maKH);
                if (khachHangDTO == null) {
                    khachHangDTO = new KhachHangDTO(temp.maKH);
                }

                // Lấy đầy đủ thông tin khuyến mãi từ KhuyenMai_DAO
                KhuyenMaiDTO khuyenMaiDTO = null;
                if (temp.maKM != null && !temp.maKM.trim().isEmpty()) {
                    khuyenMaiDTO = khuyenMaiDAO.getKhuyenMaiTheoMa(temp.maKM);
                }

                HoaDonDTO hoaDonDTO = new HoaDonDTO(temp.maHD, temp.ngayTao, nhanVienDTO, khachHangDTO, khuyenMaiDTO, temp.tongTien, temp.deleteAt);
                hoaDonDTO.setTongTien(temp.tongTien);
                dsHoaDon.add(hoaDonDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsHoaDon;
    }

    /**
     * Tìm kiếm hóa đơn theo mã nhân viên
     * @param maNV mã nhân viên
     * @return danh sách hóa đơn của nhân viên đó
     */
    @Override
    public ArrayList<HoaDonDTO> searchHoaDonByMaNV(String maNV) {
        ArrayList<HoaDonDTO> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MaNV = ?";

        // Danh sách lưu trữ dữ liệu tạm từ ResultSet
        class TempHoaDonData {
            String maHD, maKH, maKM, maNV;
            LocalDate ngayTao;
            double tongTien;
            boolean deleteAt;

            TempHoaDonData(String maHD, LocalDate ngayTao, String maNV, String maKH, String maKM, double tongTien, boolean deleteAt) {
                this.maHD = maHD;
                this.ngayTao = ngayTao;
                this.maNV = maNV;
                this.maKH = maKH;
                this.maKM = maKM;
                this.tongTien = tongTien;
                this.deleteAt = deleteAt;
            }
        }

        ArrayList<TempHoaDonData> tempList = new ArrayList<>();

        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }

            // Bước 1: Đọc tất cả dữ liệu từ ResultSet trước
            try (PreparedStatement state = con.prepareStatement(sql)) {
                state.setString(1, maNV);
                try (ResultSet rs = state.executeQuery()) {
                    while (rs.next()) {
                        String maHD = rs.getString("MaHD");
                        LocalDate ngayTaoDate = rs.getDate("NgayTao").toLocalDate();
                        String maKH = rs.getString("MaKH");
                        String maKM = rs.getString("MaKM");
                        double tongTien = rs.getDouble("TongTien");
                        boolean deleteAt = rs.getBoolean("DeleteAt");

                        tempList.add(new TempHoaDonData(maHD, ngayTaoDate, maNV, maKH, maKM, tongTien, deleteAt));
                    }
                }
            }

            // Bước 2: Sau khi đóng ResultSet, gọi các DAO khác để lấy thông tin chi tiết
            NhanVien_Service nhanVienDAO = new NhanVien_Service();
            KhachHang_Service khachHangDAO = new KhachHang_Service();
            KhuyenMai_Service khuyenMaiDAO = new KhuyenMai_Service();

            for (TempHoaDonData temp : tempList) {
                // Lấy đầy đủ thông tin nhân viên từ NhanVien_DAO
                NhanVienDTO nhanVienDTO = nhanVienDAO.findNhanVienVoiMa(temp.maNV);
                if (nhanVienDTO == null) {
                    nhanVienDTO = new NhanVienDTO(temp.maNV);
                }

                // Lấy đầy đủ thông tin khách hàng từ KhachHang_DAO
                KhachHangDTO khachHangDTO = khachHangDAO.getKhachHangTheoMa(temp.maKH);
                if (khachHangDTO == null) {
                    khachHangDTO = new KhachHangDTO(temp.maKH);
                }

                // Lấy đầy đủ thông tin khuyến mãi từ KhuyenMai_DAO
                KhuyenMaiDTO khuyenMaiDTO = null;
                if (temp.maKM != null && !temp.maKM.trim().isEmpty()) {
                    khuyenMaiDTO = khuyenMaiDAO.getKhuyenMaiTheoMa(temp.maKM);
                }

                HoaDonDTO hoaDonDTO = new HoaDonDTO(temp.maHD, temp.ngayTao, nhanVienDTO, khachHangDTO, khuyenMaiDTO, temp.tongTien, temp.deleteAt);
                hoaDonDTO.setTongTien(temp.tongTien);
                dsHoaDon.add(hoaDonDTO);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dsHoaDon;
    }

    /**
     * Thêm hóa đơn vào database
     * @param hoaDonDTO hóa đơn cần thêm
     * @return true nếu thêm thành công, false nếu thêm thất bại
     */
    @Override
    public boolean insertHoaDon(HoaDonDTO hoaDonDTO) {
        String sql = "INSERT INTO HoaDon (MaHD, NgayTao, MaNV, MaKH, MaKM, TongTien, DeleteAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getConnection();
            if (con == null || con.isClosed()) {
                ConnectDB.getInstance().connect();
                con = ConnectDB.getConnection();
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, hoaDonDTO.getMaHD());
                ps.setDate(2, Date.valueOf(hoaDonDTO.getNgayTao()));
                ps.setString(3, hoaDonDTO.getMaNV() != null ? hoaDonDTO.getMaNV().getMaNV() : null);
                ps.setString(4, hoaDonDTO.getMaKH() != null ? hoaDonDTO.getMaKH().getMaKH() : null);
                if (hoaDonDTO.getMaKM() != null) {
                    ps.setString(5, hoaDonDTO.getMaKM().getMaKM());
                } else {
                    ps.setNull(5, Types.VARCHAR);
                }
                ps.setDouble(6, hoaDonDTO.getTongTien());
                ps.setBoolean(7, hoaDonDTO.isDeleteAt());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Đếm số hóa đơn đã áp dụng khuyến mãi với mã khuyến mãi cụ thể
     * @param maKM mã khuyến mãi
     * @return số lượng hóa đơn đã áp dụng khuyến mãi
     */
    @Override
    public int soHoaDonDaCoKhuyenMaiVoiMa(String maKM){
        int count = 0;
        String sql = "SELECT COUNT(*) AS SoLuong FROM HoaDon WHERE MaKM = ? AND DeleteAt = 0";
        Connection con = ConnectDB.getConnection();
        try{
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, maKM);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                count = rs.getInt("SoLuong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Lấy mã hóa đơn lớn nhất hiện có trong database
     * @return string - phần số của mã hóa đơn lớn nhất
     */
    @Override
    public String getMaxHash(){
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "select top 1 MaHD from HoaDon order by MaHD desc";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet kq = state.executeQuery();
            while (kq.next()){
                return kq.getString(1).substring(3,5);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}

