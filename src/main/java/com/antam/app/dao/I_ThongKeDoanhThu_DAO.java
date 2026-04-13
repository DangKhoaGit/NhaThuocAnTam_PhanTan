package com.antam.app.dao;

import com.antam.app.entity.NhanVien;
import com.antam.app.entity.ThongKeDoanhThu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_ThongKeDoanhThu_DAO {
    ArrayList<ThongKeDoanhThu> getDoanhThuTheoThoiGian(LocalDate tuNgay, LocalDate denNgay, String maNV);

    ArrayList<ThongKeDoanhThu> getDoanhThuTheoThang(LocalDate tuNgay, LocalDate denNgay, String maNV);

    double getTongDoanhThu(LocalDate tuNgay, LocalDate denNgay, String maNV);

    int getTongDonHang(LocalDate tuNgay, LocalDate denNgay, String maNV);

    int getSoKhachHangMoi(LocalDate tuNgay, LocalDate denNgay, String maNV);

    Map<String, Integer> getTopSanPhamBanChay(LocalDate tuNgay, LocalDate denNgay, int top);

    ArrayList<NhanVien> getDanhSachNhanVien();
}
