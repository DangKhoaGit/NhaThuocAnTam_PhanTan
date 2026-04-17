package com.antam.app.service;

import com.antam.app.dto.NhanVienDTO;
import com.antam.app.dto.ThongKeDoanhThuDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_ThongKeDoanhThu_Service {
    ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoThoiGian(LocalDate tuNgay, LocalDate denNgay, String maNV);

    ArrayList<ThongKeDoanhThuDTO> getDoanhThuTheoThang(LocalDate tuNgay, LocalDate denNgay, String maNV);

    double getTongDoanhThu(LocalDate tuNgay, LocalDate denNgay, String maNV);

    int getTongDonHang(LocalDate tuNgay, LocalDate denNgay, String maNV);

    int getSoKhachHangMoi(LocalDate tuNgay, LocalDate denNgay, String maNV);

    Map<String, Integer> getTopSanPhamBanChay(LocalDate tuNgay, LocalDate denNgay, int top);

    ArrayList<NhanVienDTO> getDanhSachNhanVien();
}
