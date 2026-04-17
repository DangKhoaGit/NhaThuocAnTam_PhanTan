package com.antam.app.service;

import com.antam.app.connect.ConnectDB;
import com.antam.app.dto.NhanVienDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_NhanVien_Service {
    /**
     * Phương thức lấy toàn bộ thông tin Nhân viên từ DBS. trả về 1 mảng nhân viên
     *
     * @return Array[NhanVien]
     */

    static ArrayList<NhanVienDTO> getDsNhanVienformDBS() {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        String sql = "Select * from NhanVien";
        try {
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet result = state.executeQuery();
            while (result.next()) {
                String maNV = result.getNString("MaNV");
                String hoTen = result.getNString("HoTen");
                String soDT = result.getNString("SoDienThoai");
                String email = result.getNString("Email");
                String diaChi = result.getNString("DiaChi");
                double luongCb = result.getDouble("LuongCoBan");
                String taiKhoan = result.getNString("TaiKhoan");
                String matKhau = result.getNString("MatKhau");
                boolean deleteAt = result.getBoolean("DeleteAt");
                boolean isQL = result.getBoolean("IsQuanLi");
                NhanVienDTO e = new NhanVienDTO(maNV, hoTen, soDT, email, diaChi, luongCb, taiKhoan, matKhau, deleteAt, isQL);
                list.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * thêm nhân viên mới vào DBS. Phưong thức trước tiên sẽ kiểm tra nhân viên có tồn tại trong hệ thống hay chưa.
     * Nếu có sẽ kiểm tra tiếp đến trạng thái hoạt động, nếu vẫn đang hoạt động thì sẽ trả kết quả false và thoát chức năng.
     * Nếu không thì sẽ cập nhật lại và bật trạng thái hoạt động của nhân viên.
     * Nếu nhân viên chưa có trong hệ thống thì sẽ tạo mới vào trong DBS.
     *
     * @param nv NhanVien
     * @return true nếu có nhân viên được thêm hoặc cập nhật.
     * false nếu nhân viên đã tồn tại trong hệ thống
     */
    static boolean themNhanVien(NhanVienDTO nv) {
        int kq = 0;
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PreparedStatement state = null;
        Connection con = ConnectDB.getConnection();

        //kiểm tra đã tồn tại hay chưa
        try {
            String sql = "select DeleteAt From NhanVien where MaNV = ?";
            state = con.prepareStatement(sql);
            state.setString(1, nv.getMaNV());
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                int status = rs.getInt("DeleteAt");
                state.close();
                //Kiểm tra vẫn còn hoạt đông hay không
                if (status == 0) {
                    //Vẫn hoạt động -> thoát phương thức
                    return false;
                } else {
                    //không hoạt động -> cập nhật nhân viên
                    String updateSQL = "Update NhanVien Set HoTen = ?,SoDienThoai = ?,Email = ?,DiaChi =?,LuongCoBan =?,TaiKhoan=?," +
                            "MatKhau=?,IsQuanLy=?,DeleteAt = ? where MaNV = ?";
                    state = con.prepareStatement(updateSQL);
                    state.setString(10, nv.getMaNV());
                    state.setString(1, nv.getHoTen());
                    state.setString(2, nv.getSoDienThoai());
                    state.setString(3, nv.getEmail());
                    state.setString(4, nv.getDiaChi());
                    state.setDouble(5, nv.getLuongCoBan());
                    state.setString(6, nv.getTaiKhoan());
                    state.setString(7, nv.getMatKhau());
                    state.setBoolean(8, nv.isQuanLy());
                    state.setBoolean(9, false);
                    kq = state.executeUpdate();
                    return kq > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // chưa có thì thêm mới
        try {
            String sql = "INSERT INTO NhanVien VALUES (?,?,?,?,?,?,?,?,?,?);";
            state = con.prepareStatement(sql);
            state.setString(1, nv.getMaNV());
            state.setString(2, nv.getHoTen());
            state.setString(3, nv.getSoDienThoai());
            state.setString(4, nv.getEmail());
            state.setString(5, nv.getDiaChi());
            state.setDouble(6, nv.getLuongCoBan());
            state.setString(7, nv.getTaiKhoan());
            state.setString(8, nv.getMatKhau());
            state.setBoolean(9, nv.isQuanLy());
            state.setBoolean(10, false);
            kq = state.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (state != null) {
                    state.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return kq > 0;
    }

    /**
     * Cập nhật nhân viên với thông tin mới.
     *
     * @param nv NhanVien
     * @return true nếu có nhân viên được cập nhật.
     * false nếu không có nhân viên nào được cập nhật hoặc nhân viên không tồn tại.
     */
    static boolean updateNhanVienTrongDBS(NhanVienDTO nv) {
        int kq = 0;
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        PreparedStatement state = null;
        String updateSQL = "Update NhanVien Set HoTen = ?,SoDienThoai = ?,Email = ?,DiaChi =?,LuongCoBan =?,TaiKhoan=?," +
                "MatKhau=?,IsQuanLi=?,DeleteAt = ? where MaNV = ?";
        try {
            state = con.prepareStatement(updateSQL);
            state.setString(10, nv.getMaNV());
            state.setString(1, nv.getHoTen());
            state.setString(2, nv.getSoDienThoai());
            state.setString(3, nv.getEmail());
            state.setString(4, nv.getDiaChi());
            state.setDouble(5, nv.getLuongCoBan());
            state.setString(6, nv.getTaiKhoan());
            state.setString(7, nv.getMatKhau());
            state.setBoolean(8, nv.isQuanLy());
            state.setBoolean(9, false);
            kq = state.executeUpdate();
            return kq > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Là phương thức sử dụng để tắt trạng thái hoạt đông trong dbs sử dụng mã nhân viên.
     *
     * @param manv
     * @return true nếu có nhân viên bị cập nhật ở dbs. false nếu không có nhân viên nào cập nhật trạng thái.
     */
    static boolean xoaNhanVienTrongDBS(String manv) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement state = null;
        Connection con = ConnectDB.getConnection();
        String sql = "Update [NhanVien] set DeleteAt = 1 where MaNV = ?";
        int kq = 0;
        try {
            state = con.prepareStatement(sql);
            state.setString(1, manv);
            kq = state.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (state != null) {
                    state.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return kq > 0;
    }

    /**
     * Lấy mã hash mã nhân viên mới nhất trong database
     *
     * @return String(là dãy số 5 ký tự)
     */
    static String getMaxHashNhanVien() {
        String str = "";
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Connection con = ConnectDB.getConnection();
        try {
            String sql = "select top 1 MaNV from NhanVien order by MaNV desc";
            PreparedStatement state = con.prepareStatement(sql);
            ResultSet kq = state.executeQuery();
            while (kq.next()) {
                str = kq.getString(1).substring(2, 7);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    /**
     * Khooi phục nhân viên đã xóa trong DBS sử dụng mã nhân viên.
     *
     * @param maNV - mã nhân viên
     * @return true nếu có nhân viên bị khôi phục ở dbs. false nếu không có nhân viên nào khôi phục trạng thái.
     */
    static boolean khoiPhucNhanVien(String maNV) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PreparedStatement state = null;
        Connection con = ConnectDB.getConnection();
        String sql = "Update [NhanVien] set DeleteAt = 0 where MaNV = ?";
        int kq = 0;
        try {
            state = con.prepareStatement(sql);
            state.setString(1, maNV);
            kq = state.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (state != null) {
                    state.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return kq > 0;
    }

    NhanVienDTO findNhanVienVoiMa(String maVN);

    NhanVienDTO getNhanVien();

    NhanVienDTO getNhanVienTaiKhoan(String id);

    // duong
    // hung
    NhanVienDTO getNhanVien(String id);

    ArrayList<NhanVienDTO> getAllNhanVien();
}
