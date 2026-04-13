package com.antam.app.dao;

import com.antam.app.entity.Thuoc;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_Thuoc_DAO {
    ArrayList<Thuoc> getAllThuoc();

    ArrayList<Thuoc> getAllThuocDaXoa();

    Thuoc getThuocTheoMa(String ma);

    boolean themThuoc(Thuoc t);

    boolean capNhatThuoc(Thuoc t);

    boolean xoaThuocTheoMa(String ma);

    boolean khoiPhucThuocTheoMa(String ma);
}
