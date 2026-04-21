package com.antam.app.service.impl;

import com.antam.app.dao.I_ChiTietPhieuNhap_DAO;
import com.antam.app.dao.I_LoThuoc_DAO;
import com.antam.app.dao.impl.ChiTietPhieuNhap_DAO;
import com.antam.app.dao.impl.LoThuoc_DAO;
import com.antam.app.dto.*;
import com.antam.app.entity.*;
import com.antam.app.service.I_ChiTietPhieuNhap_Service;

import java.util.ArrayList;

public class ChiTietPhieuNhap_Service implements I_ChiTietPhieuNhap_Service {

    private final I_ChiTietPhieuNhap_DAO chiTietDAO = new ChiTietPhieuNhap_DAO();
    private final I_LoThuoc_DAO loThuocDAO = new LoThuoc_DAO();

    @Override
    public ArrayList<ChiTietPhieuNhapDTO> getDanhSachChiTietPhieuNhapTheoMaPN(String maPN) {
        ArrayList<ChiTietPhieuNhapDTO> dsDTO = new ArrayList<>();

        ArrayList<ChiTietPhieuNhap> dsEntity = chiTietDAO.getDanhSachChiTietPhieuNhapTheoMaPN(maPN);

        for (ChiTietPhieuNhap entity : dsEntity) {
            ChiTietPhieuNhapDTO dto = new ChiTietPhieuNhapDTO();

            ThuocDTO thuocDTO = new ThuocDTO();

            thuocDTO.setTenThuoc(entity.getLoThuoc().getMaThuoc().getTenThuoc());
            thuocDTO.setMaThuoc(entity.getLoThuoc().getMaThuoc().getMaThuoc());
            thuocDTO.setThue(entity.getLoThuoc().getMaThuoc().getThue());

            LoThuocDTO loDTO = new LoThuocDTO();
            loDTO.setMaLoThuoc(entity.getLoThuoc().getMaLoThuoc());
            loDTO.setMaThuocDTO(thuocDTO);

            dto.setMaPN(new PhieuNhapDTO(entity.getMaPN().getMaPhieuNhap()));
            dto.setLoThuocDTO(loDTO);

            DonViTinhDTO dvtDTO = new DonViTinhDTO(entity.getMaDVT().getMaDVT());
            dvtDTO.setTenDVT(entity.getMaDVT().getTenDVT());
            dto.setMaDVT(dvtDTO);

            dto.setGiaNhap(entity.getGiaNhap());

            dto.setSoLuong(entity.getSoLuong());

            dsDTO.add(dto);
        }
        return dsDTO;
    }

    @Override
    public boolean themChiTietPhieuNhap(ChiTietPhieuNhapDTO dto) {
        // 1. Kiểm tra logic đầu vào
        if (dto == null || dto.getLoThuocDTO() == null) return false;

        // 2. Mapping DTO sang Entity cho Lô Thuốc
        LoThuoc loEntity = new LoThuoc();
        Thuoc thuocEntity = new Thuoc();
        thuocEntity.setMaThuoc(dto.getLoThuocDTO().getMaThuocDTO().getMaThuoc());
        // Quan trọng: Gán thuế để hàm thanhTien() không bị Null
        thuocEntity.setThue(dto.getLoThuocDTO().getMaThuocDTO().getThue());

        loEntity.setMaThuoc(thuocEntity);
        loEntity.setSoLuong(dto.getLoThuocDTO().getSoLuong());
        loEntity.setHanSuDung(dto.getLoThuocDTO().getHanSuDung());
        loEntity.setNgaySanXuat(dto.getLoThuocDTO().getNgaySanXuat());

        // 3. LƯU LÔ THUỐC TRƯỚC ĐỂ "BIẾT" MÃ LÔ
        int maLoVuaTao = loThuocDAO.themChiTietThuocVaLayID(loEntity);
        if (maLoVuaTao <= 0) return false;

        // 4. Mapping sang Entity Chi Tiết Phiếu Nhập
        ChiTietPhieuNhap ctpnEntity = new ChiTietPhieuNhap();

        PhieuNhap pn = new PhieuNhap();
        pn.setMaPhieuNhap(dto.getMaPN().getMaPhieuNhap());
        ctpnEntity.setMaPN(pn);

        // Dùng mã ID vừa lấy từ DB gán vào đây
        loEntity.setMaLoThuoc(maLoVuaTao);
        ctpnEntity.setLoThuoc(loEntity);

        ctpnEntity.setSoLuong(dto.getSoLuong());
        ctpnEntity.setGiaNhap(dto.getGiaNhap());

        DonViTinh dvt = new DonViTinh();
        dvt.setMaDVT(dto.getMaDVT().getMaDVT());
        ctpnEntity.setMaDVT(dvt);

        // 5. Lưu vào Database
        return chiTietDAO.themChiTietPhieuNhap(ctpnEntity);
    }
}