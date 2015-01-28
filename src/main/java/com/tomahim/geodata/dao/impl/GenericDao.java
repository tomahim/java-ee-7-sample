package com.tomahim.geodata.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.tomahim.geodata.dao.interfaces.IGenericDao;

public class GenericDao <T, PK extends Serializable> implements IGenericDao<T, PK> {

	protected Class<T> entityClass;
	
	//@PersistenceContext(unitName="geodataDS")
	protected EntityManager entityManager;
	
	public GenericDao() {
	    ParameterizedType genericSuperclass = (ParameterizedType) getClass()
	         .getGenericSuperclass();
	    this.entityClass = (Class<T>) genericSuperclass
	         .getActualTypeArguments()[0];
	}
	
	@Override
	public T persist(T t) {
	    this.entityManager.persist(t);
	    return t;
	}
	
	@Override
	public T findById(PK id) {
	    return this.entityManager.find(entityClass, id);
	}
	
	@Override
	public T update(T t) {
	    return this.entityManager.merge(t);
	}
	
	@Override
	public void delete(T t) {
	    t = this.entityManager.merge(t);
	    this.entityManager.remove(t);
	}
}
