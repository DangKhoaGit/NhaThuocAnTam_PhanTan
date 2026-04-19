package com.antam.app;

import com.antam.app.dao.impl.HoaDon_DAO;
import com.antam.app.entity.HoaDon;

import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public class Tester {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        HoaDon_DAO  hd = new HoaDon_DAO();
        List<HoaDon>  hoaDons = hd.getHoaDonByMaKH("KH000000001");
        System.out.println(
                hoaDons.toString()
        );
    }


}
