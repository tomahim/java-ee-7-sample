package com.tomahim.geodata.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.tomahim.geodata.dao.interfaces.IGenericDao;
import com.tomahim.geodata.entities.Country;
import com.tomahim.geodata.utils.HibernateUtil;

public class GenericDao <T, PK extends Serializable> implements IGenericDao<T, PK> {

	protected Class<T> entityClass;
	
	SessionFactory sf;
	
	public GenericDao() {
		sf = HibernateUtil.getSessionFactory();
	    ParameterizedType genericSuperclass = (ParameterizedType) getClass()
	         .getGenericSuperclass();
	    this.entityClass = (Class<T>) genericSuperclass
	         .getActualTypeArguments()[0];
	}
	
	@Override
	public T save(T t) {
	    return (T) sf.getCurrentSession().save(t);
	}
	
	@Override
	public T findById(PK id) {
	    return (T) sf.getCurrentSession().get(entityClass, id);
	}
	
	@Override
	public T update(T t) {
		return (T) sf.getCurrentSession().merge(t);
	}
	
	@Override
	public void delete(T t) {
		sf.getCurrentSession().delete(t);
	}

	@Override
	public List<T> findAll() {
	    return sf.getCurrentSession().createCriteria(entityClass).list();
	}
}
