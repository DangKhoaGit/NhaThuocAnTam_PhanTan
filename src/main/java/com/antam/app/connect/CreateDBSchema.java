package com.antam.app.connect;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public class CreateDBSchema {
    public static void main(String[] args) {
        EntityManager entityManager = Persistence.createEntityManagerFactory("mariadb-pu")
                .createEntityManager();
    }
}
