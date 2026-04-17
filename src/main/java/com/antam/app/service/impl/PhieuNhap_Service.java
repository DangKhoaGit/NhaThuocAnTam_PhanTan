package com.antam.app.service.impl;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.I_PhieuNhap_Service;
import com.antam.app.dto.NhanVienDTO;
import com.antam.app.dto.PhieuNhapDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PhieuNhap_Service implements I_PhieuNhap_Service {
    /* Duy- Huỷ phiếu nhập */
    @Override
    public boolean huyPhieuNhap(String maPhieuNhap){
        String sql = "UPDATE PhieuNhap SET DeleteAt = 1 WHERE MaPhieuNhap = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maPhieuNhap);
            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Duy- Cập nhật phiếu nhập */
    @Override
    public boolean suaPhieuNhap(PhieuNhapDTO pn){
        String sql = "UPDATE PhieuNhap SET NhaCungCap = ?, DiaChi = ?, LyDo = ? WHERE MaPhieuNhap = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, pn.getNhaCungCap());
            ps.setString(2, pn.getDiaChi());
            ps.setString(3, pn.getLyDo());
            ps.setString(4, pn.getMaPhieuNhap());
            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Duy- Cập nhật trạng thái phiếu nhập */
    @Override
    public boolean suaTrangThaiPhieuNhap(String maPhieuNhap){
        String sql = "UPDATE PhieuNhap SET DeleteAt = 0 WHERE MaPhieuNhap = ?";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maPhieuNhap);
            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Duy - Tạo mã phiêu nhập tự động */
    @Override
    public String taoMaPhieuNhapTuDong(){
        String sql = "SELECT TOP 1 MaPhieuNhap FROM PhieuNhap ORDER BY MaPhieuNhap DESC";
        String maPhieuNhapMoi = "";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String maPhieuNhap = rs.getString("MaPhieuNhap");
                int soThuTu = Integer.parseInt(maPhieuNhap.substring(2)) + 1;
                maPhieuNhapMoi = String.format("PN%06d", soThuTu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maPhieuNhapMoi;
    }

    /* Duy - Kiểm tra phiếu nhập tồn tại */
    @Override
    public boolean tonTaiMaPhieuNhap(String maPhieuNhap) {
        String sql = "SELECT COUNT(*) FROM PhieuNhap WHERE MaPhieuNhap = ?";
        try  {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, maPhieuNhap);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /* Duy- Thêm phiếu nhập */
    @Override
    public boolean themPhieuNhap(PhieuNhapDTO pn){
        String sql = "INSERT INTO PhieuNhap (MaPhieuNhap, NhaCungCap, NgayNhap, DiaChi, LyDo, MaNV, TongTien, DeleteAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, pn.getMaPhieuNhap());
            ps.setString(2, pn.getNhaCungCap());
            ps.setString(3, pn.getNgayNhap().toString());
            ps.setString(4, pn.getDiaChi());
            ps.setString(5, pn.getLyDo());
            ps.setString(6, pn.getMaNV().getMaNV().toString());
            ps.setDouble(7, pn.getTongTien());
            ps.setBoolean(8, pn.isDeleteAt());
            int kq = ps.executeUpdate();
            return kq > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Duy - Lấy danh sách phiếu nhập */
    @Override
    public ArrayList<PhieuNhapDTO> getDanhSachPhieuNhap(){
        ArrayList<PhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhap as P JOIN NhanVien as N on N.MaNV = P.MaNV ORDER BY P.MaPhieuNhap DESC";

        try{
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String maPhieuNhap = rs.getString("MaPhieuNhap");
                String nhaCungCap = rs.getString("NhaCungCap");
                LocalDate ngayNhap = rs.getDate("NgayNhap").toLocalDate();
                String diaChi = rs.getString("DiaChi");
                String maNhanVien = rs.getString("MaNV");
                String tenNhanVien = rs.getString("HoTen");
                String lyDo = rs.getString("LyDo");
                double tongTien = rs.getDouble("TongTien");
                boolean deleteAt = rs.getBoolean("DeleteAt");
                PhieuNhapDTO pn = new PhieuNhapDTO(maPhieuNhap, nhaCungCap, ngayNhap, diaChi, lyDo, new NhanVienDTO(maNhanVien, tenNhanVien), tongTien, deleteAt);
                list.add(pn);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /* Duy - Lấy danh sách phiếu nhập theo trạng thái */
    @Override
    public ArrayList<PhieuNhapDTO> getDanhSachPhieuNhapTheoTrangThai(Boolean isDeleted){
        ArrayList<PhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhap as P JOIN NhanVien as N on N.MaNV = P.MaNV WHERE P.DeleteAt = " + (isDeleted ? "1" : "0") + " ORDER BY P.MaPhieuNhap DESC";

        try{
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String maPhieuNhap = rs.getString("MaPhieuNhap");
                String nhaCungCap = rs.getString("NhaCungCap");
                LocalDate ngayNhap = rs.getDate("NgayNhap").toLocalDate();
                String diaChi = rs.getString("DiaChi");
                String maNhanVien = rs.getString("MaNV");
                String tenNhanVien = rs.getString("HoTen");
                String lyDo = rs.getString("LyDo");
                double tongTien = rs.getDouble("TongTien");
                boolean deleteAt = rs.getBoolean("DeleteAt");
                PhieuNhapDTO pn = new PhieuNhapDTO(maPhieuNhap, nhaCungCap, ngayNhap, diaChi, lyDo, new NhanVienDTO(maNhanVien, tenNhanVien), tongTien, deleteAt);
                list.add(pn);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}