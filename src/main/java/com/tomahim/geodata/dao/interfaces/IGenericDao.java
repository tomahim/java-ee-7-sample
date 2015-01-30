package com.tomahim.geodata.dao.interfaces;

import java.io.Serializable;
import java.util.List;

public interface IGenericDao<T, PK extends Serializable> {
	List<T> findAll();
    T findById(PK id);
    T save(T t);
    T update(T t);
    void delete(T t);
}