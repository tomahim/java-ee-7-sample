package com.tomahim.geodata.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

import com.tomahim.geodata.dao.interfaces.IGenericDao;
import com.tomahim.geodata.entities.Country;
import com.tomahim.geodata.utils.HibernateUtil;

public class GenericDao <T, PK extends Serializable> implements IGenericDao<T, PK> {

	Session currentSession;

	protected Class<T> entityClass;
	
	public GenericDao() {
		currentSession = HibernateUtil.getSessionFactory().openSession();
	    ParameterizedType genericSuperclass = (ParameterizedType) getClass()
	         .getGenericSuperclass();
	    this.entityClass = (Class<T>) genericSuperclass
	         .getActualTypeArguments()[0];
	}
	
	@Override
	public T save(T t) {
	    return (T) currentSession.save(t);
	}
	
	@Override
	public T findById(PK id) {
	    return (T) currentSession.get(entityClass, id);
	}
	
	@Override
	public T update(T t) {
		return (T) currentSession.merge(t);
	}
	
	@Override
	public void delete(T t) {
		currentSession.delete(t);
	}

	@Override
	public List<T> findAll() {
	    return currentSession.createCriteria(entityClass).list();
	}
}
