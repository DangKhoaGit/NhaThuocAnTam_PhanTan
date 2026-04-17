package com.antam.app.service;

import com.antam.app.dto.ThuocDTO;

import java.util.ArrayList;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public interface I_Thuoc_Service {
    ArrayList<ThuocDTO> getAllThuoc();

    ArrayList<ThuocDTO> getAllThuocDaXoa();

    ThuocDTO getThuocTheoMa(String ma);

    boolean themThuoc(ThuocDTO t);

    boolean capNhatThuoc(ThuocDTO t);

    boolean xoaThuocTheoMa(String ma);

    boolean khoiPhucThuocTheoMa(String ma);
}
