package com.antam.app.dao.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 02/10/2025
 * @version: 1.0
 */

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.antam.app.dao.I_PhieuDat_DAO;
import com.antam.app.entity.*;

public class PhieuDat_DAO extends AbstractGenericDao<PhieuDatThuoc,String> implements I_PhieuDat_DAO {
    public PhieuDat_DAO() {
        super(PhieuDatThuoc.class);
    }

    @Override
    public List<PhieuDatThuoc> getAllPhieuDatThuocFromDBS() {
        @SuppressWarnings("unchecked")
        List<PhieuDatThuoc> list = (List<PhieuDatThuoc>) super.findAll();
        return new ArrayList<>(list.stream().filter(p -> !p.isDaXoa()).collect(Collectors.toList()));
    }

    @Override
    public List<PhieuDatThuoc> getAllPhieuDatThuocDaXoa() {
        @SuppressWarnings("unchecked")
        List<PhieuDatThuoc> list = (List<PhieuDatThuoc>) super.findAll();
        return new ArrayList<>(list.stream().filter(PhieuDatThuoc::isDaXoa).collect(Collectors.toList()));
    }

    @Override
    public boolean themPhieuDatThuocVaoDBS(PhieuDatThuoc i) {
        return doInTransaction(em -> {

            //  2. Set reference thay vì merge
            if (i.getNhanVien() != null) {
                i.setNhanVien(
                        em.getReference(NhanVien.class, i.getNhanVien().getMaNV())
                );
            }

            if (i.getKhachHang() != null) {
                i.setKhachHang(
                        em.getReference(KhachHang.class, i.getKhachHang().getMaKH())
                );
            }

            if (i.getKhuyenMai() != null) {
                i.setKhuyenMai(
                        em.getReference(KhuyenMai.class, i.getKhuyenMai().getMaKM())
                );
            }

            //  3. Persist
            em.persist(i);

            return true;
        });
    }

    @Override
    public boolean xoaPhieuDatThuocTrongDBS(String maPDT) {
        return doInTransaction(em -> {
            String query = "UPDATE PhieuDatThuoc p SET p.daXoa = true WHERE p.maPhieu = :maPDT";
            return em.createQuery(query)
                    .setParameter("maPDT", maPDT)
                    .executeUpdate() > 0;
        });
    }

    @Override
    public String getMaxHash() {
        String query = "SELECT p.maPhieu FROM PhieuDatThuoc p ORDER BY p.maPhieu DESC";
        return doInTransaction(em -> {
            List<String> result = em.createQuery(query, String.class)
                    .setMaxResults(1)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    @Override
    public void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuoc ctPDT) {
        // This should probably be in ChiTietPhieuDat_DAO
        // But since it's in the interface, we'll delegate
        ChiTietPhieuDat_DAO chiTietDAO = new ChiTietPhieuDat_DAO();
        chiTietDAO.create(ctPDT);
    }

    @Override
    public boolean capNhatThanhToanPhieuDat(String maPDT) {
        return doInTransaction(em -> {
            String query = "UPDATE PhieuDatThuoc p SET p.isThanhToan = true WHERE p.maPhieu = :maPDT";
            return em.createQuery(query)
                    .setParameter("maPDT", maPDT)
                    .executeUpdate() > 0;
        });
    }

    @Override
    public PhieuDatThuoc getPhieuDatByMaFromDBS(String maPDT) {
        return super.findById(maPDT);
    }

    @Override
    public boolean khoiPhucPhieuDat(String maPhieu) {
        return doInTransaction(em -> {
            String query = "UPDATE PhieuDatThuoc p SET p.daXoa = false WHERE p.maPhieu = :maPhieu";
            return em.createQuery(query)
                    .setParameter("maPhieu", maPhieu)
                    .executeUpdate() > 0;
        });
    }

    public static List<PhieuDatThuoc> list = new PhieuDat_DAO().getAllPhieuDatThuocFromDBS();

    public static Thuoc_DAO thuoc_dao = new Thuoc_DAO();
    private NhanVien_DAO nvDAO = new NhanVien_DAO();
    private KhachHang_DAO khDAO = new KhachHang_DAO();
    private KhuyenMai_DAO kmDAO = new KhuyenMai_DAO();

    /**
     * Helper method để lấy danh sách tất cả khách hàng (không bị xóa)
     * Dùng cho static methods trong I_PhieuDat_DAO
     * @return danh sách KhachHang
     */
    public List<KhachHang> getAllKhachHangFromDAO() {
        return khDAO.getAllKhachHang();
    }
}
