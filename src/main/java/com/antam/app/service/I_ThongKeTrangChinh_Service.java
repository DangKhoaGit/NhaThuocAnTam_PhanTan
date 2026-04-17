package com.antam.app.service;

import java.util.List;
import java.util.Map;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_ThongKeTrangChinh_Service {
    int getTongSoThuoc();

    int getTongSoNhanVien();

    int getSoHoaDonHomNay();

    int getSoKhuyenMaiApDung();

    Map<String, Double> getDoanhThu7NgayGanNhat();

    Map<String, Integer> getTopSanPhamBanChay(int limit);

    List<Map<String, Object>> getThuocSapHetHan();

    List<Map<String, Object>> getThuocTonKhoThap();
}
