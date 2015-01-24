package com.tomahim.geodata.dao.interfaces;

import java.io.Serializable;

public interface IGenericDao<T, PK extends Serializable> {
    T persist(T t);
    T findById(PK id);
    T update(T t);
    void delete(T t);
}