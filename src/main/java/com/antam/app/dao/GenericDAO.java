package com.antam.app.dao;

import java.util.List;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 08/04/2026
 * @version: 1.0
 */
public interface GenericDAO<T, ID>{
    T create (T t);
    boolean delete (String id);
    T findById (String id);
    T update (T t);
    List<T> findAll();
}
