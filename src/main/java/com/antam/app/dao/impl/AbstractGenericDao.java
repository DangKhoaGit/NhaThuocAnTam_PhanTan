package com.antam.app.dao.impl;

import com.antam.app.connect.JPA_Util;
import com.antam.app.dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.function.Function;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 08/04/2026
 * @version: 1.0
 */
public abstract class AbstractGenericDao<T, ID> implements GenericDAO<T,ID> {
    protected final Class<T> entityClass;

    protected AbstractGenericDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected <R> R doInTransaction(Function<EntityManager, R> function){
        EntityTransaction tx = null;
        EntityManager em = null;
        try{
            em = JPA_Util.getEntityManager();
            tx = em.getTransaction();
            tx.begin();
            R result = function.apply(em);
            tx.commit();
            return result;

        }catch (Exception e){
            if (tx != null && tx.isActive())
                tx.rollback();
            throw new RuntimeException(e);
        }finally {
            if (em != null)
                em.close();
        }
    }

    @Override
    public T create(T t) {
        return doInTransaction(em ->{
            em.persist(t);
            return t;
        });
    }

    @Override
    public boolean delete(String id) {
        return doInTransaction(em ->{
            T t = em.find(entityClass, id);
            em.remove(t);
            return true;
        });
    }

    @Override
    public T findById(String id) {
        return doInTransaction(em ->{
            return em.find(entityClass, id);
        });
    }

    @Override
    public T update(T t) {
        return doInTransaction(em ->{
            em.merge(t);
            return t;
        });
    }

    @Override
    public List findAll() {
        String query = "FROM " + entityClass.getSimpleName();
        return doInTransaction(em -> {
            return em.createQuery(query, entityClass)
                    .getResultList();
        });
    }
}
