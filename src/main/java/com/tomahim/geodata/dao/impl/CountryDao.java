package com.tomahim.geodata.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.tomahim.geodata.dao.interfaces.ICountryDao;
import com.tomahim.geodata.entities.Country;
import com.tomahim.geodata.utils.HibernateUtil;

@Stateless
public class CountryDao implements ICountryDao {

	Session currentSession;
	
	public CountryDao() {
		currentSession = HibernateUtil.getSessionFactory().openSession();
	}
	
	public List<Country> findAll() {
	      return currentSession.createCriteria(Country.class).list();
	}

	@Override
	public Country persist(Country t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Country findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Country update(Country t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Country t) {
		// TODO Auto-generated method stub
		
	}

}
