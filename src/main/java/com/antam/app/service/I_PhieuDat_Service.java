package com.antam.app.service;

import com.antam.app.connect.ConnectDB;
import com.antam.app.service.impl.PhieuDat_Service;
import com.antam.app.dto.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_PhieuDat_Service {
    /**
     * Lấy toàn bộ danh sách phiếu đặt thuốc với thông tin bao gồm
     * mã, ngày tạo, đã thanh toán hay chưa, mã khách, mã nhân viên tạo
     * thành tiền của phiếu đặt
     *
     * @return Array[PhieuDatThuoc]
     */
    static ArrayList<PhieuDatThuocDTO> getAllPhieuDatThuocFromDBS() {
        ArrayList<PhieuDatThuocDTO> ds = new ArrayList<>();

        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Connection con = ConnectDB.getConnection();

        String sql = "SELECT MaPDT, NgayTao, IsThanhToan, MaKH, MaNV, MaKM, TongTien FROM PhieuDatThuoc where DeleteAt =0";

        try (
                PreparedStatement state = con.prepareStatement(sql);
                ResultSet kq = state.executeQuery()
        ) {
            ArrayList<NhanVienDTO> nvList = I_NhanVien_Service.getDsNhanVienformDBS();
            ArrayList<KhachHangDTO> khList = I_KhachHang_Service.loadBanFromDB();
            List<KhuyenMaiDTO> kmList = I_KhuyenMai_Service.getAllKhuyenMaiConHieuLuc();

            Map<String, NhanVienDTO> mapNV = nvList.stream()
                    .collect(Collectors.toMap(NhanVienDTO::getMaNV, x -> x));

            Map<String, KhachHangDTO> mapKH = khList.stream()
                    .collect(Collectors.toMap(KhachHangDTO::getMaKH, x -> x));

            Map<String, KhuyenMaiDTO> mapKM = kmList.stream()
                    .collect(Collectors.toMap(KhuyenMaiDTO::getMaKM, x -> x));

            while (kq.next()) {
                String ma = kq.getString("MaPDT");
                LocalDate ngay = kq.getDate("NgayTao").toLocalDate();
                boolean isThanhToan = kq.getBoolean("IsThanhToan");
                String maKhach = kq.getString("MaKH");
                String maNV = kq.getString("MaNV");
                String maKM = kq.getString("MaKM");
                double total = kq.getDouble("TongTien");

                PhieuDatThuocDTO e = new PhieuDatThuocDTO(
                        ma,
                        ngay,
                        isThanhToan,
                        mapNV.get(maNV),
                        mapKH.get(maKhach),
                        mapKM.get(maKM),
                        total
                );

                ds.add(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ds;
    }

    static ArrayList<PhieuDatThuocDTO> getAllPhieuDatThuocDaXoa() {
        ArrayList<PhieuDatThuocDTO> ds = new ArrayList<>();

        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Connection con = ConnectDB.getConnection();

        String sql = "SELECT MaPDT, NgayTao, IsThanhToan, MaKH, MaNV, MaKM, TongTien FROM PhieuDatThuoc where DeleteAt =1";

        try (
                PreparedStatement state = con.prepareStatement(sql);
                ResultSet kq = state.executeQuery()
        ) {
            ArrayList<NhanVienDTO> nvList = I_NhanVien_Service.getDsNhanVienformDBS();
            ArrayList<KhachHangDTO> khList = I_KhachHang_Service.loadBanFromDB();
            List<KhuyenMaiDTO> kmList = I_KhuyenMai_Service.getAllKhuyenMaiConHieuLuc();

            Map<String, NhanVienDTO> mapNV = nvList.stream()
                    .collect(Collectors.toMap(NhanVienDTO::getMaNV, x -> x));

            Map<String, KhachHangDTO> mapKH = khList.stream()
                    .collect(Collectors.toMap(KhachHangDTO::getMaKH, x -> x));

            Map<String, KhuyenMaiDTO> mapKM = kmList.stream()
                    .collect(Collectors.toMap(KhuyenMaiDTO::getMaKM, x -> x));

            while (kq.next()) {
                String ma = kq.getString("MaPDT");
                LocalDate ngay = kq.getDate("NgayTao").toLocalDate();
                boolean isThanhToan = kq.getBoolean("IsThanhToan");
                String maKhach = kq.getString("MaKH");
                String maNV = kq.getString("MaNV");
                String maKM = kq.getString("MaKM");
                double total = kq.getDouble("TongTien");

                PhieuDatThuocDTO e = new PhieuDatThuocDTO(
                        ma,
                        ngay,
                        isThanhToan,
                        mapNV.get(maNV),
                        mapKH.get(maKhach),
                        mapKM.get(maKM),
                        total
                );

                ds.add(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ds;
    }

    static boolean themPhieuDatThuocVaoDBS(PhieuDatThuocDTO i) {
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();

            // Kiểm tra đã tồn tại hay chưa
            String sqlCheck = "SELECT * FROM PhieuDatThuoc WHERE MaPDT = ?";
            PreparedStatement checkStmt = con.prepareStatement(sqlCheck);
            checkStmt.setString(1, i.getMaPhieu());
            ResultSet check = checkStmt.executeQuery();
            if (check.next()) {
                return false; // đã tồn tại
            }

            // Câu lệnh thêm mới
            String updateSQL = "INSERT INTO PhieuDatThuoc " +
                    "([MaPDT], [NgayTao], [IsThanhToan], [MaKH], [MaNV], [MaKM], [TongTien]) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement state = con.prepareStatement(updateSQL);
            state.setString(1, i.getMaPhieu());
            state.setDate(2, Date.valueOf(i.getNgayTao()));
            state.setBoolean(3, i.isThanhToan());
            state.setString(4, i.getKhachHang().getMaKH());
            state.setString(5, i.getNhanVienDTO().getMaNV());
            state.setString(6, i.getKhuyenMaiDTO() != null ? i.getKhuyenMaiDTO().getMaKM() : null);
            state.setDouble(7, i.getTongTien());

            int kq = state.executeUpdate();
            return kq > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Xoá phiếu đặt thuốc trong DBS bằng cách tắt trạng thái hoạt động.
     *
     * @param maPDT mã phiếu đặt thuốc
     * @return true nếu cập nhật thành công. false nếu không thể cập nhật.
     */
    static boolean xoaPhieuDatThuocTrongDBS(String maPDT) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String sql = "UPDATE PhieuDatThuoc SET DeleteAt = ? WHERE MaPDT = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, true);
            ps.setString(2, maPDT);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy mã hash lớn nhẩt trong database
     *
     * @return String - mã phiếu đặt thuốc mới nhất.
     * null nếu không có gì trong dbs.
     */
    static String getMaxHash() {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "select top 1 MaPDT from PhieuDatThuoc order by MaPDT desc";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet kq = state.executeQuery();
            while (kq.next()) {
                return kq.getString(1).substring(4, 6);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    static void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuocDTO ctPDT) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();

        try {
            String updateSQL = "insert into ChiTietPhieuDatThuoc values(?,?,?,?,?,?)";
            PreparedStatement state = con.prepareStatement(updateSQL);
            state.setString(1, ctPDT.getMaPhieu().getMaPhieu());
            state.setString(2, ctPDT.getMaThuoc().getMaThuocDTO().getMaThuoc());
            state.setInt(3, ctPDT.getSoLuong());
            state.setInt(4, ctPDT.getDonViTinhDTO().getMaDVT());
            state.setString(5, "Đặt");
            state.setDouble(6, ctPDT.getThanhTien());
            int kq = state.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean capNhatThanhToanPhieuDat(String maPDT) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();

        try {
            String sql = "update PhieuDatThuoc set IsThanhToan = 1 where MaPDT = ?";
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, maPDT);
            int kq = state.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    static PhieuDatThuocDTO getPhieuDatByMaFromDBS(String maPDT) {
        for (PhieuDatThuocDTO pdt : PhieuDat_Service.list) {
            if (pdt.getMaPhieu().equalsIgnoreCase(maPDT)) {
                return pdt;
            }
        }
        return null;
    }

    static boolean khoiPhucPhieuDat(String maPhieu) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();

        try {
            String sql = "update PhieuDatThuoc set DeleteAt = 0 where MaPDT = ?";
            PreparedStatement state = con.prepareStatement(sql);
            state.setString(1, maPhieu);
            int kq = state.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
