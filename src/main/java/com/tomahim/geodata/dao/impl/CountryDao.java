package com.tomahim.geodata.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.tomahim.geodata.dao.interfaces.ICountryDao;
import com.tomahim.geodata.entities.Country;

@Stateless
public class CountryDao extends GenericDao<Country, Integer> implements ICountryDao {
	
	public List<Country> findAll() {
    	TypedQuery<Country> query = entityManager.createNamedQuery("Country.findAll", entityClass);
		List<Country> results = query.getResultList();
	    return results;
	}

}
