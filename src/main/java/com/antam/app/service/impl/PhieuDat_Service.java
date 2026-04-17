package com.antam.app.service.impl;/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 02/10/2025
 * @version: 1.0
 */

import com.antam.app.service.I_PhieuDat_Service;
import com.antam.app.dto.*;

import java.util.ArrayList;

public class PhieuDat_Service implements I_PhieuDat_Service {
    public static ArrayList<PhieuDatThuocDTO> list = I_PhieuDat_Service.getAllPhieuDatThuocFromDBS();

    public static Thuoc_Service thuoc_dao = new Thuoc_Service();
    private NhanVien_Service nvDAO = new NhanVien_Service();
    private KhachHang_Service khDAO = new KhachHang_Service();
    private KhuyenMai_Service kmDAO = new KhuyenMai_Service();


}
