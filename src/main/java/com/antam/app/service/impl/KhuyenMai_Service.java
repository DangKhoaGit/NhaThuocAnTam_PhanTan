package com.antam.app.service.impl;

import com.antam.app.dao.I_KhuyenMai_DAO;
import com.antam.app.dao.impl.KhuyenMai_DAO;
import com.antam.app.dto.KhuyenMaiDTO;
import com.antam.app.dto.LoaiKhuyenMaiDTO;
import com.antam.app.entity.KhuyenMai;
import com.antam.app.entity.LoaiKhuyenMai;
import com.antam.app.service.I_KhuyenMai_Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMai_Service implements I_KhuyenMai_Service {
    private final I_KhuyenMai_DAO khuyenMaiDAO;

    public KhuyenMai_Service() {
        this.khuyenMaiDAO = new KhuyenMai_DAO();
    }

    @Override
    public List<KhuyenMaiDTO> getAllKhuyenMaiConHieuLuc() {
        List<KhuyenMai> entities = khuyenMaiDAO.fetchAllKhuyenMaiConHieuLuc();
        ArrayList<KhuyenMaiDTO> result = new ArrayList<>();
        for (KhuyenMai entity : entities) {
            result.add(toDto(entity));
        }
        return result;
    }

    @Override
    public ArrayList<KhuyenMaiDTO> getAllKhuyenMaiChuaXoa() {
        return toDtoList(khuyenMaiDAO.getAllKhuyenMaiChuaXoa());
    }

    @Override
    public ArrayList<KhuyenMaiDTO> getAllKhuyenMaiDaXoa() {
        return toDtoList(khuyenMaiDAO.getAllKhuyenMaiDaXoa());
    }

    @Override
    public boolean khoiPhucKhuyenMai(String maKM) {
        return khuyenMaiDAO.khoiPhucKhuyenMai(maKM);
    }

    @Override
    public ArrayList<KhuyenMaiDTO> getAllKhuyenMai() {
        return toDtoList(khuyenMaiDAO.getAllKhuyenMai());
    }

    @Override
    public KhuyenMaiDTO getKhuyenMaiTheoMa(String maKM) {
        return toDto(khuyenMaiDAO.getKhuyenMaiTheoMa(maKM));
    }

    @Override
    public boolean themKhuyenMai(String maKM, String tenKM, LoaiKhuyenMaiDTO loaiKM, double so, LocalDate ngayBatDau, LocalDate ngayKetThuc, int soLuongToiDa) {
        return khuyenMaiDAO.themKhuyenMai(
                maKM,
                tenKM,
                toLoaiEntity(loaiKM),
                so,
                ngayBatDau,
                ngayKetThuc,
                soLuongToiDa
        );
    }

    @Override
    public boolean capNhatKhuyenMai(KhuyenMaiDTO km) {
        return khuyenMaiDAO.capNhatKhuyenMai(toEntity(km));
    }

    @Override
    public boolean xoaKhuyenMai(String maKM) {
        return khuyenMaiDAO.xoaKhuyenMai(maKM);
    }

    private ArrayList<KhuyenMaiDTO> toDtoList(List<KhuyenMai> entities) {
        ArrayList<KhuyenMaiDTO> dtos = new ArrayList<>();
        for (KhuyenMai entity : entities) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }

    private KhuyenMaiDTO toDto(KhuyenMai entity) {
        if (entity == null) {
            return null;
        }
        LoaiKhuyenMai loaiEntity = entity.getLoaiKhuyenMai();
        LoaiKhuyenMaiDTO loaiDto = loaiEntity == null
                ? new LoaiKhuyenMaiDTO(0, "")
                : new LoaiKhuyenMaiDTO(loaiEntity.getMaLKM(), loaiEntity.getTenLKM());

        return new KhuyenMaiDTO(
                entity.getMaKM(),
                entity.getTenKM(),
                entity.getNgayBatDau(),
                entity.getNgayKetThuc(),
                loaiDto,
                entity.getSo(),
                entity.getSoLuongToiDa(),
                entity.isDeleteAt()
        );
    }

    private KhuyenMai toEntity(KhuyenMaiDTO dto) {
        if (dto == null) {
            return null;
        }
        return new KhuyenMai(
                dto.getMaKM(),
                dto.getTenKM(),
                dto.getNgayBatDau(),
                dto.getNgayKetThuc(),
                toLoaiEntity(dto.getLoaiKhuyenMaiDTO()),
                dto.getSo(),
                dto.getSoLuongToiDa(),
                false
        );
    }

    private LoaiKhuyenMai toLoaiEntity(LoaiKhuyenMaiDTO dto) {
        if (dto == null) {
            return new LoaiKhuyenMai(0, "");
        }
        return new LoaiKhuyenMai(dto.getMaLKM(), dto.getTenLKM());
    }

    public KhuyenMaiDTO mapEntityToDTO(KhuyenMai entity) {
        if (entity == null) return null;
        LoaiKhuyenMaiDTO loaiDTO = null;
        if (entity.getLoaiKhuyenMai() != null) {
            loaiDTO = new LoaiKhuyenMaiDTO(entity.getLoaiKhuyenMai().getMaLKM(), entity.getLoaiKhuyenMai().getTenLKM());
        }
        return new KhuyenMaiDTO(entity.getMaKM(), entity.getTenKM(), entity.getNgayBatDau(), entity.getNgayKetThuc(), loaiDTO, entity.getSo(), entity.getSoLuongToiDa(), entity.isDeleteAt());
    }

    public KhuyenMai mapDTOToEntity(KhuyenMaiDTO dto) {
        if (dto == null) return null;
        LoaiKhuyenMai loai = null;
        if (dto.getLoaiKhuyenMaiDTO() != null) {
            loai = new LoaiKhuyenMai(dto.getLoaiKhuyenMaiDTO().getMaLKM(), dto.getLoaiKhuyenMaiDTO().getTenLKM());
        }
        return new KhuyenMai(dto.getMaKM(), dto.getTenKM(), dto.getNgayBatDau(), dto.getNgayKetThuc(), loai, dto.getSo(), dto.getSoLuongToiDa(), false); // assuming deleteAt false
    }
}
