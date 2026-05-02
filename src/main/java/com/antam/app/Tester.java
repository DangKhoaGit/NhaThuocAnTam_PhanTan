package com.antam.app;

import com.antam.app.dao.impl.HoaDon_DAO;
import com.antam.app.dao.impl.NhanVien_DAO;
import com.antam.app.dao.impl.Thuoc_DAO;
import com.antam.app.dto.*;
import com.antam.app.entity.HoaDon;

import java.util.ArrayList;
import java.util.List;

import com.antam.app.dao.impl.HoaDon_DAO;
import com.antam.app.entity.HoaDon;
import com.antam.app.entity.LoThuoc;
import com.antam.app.entity.PhieuDatThuoc;
import com.antam.app.helper.MaKhoaMatKhau;
import com.antam.app.network.ClientManager;
import com.antam.app.service.impl.*;
import javafx.concurrent.Task;

import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public class Tester {
    public static void main(String[] args) {
//        String string = "admin";
//        String password = MaKhoaMatKhau.hashPassword(string , 10);
//        System.out.println(password);

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

//        NhanVien_Service thuoc_service = new NhanVien_Service();
//        List<NhanVienDTO> dtos = thuoc_service.getAllNhanVien();
//        for (NhanVienDTO dto : dtos) {
//            System.out.println(dto.getHoTen() +"|" +dto.isQuanLi() +"|" + dto.isDeleteAt());
//        }

//        KhuyenMai_Service service = new KhuyenMai_Service();
//        List<KhuyenMaiDTO> dtos = service.getAllKhuyenMai();
//        for (KhuyenMaiDTO dto : dtos) {
//            System.out.println(dto);
//        }
        testClient();

    }

    public static void testClient() {
        Task<List<LoThuocDTO>> task = new Task<>() {

            @Override
            protected List<LoThuocDTO> call() throws Exception {
                System.out.println("Task running...");
                return ClientManager.getInstance().getLoThuocFefoByThuocId1("TH000000001");
            }
        };

        task.setOnSucceeded(event -> {
            System.out.println("SUCCESS");
            List<LoThuocDTO> list = task.getValue();
            System.out.println(list);
        });

        task.setOnFailed(event -> {
            System.out.println("FAILED");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }


}
