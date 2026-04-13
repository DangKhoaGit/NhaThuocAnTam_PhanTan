package com.antam.app.dao;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.impl.KhachHang_DAO;
import com.antam.app.dao.impl.KhuyenMai_DAO;
import com.antam.app.dao.impl.NhanVien_DAO;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.KhachHang;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_HoaDon_DAO {
    /**
     * Lấy danh sách hóa đơn theo mã khách hàng
     *
     * @param maKH mã khách hàng
     * @return danh sách hóa đơn của khách hàng đó
     */
    static ArrayList<HoaDon> getHoaDonByMaKH(String maKH) {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE MaKH = ? AND DeleteAt = 0 ORDER BY NgayTao DESC";

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
                ps.setString(1, maKH);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String maHD = rs.getString("MaHD");
                        LocalDate ngayTaoDate = rs.getDate("NgayTao").toLocalDate();
                        String maNV = rs.getString("MaNV");
                        String maKM = rs.getString("MaKM");
                        double tongTien = rs.getDouble("TongTien");
                        boolean deleteAt = rs.getBoolean("DeleteAt");

                        tempList.add(new TempHoaDonData(maHD, ngayTaoDate, maNV, maKH, maKM, tongTien, deleteAt));
                    }
                }
            }

            // Bước 2: Sau khi đóng ResultSet, gọi các DAO khác để lấy thông tin chi tiết
            NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
            KhachHang_DAO khachHangDAO = new KhachHang_DAO();
            KhuyenMai_DAO khuyenMaiDAO = new KhuyenMai_DAO();

            for (TempHoaDonData temp : tempList) {
                // Lấy đầy đủ thông tin nhân viên từ NhanVien_DAO
                NhanVien nhanVien = nhanVienDAO.findNhanVienVoiMa(temp.maNV);
                if (nhanVien == null) {
                    nhanVien = new NhanVien(temp.maNV);
                }

                // Lấy đầy đủ thông tin khách hàng từ KhachHang_DAO
                KhachHang khachHang = khachHangDAO.getKhachHangTheoMa(temp.maKH);
                if (khachHang == null) {
                    khachHang = new KhachHang(temp.maKH);
                }

                // Lấy đầy đủ thông tin khuyến mãi từ KhuyenMai_DAO
                KhuyenMai khuyenMai = null;
                if (temp.maKM != null && !temp.maKM.trim().isEmpty()) {
                    khuyenMai = khuyenMaiDAO.getKhuyenMaiTheoMa(temp.maKM);
                }

                HoaDon hoaDon = new HoaDon(temp.maHD, temp.ngayTao, nhanVien, khachHang, khuyenMai, temp.tongTien, temp.deleteAt);
                hoaDon.setTongTien(temp.tongTien);
                dsHoaDon.add(hoaDon);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy hóa đơn theo mã khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return dsHoaDon;
    }

    ArrayList<HoaDon> getAllHoaDon();

    HoaDon getHoaDonTheoMa(String maHD);

    boolean CapNhatTongTienHoaDon(String maHD, double tongTien);

    boolean xoaMemHoaDon(String maHD);

    ArrayList<HoaDon> searchHoaDonByMaHd(String maHd);

    ArrayList<HoaDon> searchHoaDonByStatus(String status);

    ArrayList<HoaDon> searchHoaDonByMaNV(String maNV);

    boolean insertHoaDon(HoaDon hoaDon);

    int soHoaDonDaCoKhuyenMaiVoiMa(String maKM);

    String getMaxHash();
}
