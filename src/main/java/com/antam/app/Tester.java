package com.antam.app;

import com.antam.app.dao.impl.HoaDon_DAO;
import com.antam.app.entity.HoaDon;

import java.util.List;

import com.antam.app.dao.impl.HoaDon_DAO;
import com.antam.app.dto.HoaDonDTO;
import com.antam.app.dto.KhachHangDTO;
import com.antam.app.entity.HoaDon;
import com.antam.app.helper.MaKhoaMatKhau;
import com.antam.app.service.impl.HoaDon_Service;
import com.antam.app.service.impl.KhachHang_Service;

import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public class Tester {
    public static void main(String[] args) {
        String string = "duong";
        String password = MaKhoaMatKhau.hashPassword(string , 10);
        System.out.println(password);

//        KhachHang_Service khachHang_service = new KhachHang_Service();
//        List<KhachHangDTO> dtos = khachHang_service.getAllKhachHang();
//        for (KhachHangDTO dto : dtos) {
//            System.out.println(dto);
//        }
//        HoaDon_Service  hd = new HoaDon_Service();
//        List<HoaDonDTO>  hoaDons = hd.getHoaDonByMaKH("KH000000001");
//        System.out.println(
//                hoaDons.toString()
//        );

    }


}
