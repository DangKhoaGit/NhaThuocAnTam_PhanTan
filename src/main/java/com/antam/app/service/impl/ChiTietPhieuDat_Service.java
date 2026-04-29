package com.antam.app.service.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 05/12/2025
 * @version: 1.0
 */

import com.antam.app.connect.JPA_Util;
import com.antam.app.dao.impl.ChiTietPhieuDat_DAO;
import com.antam.app.dto.ChiTietPhieuDatThuocDTO;
import com.antam.app.entity.ChiTietPhieuDatThuoc;
import com.antam.app.service.I_ChiTietPhieuDat_Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;



public class ChiTietPhieuDat_Service  implements I_ChiTietPhieuDat_Service {
    private final ChiTietPhieuDat_DAO chiTietDAO = new ChiTietPhieuDat_DAO();
    private PhieuDat_Service phieuDatService;
    private final LoThuoc_Service loThuocService = new LoThuoc_Service();
    private final DonViTinh_Service donViTinhService = new DonViTinh_Service();

    public ChiTietPhieuDat_Service(PhieuDat_Service phieuDatService) {
        this.phieuDatService = phieuDatService;
    }

    public ChiTietPhieuDat_Service() {
        // phieuDatService will be initialized lazily
    }

    @Override
    public boolean themChiTietPhieuDatVaoDBS(ChiTietPhieuDatThuocDTO ct) {
        try {
            ChiTietPhieuDatThuoc entity = mapDTOToEntity(ct);
            return chiTietDAO.themChiTietPhieuDatVaoDBS(entity);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi them chi tiet phieu dat", e);
        }
    }

    protected <R> R doInTransaction(Function<EntityManager, R> function){
        EntityTransaction tx = null;
        EntityManager em = null;
        try{
            em = JPA_Util.getEntityManager();
            tx = em.getTransaction();
            tx.begin();
            R result = function.apply(em);
            tx.commit();
            return result;

        }catch (Exception e){
            if (tx != null && tx.isActive())
                tx.rollback();
            throw new RuntimeException(e);
        }finally {
            if (em != null)
                em.close();
        }
    }

    @Override
    public List<ChiTietPhieuDatThuocDTO> getChiTietTheoPhieu(String maPDT) {
        return doInTransaction(em -> {
            List<ChiTietPhieuDatThuoc> entities =
                    chiTietDAO.getChiTietTheoPhieu(maPDT);

            List<ChiTietPhieuDatThuocDTO> result = new ArrayList<>();

            for (ChiTietPhieuDatThuoc entity : entities) {
                result.add(mapEntityToDTO(entity)); // ✔ vẫn còn session
            }

            return result;
        });
    }

    @Override
    public boolean thanhToanChiTietVoiMa(String maPDT) {
        try {
            return chiTietDAO.thanhToanChiTietVoiMa(maPDT);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi thanh toan chi tiet", e);
        }
    }

    @Override
    public boolean huyChiTietPhieu(String maPDT) {
        try {
            return chiTietDAO.huyChiTietPhieu(maPDT);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi huy chi tiet phieu", e);
        }
    }

    @Override
    public boolean khoiPhucChiTietPhieu(String maPDT) {
        try {
            return chiTietDAO.khoiPhucChiTietPhieu(maPDT);
        } catch (Exception e) {
            throw new RuntimeException("Loi khi khoi phuc chi tiet phieu", e);
        }
    }

    private ChiTietPhieuDatThuocDTO mapEntityToDTO(ChiTietPhieuDatThuoc entity) {
        if (entity == null) {
            return null;
        }
        return new ChiTietPhieuDatThuocDTO(
                getPhieuDatService().mapEntityToDTO(entity.getMaPhieu()),
                loThuocService.mapEntityToDTO(entity.getMaThuoc()),
                entity.getSoLuong(),
                donViTinhService.mapEntityToDTO(entity.getDonViTinh()),
                entity.getThanhTien()
        );
    }

    private ChiTietPhieuDatThuoc mapDTOToEntity(ChiTietPhieuDatThuocDTO dto) {
        if (dto == null) {
            return null;
        }
        return new ChiTietPhieuDatThuoc(
                getPhieuDatService().mapDTOToEntity(dto.getMaPhieu()),
                loThuocService.mapDTOToEntity(dto.getMaThuoc()),
                dto.getSoLuong(),
                donViTinhService.mapDTOToEntity(dto.getDonViTinhDTO()),
                dto.getThanhTien()
        );
    }

    private PhieuDat_Service getPhieuDatService() {
        if (phieuDatService == null) {
            phieuDatService = new PhieuDat_Service();
        }
        return phieuDatService;
    }
}