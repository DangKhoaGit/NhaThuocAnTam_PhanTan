package com.antam.app.connect;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 13/04/2026
 * @version: 1.0
 */
public class JPA_Util {
    private static final String PERSISTEN_UNIT_NAME = "mariadb-pu";
    private static EntityManagerFactory emf;
    private static EntityManager em;

    static {
        emf = Persistence.createEntityManagerFactory(PERSISTEN_UNIT_NAME);
    }

    public static  EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close(){
        if (emf != null) {
            emf.close();
        }
    }
}
