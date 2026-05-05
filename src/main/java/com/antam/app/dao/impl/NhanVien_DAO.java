package com.antam.app.dao.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 01/10/2025
 * @version: 1.0
 */

import com.antam.app.connect.ConnectDB;
import com.antam.app.dao.I_NhanVien_DAO;
import com.antam.app.entity.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVien_DAO extends AbstractGenericDao<NhanVien,String> implements I_NhanVien_DAO {

    public NhanVien_DAO() {
        super(NhanVien.class);
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
    @Override
    public boolean themNhanVien(NhanVien nv) {
        NhanVien existing = getNhanVien(nv.getMaNV());
        if (existing == null) {
            return super.create(nv) != null;
        }
        return existing.isDeleteAt()
                ? khoiPhucNhanVien(existing.getMaNV())
                : false;
    }

    /**
     * Cập nhật nhân viên với thông tin mới.
     *
     * @param nv NhanVien
     * @return true nếu có nhân viên được cập nhật.
     * false nếu không có nhân viên nào được cập nhật hoặc nhân viên không tồn tại.
     */
    @Override
    public boolean updateNhanVienTrongDBS(NhanVien nv) {
        String query   = "update NhanVien nv " +
                "set nv.hoTen = :hoTen, " +
                "nv.soDienThoai = :soDienThoai, " +
                "nv.email = :email, " +
                "nv.diaChi = :diaChi, " +
                "nv.luongCoBan = :luongCoBan, " +
                "nv.taiKhoan = :taiKhoan, " +
                "nv.matKhau = :matKhau, " +
                "nv.deleteAt = :deleteAt, " +
                "nv.quanLi = :quanLi " +
                "where nv.id = :id";
        return doInTransaction(em ->
                em.createQuery(query)
                        .setParameter("hoTen", nv.getHoTen())
                        .setParameter("soDienThoai", nv.getSoDienThoai())
                        .setParameter("email", nv.getEmail())
                        .setParameter("diaChi", nv.getDiaChi())
                        .setParameter("luongCoBan", nv.getLuongCoBan())
                        .setParameter("taiKhoan", nv.getTaiKhoan())
                        .setParameter("matKhau", nv.getMatKhau())
                        .setParameter("deleteAt", nv.isDeleteAt())
                        .setParameter("quanLi", nv.isQuanLi())
                        .setParameter("id", nv.getMaNV())
                        .executeUpdate() > 0
        );
    }

    /**
     * Là phương thức sử dụng để tắt trạng thái hoạt đông trong dbs sử dụng mã nhân viên.
     *
     * @param manv
     * @return true nếu có nhân viên bị cập nhật ở dbs. false nếu không có nhân viên nào cập nhật trạng thái.
     */
    @Override
    public boolean xoaNhanVienTrongDBS(String manv) {
        return doInTransaction(em -> {
            String query = "update NhanVien nv " +
                    "set nv.deleteAt = true " +
                    "where nv.id = :id";
            return em.createQuery(query)
                    .setParameter("id", manv)
                    .executeUpdate() > 0;
        });
    }

    /**
     * Lấy mã hash mã nhân viên mới nhất trong database
     *
     * @return String(là dãy số 5 ký tự)
     */
    @Override
    public String getMaxHashNhanVien() {
        // Sắp xếp giảm dần theo ID để lấy mã lớn nhất
        String query = "SELECT nv.id FROM NhanVien nv ORDER BY nv.id DESC";

        return doInTransaction(em -> {
            List<String> result = em.createQuery(query, String.class)
                    .setMaxResults(1)
                    .getResultList();

            if (result.isEmpty()) {
                return null; // Hoặc trả về "NV00000" tùy bạn
            }

            // Trả về thẳng chuỗi "NV00014"
            return result.get(0);
        });
    }

    /**
     * Khooi phục nhân viên đã xóa trong DBS sử dụng mã nhân viên.
     *
     * @param maNV - mã nhân viên
     * @return true nếu có nhân viên bị khôi phục ở dbs. false nếu không có nhân viên nào khôi phục trạng thái.
     */
    @Override
    public boolean khoiPhucNhanVien(String maNV) {
        String query = "update NhanVien nv " +
                "set nv.deleteAt = false " +
                "where nv.id = :id";

        return doInTransaction(em ->
                em.createQuery(query)
                        .setParameter("id", maNV)
                        .executeUpdate() > 0
        );
    }

    // duong
    /**
     * Lấy nhân viên theo tài khoản
     * @param tk Tài khoản
     * @return Nhân viên
     */
    @Override
    public NhanVien getNhanVienTaiKhoan(String tk) {
        String  sql = "SELECT nv " +
                "from NhanVien nv " +
                "where nv.taiKhoan = :tk ";
        return doInTransaction(e -> {
            try {
                return e.createQuery(sql, NhanVien.class)
                        .setParameter("tk", tk)
                        .getSingleResult();
            } catch (jakarta.persistence.NoResultException ex) {
                return null;
            }
        });
    }
    // duong
    // hung
    @Override
    public NhanVien getNhanVien(String id) {
        String  sql = "SELECT nv " +
                "from NhanVien nv " +
                "where nv.id = :id ";
        return doInTransaction(e -> {
            try {
                return e.createQuery(sql, NhanVien.class)
                        .setParameter("id", id)
                        .getSingleResult();
            } catch (jakarta.persistence.NoResultException ex) {
                return null;
            }
        });
    }

    @Override
    public ArrayList<NhanVien> getAllNhanVien() {
        String query = "select nv from NhanVien nv";
        return doInTransaction(em ->
                new ArrayList<>(
                        em.createQuery(query, NhanVien.class)
                                .getResultList()
                )
        );
    }

    @Override
    public NhanVien findNhanVienVoiMa(String maVN) {
        String  sql = "SELECT nv " +
                "from NhanVien nv " +
                "where nv.id = :id ";
        return doInTransaction(e -> {
            try {
                return e.createQuery(sql, NhanVien.class)
                        .setParameter("id", maVN)
                        .getSingleResult();
            } catch (jakarta.persistence.NoResultException ex) {
                return null;
            }
        });
    }
}
