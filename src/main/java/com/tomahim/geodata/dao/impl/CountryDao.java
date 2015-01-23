package com.tomahim.geodata.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.tomahim.geodata.dao.interfaces.ICountryDao;
import com.tomahim.geodata.entities.Country;

@Stateless
public class CountryDao implements ICountryDao {

	@PersistenceContext(name="MySQLDS")
	protected EntityManager entityManager;
	
	public List<Country> findAll() {
    	TypedQuery<Country> query = entityManager.createNamedQuery("Country.findAll", Country.class);
		List<Country> results = query.getResultList();
	    return results;
	}
	
}
