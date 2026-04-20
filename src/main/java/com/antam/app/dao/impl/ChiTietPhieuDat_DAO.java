package com.antam.app.dao.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 05/12/2025
 * @version: 1.0
 */

import com.antam.app.dao.I_ChiTietPhieuDat_DAO;
import com.antam.app.entity.ChiTietPhieuDatThuoc;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuDat_DAO extends AbstractGenericDao<ChiTietPhieuDatThuoc, ChiTietPhieuDatThuoc.ChiTietPhieuDatThuocId> implements I_ChiTietPhieuDat_DAO {
    public ChiTietPhieuDat_DAO() {
        super(ChiTietPhieuDatThuoc.class);
    }

    @Override
    public boolean thanhToanChiTietVoiMa(String maPDT) {
        return doInTransaction(em -> {
            String query = "UPDATE ChiTietPhieuDatThuoc ct SET ct.isThanhToan = true WHERE ct.maPhieu.maPhieu = :maPDT";
            return em.createQuery(query)
                    .setParameter("maPDT", maPDT)
                    .executeUpdate() > 0;
        });
    }

    @Override
    public void themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuoc ct) {
        doInTransaction(em -> {
            // Merge the associated entities to avoid detached entity issues
            ct.setMaPhieu(em.merge(ct.getMaPhieu()));
            ct.setMaThuoc(em.merge(ct.getMaThuoc()));
            ct.setDonViTinh(em.merge(ct.getDonViTinh()));
            em.persist(ct);
            return null;
        });
    }

    @Override
    public List<ChiTietPhieuDatThuoc> getChiTietTheoPhieu(String maPDT) {
        String query = "select ct from ChiTietPhieuDatThuoc ct " +
                "join fetch ct.maThuoc lt " +
                "left join fetch lt.maCTPN " +
                "join fetch ct.maPhieu p " +
                "join fetch ct.donViTinh dvt " +
                "where p.maPhieu = :maPDT";

        return doInTransaction(em -> {
            List<ChiTietPhieuDatThuoc> result = em.createQuery(query, ChiTietPhieuDatThuoc.class)
                    .setParameter("maPDT", maPDT)
                    .getResultList();
            for (ChiTietPhieuDatThuoc ct : result) {
                Hibernate.initialize(ct.getMaThuoc().getMaCTPN());
            }
            return result;
        });
    }

    @Override
    public boolean huyChiTietPhieu(String maPDT) {
        return doInTransaction(em -> {
            String query = "UPDATE ChiTietPhieuDatThuoc ct SET ct.isThanhToan = false WHERE ct.maPhieu.maPhieu = :maPDT";
            return em.createQuery(query)
                    .setParameter("maPDT", maPDT)
                    .executeUpdate() > 0;
        });
    }

    @Override
    public boolean khoiPhucChiTietPhieu(String maPDT) {
        return doInTransaction(em -> {
            String query = "UPDATE ChiTietPhieuDatThuoc ct SET ct.isThanhToan = true WHERE ct.maPhieu.maPhieu = :maPDT";
            return em.createQuery(query)
                    .setParameter("maPDT", maPDT)
                    .executeUpdate() > 0;
        });
    }
}